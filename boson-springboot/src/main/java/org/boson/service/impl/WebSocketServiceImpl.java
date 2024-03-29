package org.boson.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.boson.domain.dto.ChatRecordDto;
import org.boson.domain.dto.RecallMessageDto;
import org.boson.domain.dto.WebsocketMessageDto;
import org.boson.domain.po.ChatRecord;
import org.boson.domain.vo.VoiceVo;
import org.boson.enums.FilePathEnum;
import org.boson.mapper.ChatRecordMapper;
import org.boson.strategy.context.UploadStrategyContext;
import org.boson.util.BeanUtils;
import org.boson.util.HTMLUtils;
import org.boson.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.boson.enums.ChatTypeEnum.*;


/**
 * websocket服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@Service
@ServerEndpoint(value = "/websocket", configurator = WebSocketServiceImpl.ChatConfigurator.class)
public class WebSocketServiceImpl {

    /**
     * 用户session
     */
    private Session session;

    /**
     * 用户session集合
     */
    private static CopyOnWriteArraySet<WebSocketServiceImpl> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();

    @Autowired
    public void setChatRecordMapper(ChatRecordMapper chatRecordMapper) {
        WebSocketServiceImpl.chatRecordMapper = chatRecordMapper;
    }

    @Autowired
    public void setUploadStrategyContext(UploadStrategyContext uploadStrategyContext) {
        WebSocketServiceImpl.uploadStrategyContext = uploadStrategyContext;
    }

    private static ChatRecordMapper chatRecordMapper;

    private static UploadStrategyContext uploadStrategyContext;

    /**
     * 获取客户端真实ip
     */
    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {

        public static String HEADER_NAME = "X-Real-IP";

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            try {
                String firstFoundHeader = request.getHeaders().get(HEADER_NAME.toLowerCase()).get(0);
                sec.getUserProperties().put(HEADER_NAME, firstFoundHeader);
            } catch (Exception e) {
                sec.getUserProperties().put(HEADER_NAME, "未知ip");
            }
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        // 加入连接
        this.session = session;
        WEB_SOCKET_SET.add(this);
        // 更新在线人数
        updateOnlineCount();
        // 加载历史聊天记录
        ChatRecordDto chatRecordDto = listChartRecords(endpointConfig);
        // 发送消息
        WebsocketMessageDto messageDTO = WebsocketMessageDto.builder()
                .type(HISTORY_RECORD.getType())
                .data(chatRecordDto)
                .build();
        synchronized (session) {
            session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebsocketMessageDto messageDTO = JSON.parseObject(message, WebsocketMessageDto.class);
        switch (Objects.requireNonNull(getChatType(messageDTO.getType()))) {
            case SEND_MESSAGE:
                // 发送消息
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), ChatRecord.class);
                // 过滤html标签
                chatRecord.setContent(HTMLUtils.filter(chatRecord.getContent()));
                chatRecord.setCreateBy(0);
                chatRecordMapper.insert(chatRecord);
                messageDTO.setData(chatRecord);
                // 广播消息
                broadcastMessage(messageDTO);
                break;
            case RECALL_MESSAGE:
                // 撤回消息
                RecallMessageDto recallMessage = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), RecallMessageDto.class);
                // 删除记录
                chatRecordMapper.deleteById(recallMessage.getId());
                // 广播消息
                broadcastMessage(messageDTO);
                break;
            case HEART_BEAT:
                // 心跳消息
                messageDTO.setData("pong");
                session.getBasicRemote().sendText(JSON.toJSONString(JSON.toJSONString(messageDTO)));
            default:
                break;
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        // 更新在线人数
        WEB_SOCKET_SET.remove(this);
        updateOnlineCount();
    }

    /**
     * 加载历史聊天记录
     *
     * @param endpointConfig 配置
     * @return 加载历史聊天记录
     */
    private ChatRecordDto listChartRecords(EndpointConfig endpointConfig) {
        // 获取聊天历史记录
        List<ChatRecord> chatRecordList = chatRecordMapper.selectList(new LambdaQueryWrapper<ChatRecord>()
                .ge(ChatRecord::getCreateAt, DateUtil.offsetHour(new Date(), -12)));
        // 获取当前用户ip
        String ipAddress = endpointConfig.getUserProperties().get(ChatConfigurator.HEADER_NAME).toString();
        return ChatRecordDto.builder()
                .chatRecordList(chatRecordList)
                .ipAddress(ipAddress)
                .ipSource(IpUtils.getIpSource(ipAddress))
                .build();
    }

    /**
     * 更新在线人数
     *
     * @throws IOException io异常
     */
    @Async
    public void updateOnlineCount() throws IOException {
        // 获取当前在线人数
        WebsocketMessageDto messageDto = WebsocketMessageDto.builder()
                .type(ONLINE_COUNT.getType())
                .data(WEB_SOCKET_SET.size())
                .build();
        // 广播消息
        broadcastMessage(messageDto);
    }

    /**
     * 发送语音
     *
     * @param voiceVo 语音路径
     */
    public void sendVoice(VoiceVo voiceVo) {
        // 上传语音文件
        String content = uploadStrategyContext.executeUploadStrategy(voiceVo.getFile(), FilePathEnum.VOICE.getPath());
        voiceVo.setContent(content);
        // 保存记录
        ChatRecord chatRecord = BeanUtils.bean2Bean(voiceVo, ChatRecord.class);
        chatRecord.setCreateBy(0);
        chatRecordMapper.insert(chatRecord);
        // 发送消息
        WebsocketMessageDto messageDTO = WebsocketMessageDto.builder()
                .type(VOICE_MESSAGE.getType())
                .data(chatRecord)
                .build();
        // 广播消息
        try {
            broadcastMessage(messageDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 广播消息
     *
     * @param messageDto 消息dto
     * @throws IOException io异常
     */
    private void broadcastMessage(WebsocketMessageDto messageDto) throws IOException {
        for (WebSocketServiceImpl webSocketService : WEB_SOCKET_SET) {
            synchronized (webSocketService.session) {
                webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(messageDto));
            }
        }
    }

}

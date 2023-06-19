package org.boson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.boson.domain.dto.UniqueViewDto;
import org.boson.domain.po.UniqueView;

import java.util.List;


/**
 * 访问量统计服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 查看7天用户量统计
     *
     * @return 用户量
     */
    List<UniqueViewDto> listUniqueViews();
}

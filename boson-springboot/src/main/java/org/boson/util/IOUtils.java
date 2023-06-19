package org.boson.util;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * IO工具类
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
public final class IOUtils {

    public static void writeJSON(PrintWriter writer, Object data) {
        writer.write(JSON.toJSONString(data));
    }

    public static void writeJSON(ServletResponse servletResponse, Object data, Charset charset) throws IOException {
        servletResponse.setContentType(ContentType.build(ContentType.JSON.getValue(), charset));
        IOUtils.writeJSON(servletResponse.getWriter(), data);
    }

    public static void writeJSON(ServletResponse servletResponse, Object data) throws IOException {
        IOUtils.writeJSON(servletResponse, data, StandardCharsets.UTF_8);
    }
}

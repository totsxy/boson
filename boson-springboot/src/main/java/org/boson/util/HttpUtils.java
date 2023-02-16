package org.boson.util;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class HttpUtils {

    public static void writeJSON(PrintWriter writer, Object data) {
        writer.write(JSON.toJSONString(data));
    }

    public static void writeJSON(ServletResponse servletResponse, Object data, Charset charset) throws IOException {
        servletResponse.setContentType(ContentType.build(ContentType.JSON.getValue(), charset));
        HttpUtils.writeJSON(servletResponse.getWriter(), data);
    }

    public static void writeJSON(ServletResponse servletResponse, Object data) throws IOException {
        HttpUtils.writeJSON(servletResponse, data, StandardCharsets.UTF_8);
    }
}

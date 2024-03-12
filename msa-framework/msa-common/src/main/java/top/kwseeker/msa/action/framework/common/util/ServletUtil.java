package top.kwseeker.msa.action.framework.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtil {

    public static String getRemoteId(HttpServletRequest request) {
        String ip = getClientIP(request);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    public static String getClientIP(HttpServletRequest request) {
        return ServletUtil.getClientIP(request);
    }

    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JSONUtil.toJsonStr(object);
        write(response, content, ContentType.JSON);
    }

    public static void write(HttpServletResponse response, String content, ContentType contentType) {
        response.setContentType(contentType.getValue());
        response.setCharacterEncoding("UTF-8");     //设置一下编码方式，不然会显示乱码
        try (PrintWriter writer = response.getWriter()) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.jayce.seckillsystem.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 获取 web 相关的对象
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public class WebUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }
}

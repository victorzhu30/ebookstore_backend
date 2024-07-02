package org.example.bookstorebackend.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.bookstorebackend.entity.UserAuth;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtils {
    public static void setSession(UserAuth userAuth) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("userId", userAuth.getUserId());
            session.setAttribute("isAdmin", userAuth.getIsAdmin());
        }
    }
    /*
    从 HTTP 请求的角度来看，创建的会话（session）是与客户端（比如浏览器）绑定的，
    这意味着只要你在同一个请求上下文或者后续同一个会话过程中（同一个客户端）使用相同会话，其他地方都可以使用同一个会话。
    在 Spring 框架中，通过 HttpServletRequest 和 HttpSession 可以方便地获取和操作会话数据。
    会话的创建和获取：
    在 setSession 方法中，调用 request.getSession() 创建了一个会话（如果没有现有的会话）。
    创建会话后，该会话会通过 HTTP 响应发送到客户端，客户端会保留会话ID（通常通过cookie）。
    会话的共享：
    在同一个客户端后续的请求中，会自动带上会话ID（通过cookie），服务器使用会话ID找到对应的会话。
    因此，不管是同一个请求内的其他方法，还是后续的请求，都可以通过 HttpServletRequest 获取到同一个会话对象。
     */

    private static HttpSession getSession() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            return request.getSession(false);
        }
        return null;
    }

    public static Long getUserId() {
        HttpSession session = getSession();
        return (session != null) ? (Long) session.getAttribute("userId") : null;
    }

    public static Boolean getIsAdmin() {
        HttpSession session = getSession();
        return (session != null) ? (Boolean) session.getAttribute("isAdmin") : null;
    }
}

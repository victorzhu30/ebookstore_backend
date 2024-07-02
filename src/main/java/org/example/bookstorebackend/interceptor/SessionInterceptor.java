package org.example.bookstorebackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("OPTIONS is invoked");
            return true;
        }
        HttpSession session = request.getSession(false);
        System.out.println("Session is " + session);
        if (session != null && session.getAttribute("userId") != null) {
            System.out.println(session.getAttribute("userId"));
            System.out.println("User already logged in");
            return true;
        }
        System.out.println("User not logged in yet");
        response.setStatus(401);
        return false;
    }
}
// 统一的管理员拦截器

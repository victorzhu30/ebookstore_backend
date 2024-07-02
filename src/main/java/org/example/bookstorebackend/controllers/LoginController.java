package org.example.bookstorebackend.controllers;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.bookstorebackend.entity.UserAuth;
import org.example.bookstorebackend.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    /*
    Logger 定义：使用 private static final 定义 Logger 对象，这样它在类加载时就会初始化，并且是线程安全的。
     */

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public ResponseEntity<JSONObject> login(@RequestBody UserAuth userAuth) {
        log.info("Loging in...");

        System.out.println(userAuth.toString());

        int count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_auth WHERE username = ? AND password = ?",
                new Object[]{userAuth.getUsername(), userAuth.getPassword()},Integer.class
        );

        JSONObject response = new JSONObject();

        if (count > 0){

            Long userId = jdbcTemplate.queryForObject(
                    "SELECT user_id FROM user_auth WHERE username = ? AND password = ?",
                    new Object[]{userAuth.getUsername(), userAuth.getPassword()}, Long.class);

            boolean isBanned = jdbcTemplate.queryForObject(
                    "SELECT is_banned FROM user WHERE id = ?",
                    new Object[]{userId}, Boolean.class);

            boolean isAdmin = jdbcTemplate.queryForObject(
                    "SELECT is_admin FROM user WHERE id = ?",
                    new Object[]{userId}, Boolean.class);

            userAuth.setUserId(userId);
            userAuth.setIsAdmin(isAdmin);
            System.out.println(userAuth.toString());
            SessionUtils.setSession(userAuth);
            // 维护session

            if (isBanned) {
                response.put("ok", false);
                response.put("message", "您的帐号已经被禁用");
                return ResponseEntity.ok(response);
            }

            if (isAdmin) {
                response.put("ok", true);
                response.put("message", "登录成功，您的帐号是管理员帐号");
                return ResponseEntity.ok(response);
            }

            response.put("ok", true);
            response.put("message", "登录成功");

            return ResponseEntity.ok(response);
        } else {
            response.put("ok", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/logout")
    public ResponseEntity<JSONObject> logout(HttpServletRequest request){
        /*
        在 Spring MVC 中，HttpSession 和 HttpServletRequest 都可以用来处理 logout 逻辑。两者在功能上的差异和用途如下：
           HttpSession: 主要用于管理会话，它提供了直接访问和操作会话 (session) 的方法。
            HttpServletRequest: 是一个更广泛的类，它不但可以处理会话，还可以处理更多关于请求的上下文信息，比如请求参数、头部信息、路径信息等。
        为什么都可以使用？
        当你定义一个控制器方法时，Spring MVC 可以自动注入一些特殊类型的参数，包括 HttpServletRequest 和 HttpSession。
        这是因为 Spring MVC 会检测你的控制器方法中参数的类型，并自动注入适当的对象。
         */
        log.info("Logging out...");

        HttpSession session = request.getSession(false);
        session.invalidate();

        JSONObject response = new JSONObject();
        response.put("ok", true);
        response.put("message", "已登出");

        return ResponseEntity.ok(response);
    }
}

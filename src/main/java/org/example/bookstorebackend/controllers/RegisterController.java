package org.example.bookstorebackend.controllers;

import com.alibaba.fastjson2.JSONObject;
import org.example.bookstorebackend.entity.UserAuth;
import org.example.bookstorebackend.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RegisterController {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/register")
    public ResponseEntity<JSONObject> register(@RequestBody UserAuth userAuth) {
        log.info("Registering...");
        log.info(userAuth.toString());
        log.info(userAuth.getUsername());
        log.info(userAuth.getEmail());

        JSONObject response = new JSONObject();

        //校验⽤户名是否重复
        int ifUsernameExisted = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_auth WHERE username = ?", new Object[]{userAuth.getUsername()}, Integer.class
        );

        if (ifUsernameExisted > 0) {
            response.put("ok", false);
            response.put("message", "该用户名已经被注册，请更换");
            return ResponseEntity.ok(response);
        }

        int userRowsAffected = jdbcTemplate.update(
                "INSERT INTO user (nickname, email) VALUES (?, ?)",
                new Object[]{userAuth.getUsername(),userAuth.getEmail()}
        );
//        "The username has been registered. Please try another one."

        int generatedKey = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        /*
        复用你的方式可以实现插入数据后获取自增主键，但这个方案在并发环境下可能会出现问题。
        因为 "SELECT LAST_INSERT_ID()" 会返回最后一个生成的自增ID，如果在插入数据和获取ID之间有其他操作插入了数据，那么你将获得的可能不是你刚插入的数据的ID。
         */

        int userAuthRowsAffected = jdbcTemplate.update(
                "INSERT INTO user_auth (password, username, user_id ) VALUES (?, ?, ?)",
                new Object[]{userAuth.getPassword(), userAuth.getUsername(), generatedKey}
        );

        if (userAuthRowsAffected > 0) {
            response.put("ok", true);
            response.put("message", "注册成功！");
            return ResponseEntity.ok(response);
        } else {
            response.put("ok", false);
            response.put("message", "注册失败！");
            return ResponseEntity.ok(response);
        }
    }
}

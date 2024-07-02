package org.example.bookstorebackend.controllers;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpSession;
import org.example.bookstorebackend.entity.*;
import org.example.bookstorebackend.repository.AllUserRepository;
import org.example.bookstorebackend.service.BookService;
import org.example.bookstorebackend.service.CartItemService;
import org.example.bookstorebackend.service.OrderTblService;
import org.example.bookstorebackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.example.bookstorebackend.utils.SessionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrderTblService orderTblService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AllUserRepository allUserRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/user/me")
    public ResponseEntity<User> getMe() {
        Long userId = SessionUtils.getUserId();
        Boolean isAdmin = SessionUtils.getIsAdmin();
        return ResponseEntity.ok(userService.getUserById(userId));
        /*
        把MySQL user表中的isAdmin列名替换为is_admin,LoginController中的sql语句也做相应的修改，bug就消失了
         */
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("geting all users...");
        return ResponseEntity.ok(allUserRepository.findAll().stream().filter(user -> !user.getNickname().contains("admin")).toList());
    }

    @GetMapping("/user/count")
    public ResponseEntity<JSONArray> getUserCost(@RequestParam(name = "startDate") String startDate,
                                                 @RequestParam(name = "endDate") String endDate) {
        Long userId = SessionUtils.getUserId();
        log.info("startDate: " + startDate);
        log.info("endDate: " + endDate);
        log.info("userId: " + userId);
        log.info("Getting A User's Cost...");

        List<OrderTbl> orderTbls;
        List<OrderTbl> filteredItems;

        if (startDate != "" && endDate != "") {
            String startDateTimeString = startDate + "T00:00:00Z";
            String endDateTimeString = endDate + "T23:59:59Z";

            Instant instantStart = Instant.parse(startDateTimeString);
            Instant instantEnd = Instant.parse(endDateTimeString);
            System.out.println("Parsed Instant: " + instantStart + " " + instantEnd);
            orderTbls =  orderTblService.findByCreatedAtBetween(instantStart, instantEnd);
        }
        else {
            orderTbls =  orderTblService.findAll();
        }
        filteredItems = orderTbls.stream().filter(item -> item.getUserId() == userId).toList();

        Map<Long, Integer> userCostCount = new HashMap<>();

        for (OrderTbl filteredItem : filteredItems){
            for (OrderItem orderItem : filteredItem.getOrderItems()) {
                Long bookId = orderItem.getBook().getId();
                Integer number = orderItem.getNumber();
                userCostCount.put(bookId, userCostCount.getOrDefault(bookId, 0) + number);
            }
        }

        JSONArray userCosts = new JSONArray();

        for (Map.Entry<Long, Integer> entry : userCostCount.entrySet()) {
            System.out.println("书籍ID: " + entry.getKey() + ", 购买数量: " + entry.getValue());
            JSONObject userCost = new JSONObject();
            userCost.put("bookID", entry.getKey());
            userCost.put("title", bookService.findBookById(entry.getKey()).getTitle());
            userCost.put("number", entry.getValue());
            userCost.put("price", bookService.findBookById(entry.getKey()).getPrice());
            userCosts.add(userCost);
        }
        return ResponseEntity.ok(userCosts);
        /*
        [
    {
        "bookID": 1,
        "title": "1984",
        "number": 4,
        "price": 28
    },
    {
        "bookID": 2,
        "title": "C++ Primer 中文版（第 5 版）",
        "number": 2,
        "price": 128
    },
    {
        "bookID": 3,
        "title": "Java核心技术·卷I（原书第12版）",
        "number": 3,
        "price": 149
    },
    {
        "bookID": 5,
        "title": "Go专家编程",
        "number": 1,
        "price": 108
    },
    {
        "bookID": 7,
        "title": "吉伊卡哇 這又小又可愛的傢伙 2",
        "number": 7,
        "price": 66
    },
    {
        "bookID": 8,
        "title": "吉伊卡哇 這又小又可愛的傢伙 1",
        "number": 2,
        "price": 66
    },
    {
        "bookID": 9,
        "title": "吉伊卡哇 這又小又可愛的傢伙 3",
        "number": 1,
        "price": 66
    },
    {
        "bookID": 10,
        "title": "吉伊卡哇 這又小又可愛的傢伙 4",
        "number": 1,
        "price": 66
    },
    {
        "bookID": 12,
        "title": "Java编程思想（第4版）",
        "number": 2,
        "price": 99
    }
]
         */
    }

    @GetMapping("/users/rank")
    public ResponseEntity<JSONArray> getUserCosts(@RequestParam(name = "startDate") String startDate,
                                                  @RequestParam(name = "endDate") String endDate) {
        log.info("startDate: " + startDate);
        log.info("endDate: " + endDate);
        log.info("Getting All User Costs...");

        List<OrderTbl> orderTbls;

        if (startDate != "" && endDate != "") {
            String startDateTimeString = startDate + "T00:00:00Z";
            String endDateTimeString = endDate + "T23:59:59Z";

            Instant instantStart = Instant.parse(startDateTimeString);
            Instant instantEnd = Instant.parse(endDateTimeString);
            System.out.println("Parsed Instant: " + instantStart + " " + instantEnd);
            orderTbls =  orderTblService.findByCreatedAtBetween(instantStart, instantEnd);
        }
        else {
            orderTbls =  orderTblService.findAll();
        }

        Map<Long, Integer> userCostsCount = new HashMap<>();

        for (OrderTbl orderTbl : orderTbls){
            Integer costs = 0;
            Long userId = orderTbl.getUserId();
            for (OrderItem orderItem : orderTbl.getOrderItems()) {
                costs += orderItem.getNumber() * orderItem.getBook().getPrice();
            }
            userCostsCount.put(userId, userCostsCount.getOrDefault(userId, 0) + costs);
        }

        JSONArray userCosts = new JSONArray();

        for (Map.Entry<Long, Integer> entry : userCostsCount.entrySet()) {
            System.out.println("用户ID: " + entry.getKey() + ", 总消费额: " + entry.getValue());
            JSONObject userCost = new JSONObject();
            userCost.put("userId", entry.getKey());
            userCost.put("cost", entry.getValue());
            userCosts.add(userCost);
        }

        return ResponseEntity.ok(userCosts);
        /*
        [
    {
        "userId": 1,
        "cost": 1847
    },
    {
        "userId": 2,
        "cost": 861
    },
    {
        "userId": 3,
        "cost": 184
    }
]
         */
    }

    @PutMapping("/user/ban")
    public ResponseEntity<JSONObject> banUser(@RequestBody User user) {
        JSONObject response = new JSONObject();
        System.out.println(user.toString());
        if (user.getIsBanned()) {
            // isBanned为true，解禁该用户
            int rowsAffected = jdbcTemplate.update(
                "UPDATE user SET is_banned = 0 WHERE id = ?",
                new Object[]{user.getId()}
            );
            if (rowsAffected > 0) {
                response.put("ok", true);
                response.put("message", "用户解禁成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("ok", false);
                response.put("message", "用户解禁失败");
                return ResponseEntity.ok(response);
            }
        }

        else {
            // isBanned为false，封禁该用户
            int rowsAffected = jdbcTemplate.update(
                "UPDATE user SET is_banned = 1 WHERE id = ?",
                new Object[]{user.getId()}
            );
            if (rowsAffected > 0) {
                response.put("ok", true);
                response.put("message", "用户封禁成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("ok", false);
                response.put("message", "用户封禁失败");
                return ResponseEntity.ok(response);
            }
        }
    }

    @PutMapping("/user/me/password")
    public ResponseEntity<JSONObject> changePassword(@RequestBody Password password) {
        Long userId = SessionUtils.getUserId();

        int rowsAffected = jdbcTemplate.update(
            "UPDATE user_auth SET password = ? WHERE user_id = ?",
            new Object[]{password.getPassword(), userId}
        );

        if (rowsAffected > 0 ){
            JSONObject response = new JSONObject();
            response.put("ok", true);
            response.put("message", "密码已成功修改");
            return ResponseEntity.ok(response);
        } else {
            JSONObject response = new JSONObject();
            response.put("ok", false);
            response.put("message", "密码修改失败");
            return ResponseEntity.ok(response);
        }
    }
}

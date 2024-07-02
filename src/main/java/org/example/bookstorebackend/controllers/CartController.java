package org.example.bookstorebackend.controllers;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpSession;
import org.example.bookstorebackend.entity.CartItemDto;
import org.example.bookstorebackend.service.CartItemService;
import org.example.bookstorebackend.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDto>> getAllCartItems() {
        Long userId = SessionUtils.getUserId();

        List<CartItemDto> allItems = cartItemService.findAll();
        List<CartItemDto> filteredItems = allItems.stream().filter(item -> item.getUser().getId() == userId).collect(Collectors.toList());
        return ResponseEntity.ok(filteredItems);
    }
    /*
    能不能建一个表，存user_id和sessionID，用户清空浏览器缓存后，生成新的sessionID，替换数据库中旧的
     */

    @PutMapping("/cart")
    public ResponseEntity<JSONObject> addCartItem(@RequestParam String bookId) {
        log.info("Adding a Book to the Cart...");
        JSONObject response = new JSONObject();

        Long userId = SessionUtils.getUserId();
        // 对于特定用户检查该书是否已经被添加到购物车
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM cart_item WHERE book_id = ? AND user_id = ?", new Object[]{bookId, userId}, Integer.class
        );

        if (count > 0) {
            response.put("ok", false);
            response.put("message", "The book is already in the cart. Please do not add it repeatedly.");
            log.info("The Book already in the cart ");
            return ResponseEntity.ok(response);
        }

        int rowsAffected = jdbcTemplate.update(
                //执行一个更新语句
                "INSERT INTO cart_item (number, book_id, user_id) VALUES (1,?,?)", new Object[]{bookId, userId}
        );
        // jdbcTemplate.update() 并不需要Integer.class这一参数

        // 检查更新是否执行成功
        if (rowsAffected > 0) {
            // 如果插入成功，update()方法会返回一个大于0的整数，表示受影响的行数。
            response.put("ok", true);
            response.put("message", "书籍已成功添加到购物车");
            log.info("Adding succeed!");
            return ResponseEntity.ok(response);
        } else {
            response.put("ok", false);
            response.put("message", "添加购物车失败");
            log.info("Adding failed!");
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/cart/{id}")
    public ResponseEntity<JSONObject> updateCartItem(@PathVariable("id") Integer id, @RequestParam Integer number) {
        log.info("Updating a Book's number in the Cart...");
        JSONObject response = new JSONObject();

        Long userId = SessionUtils.getUserId();
        int rowsAffected = jdbcTemplate.update(
                "UPDATE cart_item SET number = ? WHERE book_id = ? AND user_id = ?", new Object[]{number, id, userId}
        );

        if (rowsAffected > 0) {
            response.put("ok", true);
            response.put("message", "购物车信息已成功更新");
            log.info("Updating succeed!");
            return ResponseEntity.ok(response);
        } else {
            response.put("ok", false);
            response.put("message", "购物车信息更新失败");
            log.info("Updating failed!");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<JSONObject> deleteCartItem(@PathVariable("id") Integer id) {
        log.info("Deleting a Book from the Cart...");
        JSONObject response = new JSONObject();

        Long userId = SessionUtils.getUserId();
        int rowsAffected = jdbcTemplate.update("DELETE FROM cart_item WHERE id = ? AND user_id = ?", new Object[]{id, userId});

        if (rowsAffected > 0) {
            response.put("ok", true);
            response.put("message", "该书籍已成功从购物车删除");
            log.info("Deleting succeed!");
            return ResponseEntity.ok(response);
        } else {
            response.put("ok", false);
            response.put("message", "该书籍从购物车删除失败");
            log.info("Deleting failed!");
            return ResponseEntity.ok(response);
        }
    }
}

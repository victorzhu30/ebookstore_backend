package org.example.bookstorebackend.controllers;

import com.alibaba.fastjson2.JSONObject;
import org.aspectj.weaver.ast.Or;
import org.example.bookstorebackend.entity.*;
import org.example.bookstorebackend.repository.UserRepository;
import org.example.bookstorebackend.service.OrderTblService;
import org.example.bookstorebackend.service.UserService;
import org.example.bookstorebackend.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderTblService orderTblService;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;

    @GetMapping("/order/{id}")
    ResponseEntity<OrderTbl> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderTblService.findOne(id));
    }

    @GetMapping("/order")
    public ResponseEntity<List<OrderTbl>> getAllOrders() {

        Long userId = SessionUtils.getUserId();
        Boolean isAdmin = SessionUtils.getIsAdmin();

        if (isAdmin) {
            return ResponseEntity.ok(orderTblService.findAll());
        } else {
            List<OrderTbl> allItems = orderTblService.findAll();
            List<OrderTbl> filteredItems = allItems.stream().filter(item -> item.getUserId() == userId).collect(Collectors.toList());
            return ResponseEntity.ok(filteredItems);
        }
    }

    @GetMapping("/order/search")
    public ResponseEntity<List<OrderTbl>> getOrders(@RequestParam(name="searchTerm") String searchTerm,
                                                    @RequestParam(name="startDate") String startDate,
                                                    @RequestParam(name="endDate") String endDate) {
        Boolean isAdmin = SessionUtils.getIsAdmin();
        Long userId = SessionUtils.getUserId();
        List<OrderTbl> allItems = orderTblService.findAll();
        List<OrderTbl> filteredItems;

        if (!isAdmin) {
            // 非管理员用户只能查看自己的订单
            filteredItems = allItems.stream().filter(item -> item.getUser().getId() == userId).collect(Collectors.toList());
        }

        else {
            // 管理员用户可以查看所有订单
            filteredItems = allItems;
        }
        /*
        在您提供的代码中，存在一个作用域问题，导致在 `if-else` 语句块之外无法访问 `filteredItems` 变量。
        为了解决这个问题，您可以稍作调整：
        通过以上更改，`filteredItems` 将在 `if-else` 块之外声明，这样在整个代码块中都可以访问这个变量。
        这样定义变量的方式是有效的，其中 `filteredItems` 的值根据 `userId` 的不同情况在 `if-else` 块中被赋值。
        这种定义方式保留了对过滤后的订单列表的引用，使得您可以在稍后的代码中使用它。
         */

        if (startDate != "" && endDate != "") {
            System.out.println("searchTerm = " + searchTerm);
            System.out.println("startDate = " + startDate);
            System.out.println("endDate = " + endDate);
            List<OrderTbl> searchResult = new ArrayList<>();
//            LocalDateTime localStartDate = LocalDateTime.parse(startDate);
//            LocalDateTime localEndDate = LocalDateTime.parse(endDate);
            LocalDate localStartDate = LocalDate.parse(startDate);
            LocalDate localEndDate = LocalDate.parse(endDate);
            for (OrderTbl order : filteredItems) {
                ZonedDateTime zonedDateTime = order.getCreatedAt().atZone(ZoneId.systemDefault());
                LocalDate localDateFromOrder = zonedDateTime.toLocalDate();
                // 时区？
//                LocalDateTime localDateFromOrder = order.getCreatedAt();
                if (localStartDate.isBefore(localDateFromOrder) && localEndDate.isAfter(localDateFromOrder)) {
                    List<OrderItem> orderItems = order.getOrderItems();
                    for (OrderItem orderItem : orderItems) {
                        if (orderItem.getBook().getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                            // 将搜索词和书名都转换为小写，使用contains()方法来进行模糊匹配
                            searchResult.add(order);
                            break;
                        }
                    }
                }
            }
            return ResponseEntity.ok(searchResult);
        }
        else {
            System.out.println("searchTerm = " + searchTerm);
            System.out.println("startDate = " + startDate);
            System.out.println("endDate = " + endDate);
            List<OrderTbl> searchResult = new ArrayList<>();
            for (OrderTbl order : filteredItems) {
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getBook().getTitle().contains(searchTerm)) {
                        searchResult.add(order);
                        break;
                    }
                }
            }
            return ResponseEntity.ok(searchResult);
        }
    }

    @PostMapping("/order")
    public ResponseEntity<JSONObject> submitOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Submitting an Order...");
        log.info("orderRequest = " + orderRequest);

        List<Long> itemIds = orderRequest.getItemIds();
        List<Integer> numbers = orderRequest.getNumbers();

        log.info("Calculating the total price of the order...");
        // 计算订单总价
        Integer sum = 0, totalPrice, unitPrice;
        for (int i = 0; i < itemIds.size(); i++) {
            // 更新库存和销量
            jdbcTemplate.update("UPDATE book SET stock = stock - ? WHERE id = ?", new Object[]{numbers.get(i), itemIds.get(i)});
            jdbcTemplate.update("UPDATE book SET sales = sales + ? WHERE id = ?", new Object[]{numbers.get(i), itemIds.get(i)});

            unitPrice = jdbcTemplate.queryForObject(
                    "SELECT price FROM book WHERE id = ?", new Object[]{itemIds.get(i)}, Integer.class
            );
            totalPrice = unitPrice * numbers.get(i);
            sum += totalPrice;
        }
        log.info("Total price of the order: " + sum);

        Long userId = SessionUtils.getUserId();
        log.info("Balance"+ userService.getUserById(userId).getBalance().toString());
//        userService.getUserById(userId).setBalance(userService.getUserById(userId).getBalance() - sum);
//        这很可能是因为您只是修改了 Java 对象的值，而没有将该更新同步到数据库中。
//        调用 userService.getUserById(userId).setBalance(...) 仅仅是在内存中修改了一个 Java 对象的属性，但并未实际更新数据库中的记录。
        int rowsAffected = jdbcTemplate.update(
                "UPDATE user SET balance = balance - ? WHERE id = ?", new Object[]{sum, userId});
        log.info("Balance"+ userService.getUserById(userId).getBalance().toString());

        // 保存订单信息
        int orderTblRowsAffected = jdbcTemplate.update(
                "INSERT INTO order_tbl (address, receiver, tel, user_id, created_at) VALUES (?,?,?,?,?)",
                new Object[]{orderRequest.getAddress(), orderRequest.getReceiver(), orderRequest.getTel(), userId, new Date()}
        );
        // why use new Date()?

        // 获取刚刚插入的订单的id
        int generatedKey = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        // 保存订单项信息
        int insertTimes = orderRequest.getNumbers().size();
        for (int i = 0; i < insertTimes; i++) {
            jdbcTemplate.update(
                    "INSERT INTO order_item (number, book_id, order_id) VALUES (?,?,?)",
                    new Object[]{orderRequest.getNumbers().get(i), orderRequest.getItemIds().get(i), generatedKey}
            );
        }

        // 删除购物车中的商品
        int deleteTimes = orderRequest.getIds().size();
        for (int i = 0; i < deleteTimes; i++) {
            jdbcTemplate.update(
                    "DELETE FROM cart_item WHERE id = ?",
                    new Object[]{orderRequest.getIds().get(i)}
            );
        }

        //improve @RequestBody OrderTbl orderTbl
//        User user = userRepository.findUserById(userId);
//        OrderTbl orderTbl = new OrderTbl();
//        orderTbl.setUser(user);
        /*
        在保存Order的同时保存OrderItem。
        当你创建一个Order时，你可以在其中添加一个或多个OrderItem对象：

        OrderTbl order = new OrderTbl();
        OrderItem item1 = new OrderItem();
        OrderItem item2 = new OrderItem();
        // 设置OrderItem的属性 ...
        order.getOrderItems().add(item1);
        order.getOrderItems().add(item2);
        ```
        然后你只需要保存Order对象，与之关联的OrderItem对象也将自动被保存：
        orderRepository.save(order);。
         */
        JSONObject response = new JSONObject();
        if (orderTblRowsAffected > 0) {
            response.put("ok", true);
            response.put("message", "Order submitted successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("ok", false);
            response.put("message", "Failed to submit order.");
            return ResponseEntity.ok(response);
        }
    }
}

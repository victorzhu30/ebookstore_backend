package org.example.bookstorebackend.controllers;

import com.alibaba.fastjson2.JSONObject;
import org.example.bookstorebackend.entity.Book;
import org.example.bookstorebackend.entity.OrderItem;
import org.example.bookstorebackend.entity.OrderTbl;
import org.example.bookstorebackend.service.BookService;
import org.example.bookstorebackend.service.BookService1;
import org.example.bookstorebackend.service.CartItemService;
import org.example.bookstorebackend.service.OrderTblService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookService1 bookService1;

    @Autowired
    private OrderTblService orderTblService;

    @GetMapping("/books")
    public ResponseEntity<JSONObject> getBooks(@RequestParam String keyword,
                                               @RequestParam Integer pageIndex,
                                               @RequestParam Integer pageSize) {
        JSONObject response = new JSONObject();

        if(keyword!=null && !keyword.isEmpty()){
            log.info("Searching Books...");
            Pageable bookPage = PageRequest.of(pageIndex, pageSize);
            Page<Book> books = bookService.findAllByTitleContaining(keyword, bookPage);
            response.put("items", books.getContent());
            response.put("total", books.getTotalElements());
            return ResponseEntity.ok(response);
        }

        log.info("Getting Books...");
        Pageable bookPage = PageRequest.of(pageIndex, pageSize);
        Page<Book> book = bookService.findAll(bookPage);
        response.put("items", book.getContent());
        response.put("total", book.getTotalElements());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/books/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        log.info("Querying A Book");
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @GetMapping("/books/rank")
    public ResponseEntity<List<Book>> getTop10BestSellingBooks(@RequestParam(name = "startDate") String startDate,
                                                               @RequestParam(name = "endDate") String endDate) {
        log.info("startDate: " + startDate);
        log.info("endDate: " + endDate);
        log.info("Getting Top10 Best Selling Books...");

        if (startDate != "" && endDate != "") {
            String startDateTimeString = startDate + "T00:00:00Z";
            String endDateTimeString = endDate + "T23:59:59Z";

            Instant instantStart = Instant.parse(startDateTimeString);
            Instant instantEnd = Instant.parse(endDateTimeString);
            System.out.println("Parsed Instant: " + instantStart + " " + instantEnd);
            List<OrderTbl> orderTbls =  orderTblService.findByCreatedAtBetween(instantStart, instantEnd);

            /*
            HashMap 是 Java 中用于存储键值对的数据结构，它基于哈希表实现，可以提供快速的查找、插入和删除操作。
            创建 HashMap 对象，其中键为和值均为整数类型。
             */
            Map<Long, Integer> bookCount = new HashMap<>();

            /*
            put(key, value) 方法用于将键值对添加到 HashMap 中。在这里，我们将书籍的 ID（bookId）作为键，将该书籍目前的数量加上订单项中的数量（item.getNumber()）作为值存入 HashMap 中。
            getOrDefault(key, defaultValue) 方法用于获取指定键的值，如果键存在，则返回对应的值；如果键不存在，则返回指定的默认值。这里用于判断 HashMap 中是否已存在该书籍，如果不存在，则默认数量为 0。
             */
            for (OrderTbl orderTbl : orderTbls){
                for (OrderItem orderItem : orderTbl.getOrderItems()) {
                    Long bookId = orderItem.getBook().getId();
                    bookCount.put(bookId, bookCount.getOrDefault(bookId, 0) + orderItem.getNumber());
                }
            }

            /*
            entrySet() 方法用于获取 HashMap 中所有键值对的集合。
            在 for 循环中，我们使用 Map.Entry 对象来表示 HashMap 中的每个键值对，通过 entry.getKey() 获取键，通过 entry.getValue() 获取值。
             */
            for (Map.Entry<Long, Integer> entry : bookCount.entrySet()) {
                System.out.println("书籍ID: " + entry.getKey() + ", 总数: " + entry.getValue());
            }

            // 转换 HashMap 到 List
            List<Map.Entry<Long, Integer>> list = new ArrayList<>(bookCount.entrySet());

            // 使用 Collections 工具类排序 List
            Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {
                @Override
                public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue()); // 降序排列
                }
            });

            // 取出值最大的前 10 个元素
            List<Map.Entry<Long, Integer>> top10 = list.subList(0, Math.min(10, list.size()));

            // 输出前 10 个元素
            for (Map.Entry<Long, Integer> entry : top10) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }

            List<Book> top10Books = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : top10) {
                Book topBook = bookService.findBookById(entry.getKey());
                topBook.setSales(entry.getValue());
                top10Books.add(topBook);
            }
            return ResponseEntity.ok(top10Books);
        }
        return ResponseEntity.ok(bookService.getTop10BestSellingBooks());
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<JSONObject> updateBook(@RequestBody Book book) {
        log.info("Updating A Book");
        log.info("Book: " + book);
        JSONObject response = new JSONObject();
        Book updatedBook = bookService1.updateBook(book);
        if (updatedBook != null) {
            response.put("ok", true);
            response.put("message", "书籍信息已成功修改！");
            return ResponseEntity.ok(response);
        }
        else {
            response.put("ok", false);
            response.put("message", "书籍信息修改失败！");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<JSONObject> deleteBook(@PathVariable Long bookId) {
        log.info("Deleting A Book");
        log.info("Book ID: " + bookId);
        JSONObject response = new JSONObject();
        try {
            bookService1.deleteById(bookId);
            response.put("ok", true);
            response.put("message", "书籍信息已成功删除！");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("ok", false);
            response.put("message", "书籍信息删除失败！");
            return ResponseEntity.ok(response);
        }
    }
    // Remark:级联删除

//        String sql1 = "DELETE FROM cart_item WHERE book_id = ?";
//        String sql2 = "DELETE FROM order_item WHERE book_id = ?";
//        int rowsAffected1 = jdbcTemplate.update(sql1, bookId);
//        if (rowsAffected1 > 0) {
//            int rowsAffected2 = jdbcTemplate.update(sql2, bookId);
//            if (rowsAffected2 > 0) {
//                bookService1.deleteById(bookId);
//                response.put("ok", true);
//                response.put("message", "书籍信息已成功删除！");
//                return ResponseEntity.ok(response);
//            }
//            else {
//                response.put("ok", false);
//                response.put("message", "书籍信息删除失败！");
//                return ResponseEntity.ok(response);
//            }
//        } else {
//            response.put("ok", false);
//            response.put("message", "书籍信息删除失败！");
//            return ResponseEntity.ok(response);
//        }

    @PostMapping("/addbook")
    public ResponseEntity<JSONObject> addBook(@RequestBody Book book){
        log.info("Adding A New Book");
        log.info("Book: " + book);
        JSONObject response = new JSONObject();
        Book newBook = bookService1.updateBook(book);
        if (newBook != null) {
            response.put("ok", true);
            response.put("message", "书籍信息已成功添加！");
            return ResponseEntity.ok(response);
        }
        else {
            response.put("ok", false);
            response.put("message", "书籍信息添加失败！");
            return ResponseEntity.ok(response);
        }
    }
}

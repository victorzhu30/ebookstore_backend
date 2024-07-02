package org.example.bookstorebackend.repository;

import org.example.bookstorebackend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Book findBookById(Long id);
    Page<Book> findAllByTitleContaining(String title, Pageable pageable);
    /*
    这个函数`findAllByTitleContaining`并非预定义的，而是按照Spring Data JPA的命名约定来自动实现的。
    当你定义一个符合Spring Data JPA命名约定的查询方法时（比如`findAllByTitleContaining`），Spring会在运行时自动为该方法生成相应的查询实现。
    这种方式称为"方法命名查询"或者"方法命名策略"。

    在这个例子中，`findAllByTitleContaining`方法的名称由以下几部分组成：
    - `findAll` - 返回所有匹配的结果
    - `By` - 开始定义搜索条件
    - `Title` - 对应我们要查询的实体属性
    - `Containing` - 表示我们希望通过模糊查询（即查找包含某个字符串的结果）

    所以，`findAllByTitleContaining`方法将会执行一个查询，返回所有书名包含提供的输入字符串的书籍。
    你不需要自己实现这个方法，Spring Data JPA 将会为你完成这部分工作。
     */
    @Query(value = "SELECT * FROM book ORDER BY sales DESC LIMIT 10", nativeQuery = true)
    List<Book> findTop10BestSellingBooks();
    /*
ORDER BY sales DESC：根据 sales 列进行排序，DESC 表示降序排序，这样销量最高的记录会排在前面。
LIMIT 10：限制查询结果返回前10条记录。

@Query：用来标记自定义的查询语句，支持 JPQL (Java Persistence Query Language) 和原生 SQL。
通过 value 属性指定查询语句，通过 nativeQuery 属性确定是否是原生 SQL 查询。
value：自定义的查询语句，可以是 JPQL 或原生 SQL。在这个例子中，我们使用的是原生 SQL 查询，因此查询语句为 "SELECT * FROM book ORDER BY sales DESC LIMIT 10"。
nativeQuery：设为 true 表示这是一个原生 SQL 查询，否则会认为是 JPQL 查询。
     */
}
/*
在 Spring Data JPA 中，自定义方法有几种常见方式，包括通过命名规范、使用 @Query 注解以及自定义 Repository 实现。这些方法使开发者可以灵活地在 Repository 中定义和实现复杂的查询逻辑。
1. 基于方法命名规范（Query Methods）
这是最简单的一种方式，Spring Data JPA 会根据方法名自动生成对应的查询。例如：

public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查找用户
    User findByUsername(String username);

    // 根据用户名和邮箱查找用户
    User findByUsernameAndEmail(String username, String email);
}
2. 使用 @Query 注解
如果查询逻辑比较复杂，可以使用 @Query 注解直接书写 JPQL（Java Persistence Query Language）查询。

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.email = :email")
    User findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
}
3. 使用原生 SQL 查询
如果需要使用更复杂的 SQL 查询，可以在 @Query 注解中使用 nativeQuery 属性。

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users WHERE username = :username AND email = :email", nativeQuery = true)
    User findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
}
4. 自定义 Repository 实现
对于非常复杂的查询逻辑，可以通过自定义 Repository 实现来扩展现有的 Repository 接口。
 */
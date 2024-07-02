package org.example.bookstorebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//用于标识这是一个JPA实体类，会被持久化到数据库中
@Data
@NoArgsConstructor
@AllArgsConstructor
//这些是Lombok注解，用于自动生成getter、setter、toString方法，以及默认构造函数和全参构造函数
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "order_tbl")
public class OrderTbl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "tel")
    private String tel;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    /*
    orphanRemoval用于级联删除操作，指示当实体之间的关联关系解除时，是否删除被移除的实体。
    如果设置为 true，表示当父实体中删除了关联子实体时，会自动删除被移除的子实体。
    主要用于父子实体关系，可确保子实体的生命周期和父实体相关联。

    orphanRemoval = true 表示当 OrderTbl 实体中的 OrderItem 移除（解除关联）时，
    被移除的 OrderItem 实体将被删除，以确保数据一致性。
     */
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonIgnore
    /*
    @JsonIgnore是一个Jackson库的注解，用于指示在将 Java 对象序列化为 JSON 格式时忽略某个特定的属性，即不包含该属性的数值。
    这个注解通常用于处理在序列化对象时可能出现的循环引用或不需要在输出的 JSON 数据中显示的属性。

    **循环引用问题**：
    - 循环引用指的是在对象之间相互引用，可能导致无限递归序列化问题。
    - 例如，在你提供的代码中，OrderTbl实体中包含了一个User类型的属性 user，同时User类型对象中可能包含对OrderTbl对象的引用。
    - 当尝试将这类存在循环引用的对象序列化为 JSON 格式时，如果不做处理，可能会导致无限循环、栈溢出或者生成庞大的 JSON 数据。
    - 通过在某一端的属性上使用 `@JsonIgnore` 可以告诉序列化器在序列化时跳过该属性，解决循环引用导致的问题。
     */

    /*
    将@JsonIgnore注解注释掉，可以在OrderTbl实体中序列化User对象，此处暂没有循环引用问题
     */
    @ManyToOne(fetch = FetchType.EAGER)
    /*
    `fetch = FetchType.EAGER` 是 JPA 中的注解属性，用于指定在检索关联实体时的加载策略。
    在处理关联实体对象时，有两种主要的加载策略：EAGER（急加载）和 LAZY（慢加载）。

     FetchType.EAGER（急加载）：
    - 当设置为 EAGER 时，关联实体会立即加载，即在查询包含该实体的主对象时，相关联的实体也会同时被加载。这意味着在查询主对象时，关联实体将立刻从数据库获取并填充到主对象中。
    - 急加载适用于当关联实体通常需要在主对象加载时立即访问时，或者如果主对象与关联对象之间的关系不是太多，可以通过 EAGER 加载预先加载它们避免额外的数据库查询。

     FetchType.LAZY（慢加载）：
    - 当设置为 LAZY 时，关联实体会延迟加载，即关联实体在访问时才会被加载，而不是在查询主对象时就加载。关联对象只有在首次访问时才会从数据库中检索。
    - 懒加载适用于当关联实体的加载会增加额外开销，主对象与关联对象的访问不频繁，或者关联对象的数据量很大时。使用懒加载可避免不必要的数据存取。

     用法建议：
    - 一般来说，如果关联对象的数据量较大，或者对于关联对象的访问不频繁，推荐使用 LAZY 加载，以避免不必要的数据库查询和数据传输。
    - 如果关联实体通常需要立即访问，或者关联对象数据量不大且与主对象之间的关系不是很多，可以考虑使用 EAGER 加载。

    在实际应用中，需要根据业务需求和数据访问模式选择适合的加载策略，以实现最佳性能和数据访问效率。
     */
    @JoinColumn(name = "user_id")
    /*
    @JoinColumnJPA中用于指定关联关系中外键列的注解，用于定义User对象与OrderTbl对象之间的关联：
    - 该注解指定了在数据库中用于关联的外键列名。`User` 对象的主键（通常是 `id`）会被存储在 `OrderTbl` 实体的 `user_id` 列中。
    - 这意味着 `OrderTbl` 实体中的 `user_id` 列会存储与 `User` 实体关联的外键值，用于维护两个实体之间的关系。
    当查询 `OrderTbl` 实体时，JPA 将会根据 `user_id` 列的值，自动关联到 `User` 表中的对应记录，从而实现实体之间的关联查询。
     */
    private User user;


    @Column(name = "created_at")
    private Instant createdAt;
//    private LocalDateTime createdAt;
    /*
    当你在MySQL表中有一个属性类型为 datetime 时，你可以在Spring Data JPA的实体类中将对应的属性设置为 java.time.LocalDateTime 类型。
    这样可以确保数据的正确映射和处理，并与数据库中的 datetime 类型兼容。
     */

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
    /*
    在JPA中，当你在实体类中定义了一个方法（例如`getUserId()`方法），该方法会被看作是一个属性的衍生值。
    这意味着在序列化实体对象时，JPA会将这个方法视作一个属性，将其返回值作为新的属性加入到实体对象中。
    当使用`findAll`方法从仓库（Repository）中获取所有订单记录时，返回的结果会包含实体类中定义的所有属性和任何作为属性的衍生方法的返回值。
    因此，由于`getUserId()`方法在`OrderTbl`实体类中定义并具有含义，它的返回值会被转换为一个`userId`属性在返回的订单记录中显示。
    在实际应用中，这样的设计可以使得在访问实体对象时更加便捷，因为除了实际定义的属性外，还能够包含一些经过处理或计算得到的属性值。
    这对于展示用户实体的相关信息，如用户ID等，能够提供更多的便利。
    如果你不希望在返回结果中包含这个衍生属性`userId`，可以考虑将这类方法设置成`transient`，或者使用`@JsonIgnore`注解确保这个属性不被序列化为JSON。
     */
}
/*
设置关联实体的加载策略（EAGER 或 LAZY）不仅仅影响性能，它还会影响数据的获取方式、数据的一致性以及潜在的数据访问问题。以下是加载策略的一些影响：

### 1. 性能影响：
- **EAGER（急加载）**：会立即加载关联实体，可以减少懒加载带来的延迟；但在查询时可能会一次性加载大量关联数据，增加了数据传输的开销。
- **LAZY（慢加载）**：只有在访问关联实体时才加载数据，可以减少不必要的数据传输；但频繁访问关联数据会导致多次查询数据库，可能会增加额外的查询次数。

### 2. 数据一致性：
- **EAGER**：立即加载所有关联数据，可以确保在内存中拥有完整的对象图；但可能会导致不必要的数据加载和性能问题。
- **LAZY**：延迟加载关联数据，只在访问时才加载，可以节省资源并避免不必要的加载；但可能会出现懒加载异常（LazyInitializationException）或数据库查询次数过多。

### 3. 懒加载异常：
- 使用 LAZY 策略时，如果在关闭数据库连接后访问未加载的懒加载属性，可能会导致懒加载异常。这是因为在会话关闭后无法访问未加载的数据。

### 4. 循环引用问题：
- 当对象之间存在循环引用并且使用默认的序列化方式时，EAGER 可能会导致无限递归的序列化过程，而 LAZY 则可以避免这种问题。

### 5. 数据传输：
- EAGER 策略可能会在不需要相关数据时将不必要的数据传输到客户端，增加网络开销；而 LAZY 策略在需要数据时才从数据库获取，减少不必要的传输。

综上所述，关联实体的加载策略不仅会影响性能，还涉及到数据一致性、懒加载异常、循环引用问题以及数据传输方面的考量。在选择加载策略时，需要综合考虑以上因素，根据具体业务需求和性能要求做出合适的选择。
 */
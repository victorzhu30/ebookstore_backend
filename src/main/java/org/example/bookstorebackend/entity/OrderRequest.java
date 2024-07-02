package org.example.bookstorebackend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequest {
    private List<Long> ids;
    private String address;
    private String receiver;
    private String tel;
//    private LocalDateTime createdAt;
    private List<Integer> numbers;
    private List<Long> itemIds;
}

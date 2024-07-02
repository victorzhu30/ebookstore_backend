package org.example.bookstorebackend.daoImpl;

import org.example.bookstorebackend.dao.CartItemDao;
import org.example.bookstorebackend.entity.BookDto;
import org.example.bookstorebackend.entity.CartItem;
import org.example.bookstorebackend.entity.CartItemDto;
import org.example.bookstorebackend.entity.UserDto;
import org.example.bookstorebackend.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemDaoImpl implements CartItemDao {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartItemDto convertToDto(CartItem cartItem) {
        BookDto bookDto = new BookDto(cartItem.getBook().getId(),cartItem.getBook().getCover(), cartItem.getBook().getPrice(), cartItem.getBook().getTitle(), cartItem.getBook().getDescription());
        UserDto userDto = new UserDto(cartItem.getUser().getId());
        return new CartItemDto(cartItem.getId(), cartItem.getNumber(), bookDto, userDto);
    }
    @Override
    public List<CartItemDto> getAllCartItems() {
        List<CartItem> cartItems =  cartItemRepository.findAll();
        return cartItems.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}

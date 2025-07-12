package com.umutgldn.ecommerce.service;

import com.umutgldn.ecommerce.dto.CartItemRequest;
import com.umutgldn.ecommerce.model.CartItem;
import com.umutgldn.ecommerce.model.Product;
import com.umutgldn.ecommerce.model.User;
import com.umutgldn.ecommerce.repository.CartItemRepository;
import com.umutgldn.ecommerce.repository.ProductRepository;
import com.umutgldn.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (productOpt.isEmpty()) {
            return false;
        }
        if (productOpt.get().getStockQuantity() < request.getQuantity() && userOpt.isEmpty()) {
            return false;
        }

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userOpt.get(), productOpt.get());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(productOpt.get().getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(userOpt.get());
            cartItem.setProduct(productOpt.get());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(productOpt.get().getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        Optional<Product> productOpt = productRepository.findById(productId);
        if (userOpt.isPresent() && productOpt.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }
    public void clearCart(String userId){
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartItemRepository::deleteByUser);
    }
}

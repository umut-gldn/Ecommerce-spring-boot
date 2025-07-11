package com.umutgldn.ecommerce.repository;

import com.umutgldn.ecommerce.model.CartItem;
import com.umutgldn.ecommerce.model.Product;
import com.umutgldn.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);

}

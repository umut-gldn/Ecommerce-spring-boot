package com.umutgldn.ecommerce.service;

import com.umutgldn.ecommerce.dto.OrderResponse;
import com.umutgldn.ecommerce.enums.OrderStatus;
import com.umutgldn.ecommerce.mapper.OrderMapper;
import com.umutgldn.ecommerce.model.CartItem;
import com.umutgldn.ecommerce.model.Order;
import com.umutgldn.ecommerce.model.OrderItem;
import com.umutgldn.ecommerce.model.User;
import com.umutgldn.ecommerce.repository.OrderRepository;
import com.umutgldn.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Optional<OrderResponse> createOrder(String userId){
        List<CartItem> cartItems = cartService.getCart(userId);

        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        Optional<User> userOpt=userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal totalAmount=cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Order order=new Order();
        order.setUser(userOpt.get());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems=cartItems.stream()
                .map(item-> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
        Order savedOrder=orderRepository.save(order);

        cartService.clearCart(userId);
        return Optional.of(orderMapper.mapToOrderResponse(savedOrder));
    }
}

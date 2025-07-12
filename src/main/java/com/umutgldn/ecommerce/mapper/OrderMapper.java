package com.umutgldn.ecommerce.mapper;

import com.umutgldn.ecommerce.dto.OrderResponse;
import com.umutgldn.ecommerce.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse mapToOrderResponse(Order order);
}

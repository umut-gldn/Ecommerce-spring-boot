package com.umutgldn.ecommerce.mapper;

import com.umutgldn.ecommerce.dto.ProductRequest;
import com.umutgldn.ecommerce.dto.ProductResponse;
import com.umutgldn.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse productToResponse(Product product);
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}

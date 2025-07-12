package com.umutgldn.ecommerce.service;

import com.umutgldn.ecommerce.dto.ProductRequest;
import com.umutgldn.ecommerce.dto.ProductResponse;
import com.umutgldn.ecommerce.mapper.ProductMapper;
import com.umutgldn.ecommerce.model.Product;
import com.umutgldn.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();
        productMapper.updateProductFromRequest(request, product);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    productMapper.updateProductFromRequest(request, existingProduct);
                    Product savedProduct = productRepository.save(existingProduct);
                    return productMapper.productToResponse(savedProduct);
                });
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::productToResponse)
                .toList();
    }

    public boolean deleteProduct(Long id){
        return productRepository.findById(id)
                .map(product->{
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }
    public List<ProductResponse> searchProducts(String keyword){
        return productRepository.searchProducts(keyword)
                .stream()
                .map(productMapper::productToResponse)
                .toList();
    }

}

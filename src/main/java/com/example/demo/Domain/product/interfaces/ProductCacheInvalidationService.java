package com.example.demo.Domain.product.interfaces;

import java.util.UUID;

public interface ProductCacheInvalidationService {
    public void evictProduct(UUID productId);
    public void evictAllProducts();
}

package com.example.demo.Application.product.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.product.events.ProductDeletedEvent;
import com.example.demo.Domain.product.events.ProductUpdatedEvent;
import com.example.demo.Domain.product.interfaces.ProductCacheInvalidationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCacheInvalidationHandler {
    private final ProductCacheInvalidationService productCacheInvalidationService;
    @EventListener
    public void handleProductUpdated(ProductUpdatedEvent productUpdatedEvent){
        productCacheInvalidationService.evictProduct(productUpdatedEvent.productId());
        productCacheInvalidationService.evictAllProducts();
    }

    @EventListener
    public void handleProductDeleted(ProductDeletedEvent productDeletedEvent){
        productCacheInvalidationService.evictProduct(productDeletedEvent.productId());
        productCacheInvalidationService.evictAllProducts();
    }
}

package com.example.demo.domain.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.events.ProductCreated;
import com.example.demo.Domain.shared.Result;

public class ProductTest {
    @Test
    void should_create_product_and_register_event(){
        // // arrange
        // // 1. create name value object
        // Result<Name> nameResult=Name.create("Laptop");
        // Name name=nameResult.getValue();
        // // 2. create stock value object
        // Result<Stock> stockResult=Stock.create(100);
        // Stock stock=stockResult.getValue();
        // // 3. set price and description
        // String description="This is laptop for sale";
        // int price=100;
        // // 4. create product object
        // Result<Product> productResult=Product.create(name, description, price, stock);
        // // 5 check if the result success is true
        // assertTrue(productResult.isSuccess());
        // assertNotNull(productResult.getValue().getId());
        // assertEquals("Laptop", productResult.getValue().getName().getValue());

        // assertFalse(productResult.getValue().getDomainEvents().isEmpty());
        // assertTrue(productResult.getValue().getDomainEvents().get(0) instanceof ProductCreated);
    }
}

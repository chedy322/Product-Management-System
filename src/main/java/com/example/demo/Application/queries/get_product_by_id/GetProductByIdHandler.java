package com.example.demo.Application.queries.get_product_by_id;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.queries.ProductQueryService;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProductByIdHandler {
    private final ProductQueryService productQueryService;
    public Result<GetProductByIdDTO> handle(UUID productId){
        Optional<GetProductByIdDTO> productData=productQueryService.getProductViewById(productId);
        if(productData.isEmpty()){
            Error error=Error.NOT_FOUND("Product doesn't exist");
            return Result.Failure(error);
        }
        return Result.Success(productData.get());
    }
}

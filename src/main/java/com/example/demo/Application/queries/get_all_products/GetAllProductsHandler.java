package com.example.demo.Application.queries.get_all_products;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Application.queries.ProductQueryService;
import com.example.demo.Domain.shared.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAllProductsHandler {
    private final ProductQueryService productQueryService;

    public Result<List<GetAllProductsDTO>> handle(){
        return Result.Success(productQueryService.getProductsView());
    } 
}

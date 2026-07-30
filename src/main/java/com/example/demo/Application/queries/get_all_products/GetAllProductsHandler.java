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

    public Result<List<GetAllProductsDTO>> handle(int page,int limit){
        int page_number=Math.max(page, 1);
        int limit_number=Math.min(limit,5);
        int offset=(page_number-1)*limit_number;
        return Result.Success(productQueryService.getProductsView(offset,limit_number));
    } 
}

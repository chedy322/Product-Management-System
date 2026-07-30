package com.example.demo.Application.queries.get_user_products;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.dto.authenticatedUser.AuthenticatedUser;
import com.example.demo.Domain.shared.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserProductsHandler {
    private final GetUserProductsService getUserProductsService;

    public Result<List<GetUserProductsDTO>> handle(AuthenticatedUser user,int page,int limit){
            int page_number=Math.max(1,page);
            int limit_number=Math.min(5,limit);
            int offset=(page_number-1)*limit_number;
            return Result.Success(getUserProductsService.findProductsByUserId(user.id(),offset,limit_number));
    }
}

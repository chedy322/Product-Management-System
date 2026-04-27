package com.example.demo.Application.queries.get_user_products;

import java.util.List;
import java.util.UUID;




public interface GetUserProductsService {
     List<GetUserProductsDTO> findProductsByUserId(UUID userId);
}

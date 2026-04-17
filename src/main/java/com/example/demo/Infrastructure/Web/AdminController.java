package com.example.demo.Infrastructure.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Application.user.UserService;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Infrastructure.Web.dto.adminController.RequestChangeRoleStatus;
import com.example.demo.Infrastructure.Web.dto.adminController.ResponseUserDetails;
import com.example.demo.Infrastructure.Web.dto.adminController.UserSummaryResponse;
import com.example.demo.Infrastructure.config.Enum.UserRole;
import com.example.demo.Infrastructure.Web.helper.ApiController;
import com.example.demo.Infrastructure.persistence.user.UserPersistenceAdapter;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController extends ApiController{
    private final UserService userService;

    // Get List of all users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        // 1.Get all users list from db 
       Result<List<User>> usersResult=userService.findAll();
       return handleResult(
        usersResult,
        users-> users.stream().map(UserSummaryResponse::map).toList()
       );
    }

    // Get user by Id
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        Result<User> userResult=userService.findById(userId);
        return handleResult(
            userResult,
            ResponseUserDetails::map
        );
    }       
    

    // delete user by id 

    // Change selected user role
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<Map<String,String>> changeRoleStatus(@PathVariable UUID userId,@RequestBody RequestChangeRoleStatus newUserRole ,HttpServletResponse response) {
    
          return UserRole.map(newUserRole.newUserRole()).map(
            role->{
                Result<Boolean> changeUserStatusResult=userService.changeRoleStatus(userId, role);
                if(changeUserStatusResult.isFailure()){
                    Error error=changeUserStatusResult.getError();
                    return ResponseEntity.status(error.httpStatus()).body(Map.of("error",error.errorMsg()));
                }
                return ResponseEntity.status(200).body(Map.of("message", "User role updated and sessions invalidated."));
            }
        ).orElseGet(()->{
            Error error=Error.VALIDATION_ERROR("Invalid role profile");
            return ResponseEntity.status(error.httpStatus()).body(Map.of("error",error.errorMsg()));
     } );

        
    }

    
}

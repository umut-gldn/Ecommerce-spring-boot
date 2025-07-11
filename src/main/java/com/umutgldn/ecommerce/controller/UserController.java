package com.umutgldn.ecommerce.controller;

import com.umutgldn.ecommerce.dto.UserRequest;
import com.umutgldn.ecommerce.dto.UserResponse;
import com.umutgldn.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
        @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest request){
        userService.addUser(request);
        return ResponseEntity.ok("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody UserRequest request){
        boolean updated = userService.updateUser(id,request);
        if(updated){
            return ResponseEntity.ok("User updated successfully");
        }
            return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
       try {
           userService.deleteUser(id);
           return ResponseEntity.ok("User deleted successfully");
       }catch (RuntimeException e){
           return ResponseEntity.notFound().build();
       }
    }


}

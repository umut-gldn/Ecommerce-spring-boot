package com.umutgldn.ecommerce.service;

import com.umutgldn.ecommerce.dto.UserRequest;
import com.umutgldn.ecommerce.dto.UserResponse;
import com.umutgldn.ecommerce.mapper.UserMapper;
import com.umutgldn.ecommerce.model.User;
import com.umutgldn.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsers(){
        return  userRepository.findAll().stream()
                .map(userMapper::userToResponse)
                .toList();
    }
    public void addUser(UserRequest request){
        User user = new User();
        userRepository.save(userMapper.userFromRequest(request));
    }
    public Optional<UserResponse> getUserById(Long id){
        return userRepository.findById(id)
                .map(userMapper::userToResponse);
    }
    public boolean updateUser(Long id,UserRequest request){
      return userRepository.findById(id)
              .map(existingUser->{
                      userMapper.updateUserFromRequest(request,existingUser);
              userRepository.save(existingUser);
              return true;
             }).orElse(false);
    }
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));
        userRepository.delete(user);
    }

}

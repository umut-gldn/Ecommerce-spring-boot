package com.umutgldn.ecommerce.mapper;

import com.umutgldn.ecommerce.dto.UserRequest;
import com.umutgldn.ecommerce.dto.UserResponse;
import com.umutgldn.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User userFromRequest(UserRequest request);
    UserResponse userToResponse(User user);
    void updateUserFromRequest(UserRequest request, @MappingTarget User user);
}

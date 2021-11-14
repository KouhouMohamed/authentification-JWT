package com.enset.authentification.mappers;

import com.enset.authentification.entities.AppUser;
import com.enset.authentification.request.UserRequest;
import com.enset.authentification.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser userRequestToUser(UserRequest userRequest);
    UserResponse appUserToUserResponse(AppUser appUser);
}

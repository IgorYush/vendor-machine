package com.vendor.mapper;

import com.vendor.models.User;
import com.vendor.models.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    UserResponse modelToDto(User user);
}

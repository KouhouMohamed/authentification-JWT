package com.enset.authentification.mappers;

import com.enset.authentification.entities.AppRole;
import com.enset.authentification.request.RoleRequest;
import com.enset.authentification.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    AppRole roleRequestToAppRole(RoleRequest roleRequest);
    RoleResponse appRoleToRoleResponse(AppRole appRole);
}

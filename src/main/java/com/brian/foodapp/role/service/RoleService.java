package com.brian.foodapp.role.service;

import com.brian.foodapp.response.Response;
import com.brian.foodapp.role.dtos.RoleDTO;
import java.util.List;

public interface RoleService {

    Response<RoleDTO> createRole(RoleDTO roleDTO);

    Response<RoleDTO> updateRole(RoleDTO roleDTO);

    Response<List<RoleDTO>> getAllRoles();

    Response<?> deleteRole(Long id);
}

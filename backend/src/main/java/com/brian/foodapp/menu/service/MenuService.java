package com.brian.foodapp.menu.service;

import com.brian.foodapp.menu.dtos.MenuDTO;
import com.brian.foodapp.response.Response;
import java.util.List;

public interface MenuService {

    Response<MenuDTO> createMenu(MenuDTO menuDTO);

    Response<MenuDTO> updateMenu(MenuDTO menuDTO);

    Response<MenuDTO> getMenuById(Long id);

    Response<?> deleteMenuById(Long id);

    Response<List<MenuDTO>> getAllMenus(Long categoryId, String search);
}

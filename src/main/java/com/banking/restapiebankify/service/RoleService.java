package com.banking.restapiebankify.service;

import com.banking.restapiebankify.model.Role;

public interface RoleService {
    Role findRoleByName(String name);
}

package com.lab.expenseManager.user.repository;

import com.lab.expenseManager.user.domain.Role;
import com.lab.expenseManager.user.enums.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}

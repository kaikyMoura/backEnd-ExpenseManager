package com.lab.expenseManager.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lab.expenseManager.expense.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

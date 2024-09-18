package com.lab.expenseManager.expense.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.expenseManager.expense.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query("SELECT c FROM Category c WHERE c.name = :name")
	Optional<Category> findByName(@Param(value = "name") String name);
}

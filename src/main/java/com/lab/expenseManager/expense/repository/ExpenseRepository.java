package com.lab.expenseManager.expense.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.expenseManager.expense.domain.Expense;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
	@Query("SELECT e FROM Expense e WHERE e.user.id = :user_Id")
	List<Expense> findExpensesByUserId(@Param("user_Id") UUID userId);
}

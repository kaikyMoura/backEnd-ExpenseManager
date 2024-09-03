package com.lab.expenseManager.expense.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.expenseManager.expense.domain.Expense;
import com.lab.expenseManager.expense.dto.RetrieveUserExpensesDto;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<RetrieveUserExpensesDto> findByUserId(@Param(value = "user_id") UUID user_id);
}

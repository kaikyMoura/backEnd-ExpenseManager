package com.lab.expenseManager.expense.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lab.expenseManager.expense.domain.Category;
import com.lab.expenseManager.expense.dto.RetrieveUserExpensesDto;

@Service
public class ExpenseCategoryService {
	
	//private final CategoryRepository categoryRepository;
	private final ExpenseService expenseService;
	
	public ExpenseCategoryService(ExpenseService expenseService) {
		//this.categoryRepository = categoryRepository;
		this.expenseService = expenseService;
	}

	public List<Category> retriveExpensesCategories() throws Exception {

		List<RetrieveUserExpensesDto> expenses = expenseService.getUserExpenses();

		return expenses.stream().map(RetrieveUserExpensesDto::category).distinct().collect(Collectors.toList());
	}
}

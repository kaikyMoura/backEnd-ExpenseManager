package com.lab.expenseManager.expense.dto;

import java.util.List;
import java.util.UUID;

import com.lab.expenseManager.expense.domain.Category;
import com.lab.expenseManager.expense.enums.Priority;

public record RetrieveUserExpensesDto(
		UUID id,
		String name,
		String description,
	    Double amount,
	    String date,
	    Category category,
	    String currency,
	    Boolean isRecurring,
	    List<String> attachments,
	    Priority priority
		) {}

package com.lab.expenseManager.expense.dto;

import java.util.ArrayList;
import java.util.Date;

import com.lab.expenseManager.expense.domain.Category;
import com.lab.expenseManager.expense.enums.Priority;

public record ExpenseDto(
		Long id,
		String name,
		String description,
	    Double amount,
	    Date date,
	    Category category,
	    String currency,
	    Boolean isRecurring,
	    ArrayList<String> attachments,
	    Priority priority
	    ) {}

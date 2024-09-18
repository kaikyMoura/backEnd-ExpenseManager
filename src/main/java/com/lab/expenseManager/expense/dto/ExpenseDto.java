package com.lab.expenseManager.expense.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.lab.expenseManager.expense.enums.Priority;

public record ExpenseDto(
		UUID id,
		String name,
		String description,
	    Double amount,
	    Date date,
	    CategoryDto category,
	    String currency,
	    Boolean isRecurring,
	    ArrayList<String> attachments,
	    Priority priority
	    ) {}

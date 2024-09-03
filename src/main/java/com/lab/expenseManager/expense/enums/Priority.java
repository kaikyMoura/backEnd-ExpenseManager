package com.lab.expenseManager.expense.enums;

public enum Priority {
	VERY_LOW("Very low"), 
	LOW("Low"), 
	MEDIUM("Medium"), 
	HIGH("High"), 
	VERY_HIGH("Very high");

	String priority;

	Priority(String priority) {
		this.priority = priority;
	}
}

package com.lab.expenseManager.model;

public record ResponseWithDataModel(
		int statusCode,
		String message,
		Object data
	        ){}
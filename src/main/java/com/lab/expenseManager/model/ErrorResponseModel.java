package com.lab.expenseManager.model;

public record ErrorResponseModel(int statusCode, String message, String details) {
}

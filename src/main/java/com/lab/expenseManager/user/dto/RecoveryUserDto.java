package com.lab.expenseManager.user.dto;


public record RecoveryUserDto (
	String name,
	String lastName,
    String email,
	String image
) {}
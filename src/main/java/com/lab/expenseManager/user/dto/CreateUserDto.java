package com.lab.expenseManager.user.dto;

public record CreateUserDto(

	String name,
	String lastName,
    String email,
    String password,
    String role,
    String profileImage
        ){}

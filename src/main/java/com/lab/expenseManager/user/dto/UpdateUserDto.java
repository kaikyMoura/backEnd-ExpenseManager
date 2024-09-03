package com.lab.expenseManager.user.dto;

public record UpdateUserDto(
		String name,
		String lastName,
	    String email,
	    String userImage
	        ){}
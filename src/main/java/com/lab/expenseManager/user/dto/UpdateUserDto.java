package com.lab.expenseManager.user.dto;

import com.lab.expenseManager.user.enums.Status;

public record UpdateUserDto(
		String name,
		String lastName,
	    String email,
	    String userImage,
	    Status status
	        ){}
package com.lab.expenseManager.expense.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.expenseManager.expense.dto.ExpenseDto;
import com.lab.expenseManager.expense.service.ExpenseService;
import com.lab.expenseManager.model.ResponseModel;
import com.lab.expenseManager.model.ResponseWithDataModel;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/expense")
public class ExpenseController {

	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@GetMapping(value = "/list")
	public ResponseEntity<ResponseWithDataModel> findAll() {
		try {
			return new ResponseEntity<>(new ResponseWithDataModel(200, "Operação realizada com sucesso.", expenseService.getUserExpenses()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/create")
	public ResponseEntity<ResponseModel> create(@RequestBody ExpenseDto updateExpenseDto) {
		expenseService.create(updateExpenseDto);
		return new ResponseEntity<>(new ResponseModel(201, "Operação realizada com sucesso"), HttpStatus.CREATED);
	}
}

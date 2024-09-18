package com.lab.expenseManager.expense.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.expenseManager.expense.domain.Category;
import com.lab.expenseManager.expense.dto.CategoryDto;
import com.lab.expenseManager.expense.service.CategoryService;
import com.lab.expenseManager.model.ResponseModel;
import com.lab.expenseManager.model.ResponseWithDataModel;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/category")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping(value = "/list")
	public ResponseEntity<ResponseWithDataModel> list() {
		try {
			List<Category> data = categoryService.findAll();
			return new ResponseEntity<>(new ResponseWithDataModel(200, "Operação realizada com sucesso.", data),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/create")
	public ResponseEntity<ResponseModel> create(@RequestBody CategoryDto categoryDto) {
		try {
			categoryService.create(categoryDto);
			return new ResponseEntity<>(new ResponseModel(201, "Operação realizada com sucesso"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

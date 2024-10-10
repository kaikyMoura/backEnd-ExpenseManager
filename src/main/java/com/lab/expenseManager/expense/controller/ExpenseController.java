package com.lab.expenseManager.expense.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab.expenseManager.expense.dto.ExpenseDto;
import com.lab.expenseManager.expense.dto.RetrieveUserExpensesDto;
import com.lab.expenseManager.expense.enums.Priority;
import com.lab.expenseManager.expense.service.ExpenseService;
import com.lab.expenseManager.model.ErrorResponseModel;
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

	@GetMapping("/list")
	public ResponseEntity<?> findAll() throws Exception {
		List<RetrieveUserExpensesDto> expenses = expenseService.getUserExpenses();

		if (expenses.isEmpty()) {
			return new ResponseEntity<>(new ErrorResponseModel(404, "Nenhum resultado encontrado",
					"Nenhuma despesa foi criada por esse usuário."), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(new ResponseWithDataModel(200, "Operação realizada com sucesso.", expenses),
				HttpStatus.OK);
	}

	@GetMapping("/list/categories")
	public ResponseEntity<ResponseWithDataModel> getExpensesCategories() throws Exception {
		return new ResponseEntity<>(new ResponseWithDataModel(200, "Operação realizada com sucesso.",
				expenseService.retriveExpensesCategories()), HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ExpenseDto expenseDto) {
		for (Priority priority : Priority.values()) {
			if (priority == expenseDto.priority()) {
				return new ResponseEntity<>(new ErrorResponseModel(400, "Os dados fornecidos são invalidos.",
						"Valor invalido para o campo prioridade."), HttpStatus.BAD_REQUEST);
			}
		}

		expenseService.create(expenseDto);
		return new ResponseEntity<>(new ResponseModel(201, "Operação realizada com sucesso"), HttpStatus.CREATED);

	}

	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam("id") UUID id) {
		expenseService.delete(id);
		return new ResponseEntity<>(new ResponseModel(200, "Operação realizada com sucesso"), HttpStatus.ACCEPTED);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody ExpenseDto expenseDto) {
		for (Priority priority : Priority.values()) {
			if (priority == expenseDto.priority()) {
				return new ResponseEntity<>(new ErrorResponseModel(400, "Os dados fornecidos são invalidos.",
						"Valor invalido para o campo prioridade."), HttpStatus.BAD_REQUEST);
			}
		}
		expenseService.update(expenseDto);
		return new ResponseEntity<>(new ResponseModel(200, "Operação realizada com sucesso"), HttpStatus.ACCEPTED);
	}
}

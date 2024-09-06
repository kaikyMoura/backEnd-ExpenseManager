package com.lab.expenseManager.expense.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lab.expenseManager.expense.domain.Category;
import com.lab.expenseManager.expense.domain.Expense;
import com.lab.expenseManager.expense.dto.CategoryDto;
import com.lab.expenseManager.expense.dto.ExpenseDto;
import com.lab.expenseManager.expense.dto.RetrieveUserExpensesDto;
import com.lab.expenseManager.expense.repository.ExpenseRepository;
import com.lab.expenseManager.user.domain.User;
import com.lab.expenseManager.user.service.UserService;

@Service
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserService userService;
	private final CategoryService categoryService;

	public ExpenseService(ExpenseRepository expenseRepository, UserService userService, CategoryService categoryService) {
		this.expenseRepository = expenseRepository;
		this.userService = userService;
		this.categoryService = categoryService;
	}

	public List<Expense> findAll() {
		return expenseRepository.findAll();
	}

	public List<RetrieveUserExpensesDto> getUserExpenses() throws Exception {
		try {
			List<RetrieveUserExpensesDto> expensesDtos = new ArrayList<>();
			User user = userService.getUser();

			List<Expense> expenses = expenseRepository.findByUserId(user.getId());

			if (expenses.isEmpty()) {
	            System.out.println("Nenhuma despesa encontrada para o usuário.");
	            return Collections.emptyList();
	        }
			
			expenses.stream().map((expense) -> 
			expensesDtos.add(new RetrieveUserExpensesDto(expense.getId(), expense.getName(), 
					expense.getDescription(), expense.getAmount(), expense.getDate(), expense.getCategory(), 
					expense.getCurrency(), expense.getIsRecurring(), expense.getAttachments(), 
					expense.getPriority()))).collect(Collectors.toList());
			
			return expensesDtos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao executar a operação");
		}
	}

	public Page<Expense> getPageable(Pageable pageable) {
		return null;
	}

	public void create(ExpenseDto expenseDto) {
		try {
			
			List<RetrieveUserExpensesDto> lista = getUserExpenses();
			
			Category category = (Category) expenseDto.category();
			
			// Verifica se já existe alguma categoria com o mesmo nome na lista de despesas do usuário
			if (lista.stream().map(RetrieveUserExpensesDto:: category)
					.anyMatch(name -> name.equals(category.getName()))) {
				categoryService.create(new CategoryDto(category.getName()));
			}
			
			expenseRepository.save(Expense.builder().name(expenseDto.name()).description(expenseDto.description())
					.category(expenseDto.category()).amount(expenseDto.amount()).currency(expenseDto.currency())
					.isRecurring(expenseDto.isRecurring()).attachments(expenseDto.attachments())
					.priority(expenseDto.priority()).date(expenseDto.date()).user(userService.getUser()).build());
		} catch (Exception e) {
			throw new RuntimeException("Erro ao executar a operação");
		}
	}

	public RetrieveUserExpensesDto getById(Long id) {
		Expense expense = expenseRepository.findById(id).get();
		return new RetrieveUserExpensesDto(expense.getId(), expense.getName(), expense.getDescription(),
				expense.getAmount(), expense.getDate(), expense.getCategory(), expense.getCurrency(),
				expense.getIsRecurring(), expense.getAttachments(), expense.getPriority());
	}

	public void deleteById(Long id) {
		try {
			expenseRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("Despesa não encontrada.");
		}
	}

	public void update(ExpenseDto updateExpenseDto) {
		try {
			this.getById(updateExpenseDto.id());
			expenseRepository.save(Expense.builder().name(updateExpenseDto.name())
					.description(updateExpenseDto.description()).category(updateExpenseDto.category())
					.amount(updateExpenseDto.amount()).currency(updateExpenseDto.currency())
					.isRecurring(updateExpenseDto.isRecurring()).attachments(updateExpenseDto.attachments())
					.priority(updateExpenseDto.priority()).date(updateExpenseDto.date()).build());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao realizar a operação", e.getCause());
		}

	}
}

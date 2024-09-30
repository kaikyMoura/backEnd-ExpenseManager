package com.lab.expenseManager.expense.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

	public ExpenseService(ExpenseRepository expenseRepository, UserService userService,
			CategoryService categoryService) {
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

			List<Expense> expenses = expenseRepository.findExpensesByUserId(user.getId());

			if (expenses.isEmpty()) {
				return Collections.emptyList();
			}

			expenses.stream()
					.map((expense) -> expensesDtos.add(new RetrieveUserExpensesDto(expense.getId(), expense.getName(),
							expense.getDescription(), expense.getAmount(),
							new SimpleDateFormat("yyyy/MM/dd").format(expense.getDate()).toString(),
							expense.getCategory(), expense.getCurrency(), expense.getIsRecurring(),
							expense.getAttachments(), expense.getPriority())))
					.collect(Collectors.toList());

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
			List<Category> lista = this.retriveExpensesCategories();

			CategoryDto categoryDto = expenseDto.category();

			Category category = null;

			// Verifica se já existe alguma categoria com o mesmo nome na lista de despesas
			// do usuário
			if (categoryDto != null) {

				category = lista.stream().filter(Objects::nonNull)
						.filter(cat -> cat.getName().equals(categoryDto.name())).findFirst().orElse(null);

				if (category == null) {
					category = categoryService.findOrCreateCategoryByName(categoryDto.name());
				}
			}

			expenseRepository.save(Expense.builder().id(UUID.randomUUID()).name(expenseDto.name())
					.description(expenseDto.description()).category(category).amount(expenseDto.amount())
					.currency(expenseDto.currency()).isRecurring(expenseDto.isRecurring())
					.attachments(expenseDto.attachments())
					.priority(expenseDto.priority())
					.date(expenseDto.date()).user(userService.getUser()).build());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar a operação");
		}
	}

	public RetrieveUserExpensesDto getById(UUID id) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Nenhuma despesa foi encontrada com este Id."));

		return new RetrieveUserExpensesDto(expense.getId(), expense.getName(), expense.getDescription(),
				expense.getAmount(), new SimpleDateFormat("yyyy/MM/dd").format(expense.getDate()).toString(),
				expense.getCategory(), expense.getCurrency(), expense.getIsRecurring(), expense.getAttachments(),
				expense.getPriority());
	}

	public void delete(UUID id) {
		expenseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Nenhuma despesa foi encontrada com este Id."));
		expenseRepository.deleteById(id);
	}

	public void update(ExpenseDto updateExpenseDto) {
		try {

			List<Category> lista = this.retriveExpensesCategories();

			CategoryDto categoryDto = updateExpenseDto.category();

			Category category = lista.stream().filter(Objects::nonNull)
					.filter(cat -> cat.getName().equals(categoryDto.name())).findFirst().orElse(null);
			// Verifica se já existe alguma categoria com o mesmo nome na lista de despesas
			// do usuário
			if (category == null) {
				categoryService.create(new CategoryDto(categoryDto.name()));
			}

			this.getById(updateExpenseDto.id());
			expenseRepository
					.save(Expense.builder().name(updateExpenseDto.name()).description(updateExpenseDto.description())
							.category(category).amount(updateExpenseDto.amount()).currency(updateExpenseDto.currency())
							.isRecurring(updateExpenseDto.isRecurring()).attachments(updateExpenseDto.attachments())
							.priority(updateExpenseDto.priority())
							.date(updateExpenseDto.date()).build());

		} catch (Exception e) {
			throw new RuntimeException("Erro ao realizar a operação", e.getCause());
		}

	}

	public List<Category> retriveExpensesCategories() throws Exception {
		return this.getUserExpenses().stream().map(RetrieveUserExpensesDto::category).distinct()
				.collect(Collectors.toList());
	}
}

package com.lab.expenseManager.expense.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lab.expenseManager.expense.domain.Category;
import com.lab.expenseManager.expense.dto.CategoryDto;
import com.lab.expenseManager.expense.dto.RetrieveUserExpensesDto;
import com.lab.expenseManager.expense.repository.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final ExpenseService expenseService;

	public CategoryService(CategoryRepository categoryRepository, ExpenseService expenseService) {
		this.categoryRepository = categoryRepository;
		this.expenseService = expenseService;
	}

	public List<Category> getAll() {
		return categoryRepository.findAll();
	}

	public List<Category> retriveExpensesCategories() throws Exception {
		
		List<RetrieveUserExpensesDto> expenses = expenseService.getUserExpenses();
		
		return expenses.stream().map(RetrieveUserExpensesDto::category).distinct().collect(Collectors.toList());
	}

	public List<Category> findAll(String email) throws Exception {
		return categoryRepository.findAll();
	}

	public void create(CategoryDto categoryDto) { 
		try {
			categoryRepository.save(Category.builder().name(categoryDto.name()).build());
		} catch (Exception e) {
			throw new RuntimeException("Erro ao executar a operação", e.getCause());
		}
	}

	public void deleteById(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("Categoria não encontrada.");
		}
	}

	public void update(Long id, CategoryDto categoryDto) {
		try {
			categoryRepository.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao realizar a operação", e.getCause());
		}

	}
}

package com.lab.expenseManager.expense.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lab.expenseManager.expense.domain.Expense;
import com.lab.expenseManager.expense.dto.ExpenseDto;
import com.lab.expenseManager.expense.dto.RetrieveUserExpensesDto;
import com.lab.expenseManager.expense.repository.ExpenseRepository;
import com.lab.expenseManager.user.dataAcess.UserDetailsImpl;
import com.lab.expenseManager.user.service.UserService;

@Service
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserService userService;

	public ExpenseService(ExpenseRepository expenseRepository, UserService userService) {
		this.expenseRepository = expenseRepository;
		this.userService = userService;
	}

	public List<Expense> findAll() {
		return expenseRepository.findAll();
	}

	public List<RetrieveUserExpensesDto> getUserExpenses() throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal(); 

		return expenseRepository.findByUserId(userService.retrieveUserId(userDetails.getEmail()));
	}

	public Page<Expense> getPageable(Pageable pageable) {
		return null;
	}

	public void create(ExpenseDto expenseDto) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			
			expenseRepository.save(Expense.builder().name(expenseDto.name()).description(expenseDto.description())
					.category(expenseDto.category()).amount(expenseDto.amount()).currency(expenseDto.currency())
					.isRecurring(expenseDto.isRecurring()).attachments(expenseDto.attachments())
					.priority(expenseDto.priority()).date(expenseDto.date()).user(userService.getUser(userDetails.getEmail())).build());
		} catch (Exception e) {
			throw new RuntimeException("Erro ao executar a operação", e.getCause());
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

package com.lab.expenseManager.configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lab.expenseManager.expense.domain.Expense;
import com.lab.expenseManager.expense.enums.Status;
import com.lab.expenseManager.expense.repository.ExpenseRepository;

@Component
public class SchedulingExpenses {

	private static final Logger log = LoggerFactory.getLogger(SchedulingExpenses.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private final ExpenseRepository expenseRepository;

	public SchedulingExpenses(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;}
	
	// Lógica para validar a data de uma despesa e atualizar seu status
	@Scheduled(cron = "0 0 0 * * ?") // Executa à meia-noite todos os dias
    public void updateExpenseStatus() {
        log.info("Iniciando a verificação de status de despesas às {}", dateFormat.format(new Date()));

        List<Expense> expenses = expenseRepository.findAll();

        for (Expense expense : expenses) {
            Status newStatus = determineExpenseStatus(expense.getDate(), expense.getIsPaid());

            if (!expense.getStatus().equals(newStatus)) {
                log.info("Atualizando status da despesa ID {} de {} para {}", expense.getId(), expense.getStatus(), newStatus);
                expense.setStatus(newStatus);
                expenseRepository.save(expense);
            }
        }

        log.info("Finalizada a verificação de status de despesas às {}", dateFormat.format(new Date()));
    }

	// Determina o status da despesa através da sua data e se a despesa já foi paga ou não
	public Status determineExpenseStatus(Date expenseDate, boolean isPaid) {
		Date today =  new Date();

		if (expenseDate.after(today)) {
			return Status.PENDING;
		} else if ((expenseDate.equals(today) || expenseDate.before(today)) && !isPaid) {
			return Status.IN_PROGRESS;
		} else if (expenseDate.before(today) && isPaid) {
			return Status.COMPLETED;
		}
		throw new IllegalStateException("Dados inválidos para despesa.");
	}
}
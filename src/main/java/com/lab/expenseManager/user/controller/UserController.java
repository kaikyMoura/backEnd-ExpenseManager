package com.lab.expenseManager.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab.expenseManager.model.ResponseModel;
import com.lab.expenseManager.model.ResponseWithDataModel;
import com.lab.expenseManager.user.domain.User;
import com.lab.expenseManager.user.dto.CreateUserDto;
import com.lab.expenseManager.user.dto.LoginUserDto;
import com.lab.expenseManager.user.dto.RecoveryJwtTokenDto;
import com.lab.expenseManager.user.dto.RecoveryUserDto;
import com.lab.expenseManager.user.service.GCloudStorageService;
import com.lab.expenseManager.user.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/users")
public class UserController {

	private final GCloudStorageService storageService;

	private final UserService userService;

	public UserController(UserService userService, GCloudStorageService storageService) {
		this.storageService = storageService;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseWithDataModel> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
		try {
			RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
			return new ResponseEntity<>(new ResponseWithDataModel(200, "Operação realizada com sucesso.", token),
					HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<ResponseModel> createUser(@RequestBody CreateUserDto user) {
		try {
			userService.create(user);
			return new ResponseEntity<>(new ResponseModel(201, "Operação realizada com sucesso."), HttpStatus.CREATED);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ResponseModel(500, "Erro interno no servidor."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/resend-email")
	public ResponseEntity<ResponseModel> resendVerifyAccountEmail(@RequestParam("email") String email) {
		userService.resendVerifyAccountEmail(email);
		return new ResponseEntity<>(new ResponseModel(200, "Sua conta foi verifica com sucesso."), HttpStatus.ACCEPTED);

	}

	@PostMapping("/verify-account")
	public ResponseEntity<ResponseWithDataModel> verifyAccount(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(new ResponseWithDataModel(200, "Sua conta foi verifica com sucesso.",
				userService.activateAccount(jwtToken)), HttpStatus.OK);

	}

	@GetMapping("/validate-token")
	public ResponseEntity<ResponseWithDataModel> genetaNewToken(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(
				new ResponseWithDataModel(200, "Token renovado com sucesso.", userService.generateNewToken(jwtToken)),
				HttpStatus.OK);

	}

	@GetMapping("/auth/user")
	public ResponseEntity<ResponseWithDataModel> getUser() {
		try {
			User user = userService.getUser();

			byte[] imageData = null;
			if (user.getUserImage() != null) {
				imageData = storageService.getFile(user.getUserImage());
			}

			return new ResponseEntity<>(
					new ResponseWithDataModel(200, "Operação realizada com sucesso.",
							new RecoveryUserDto(user.getName(), user.getLastName(), user.getEmail(), imageData)),
					HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Apenas usuários com o role "ADMINISTRATOR" poderão acessar esses endpoints
	@GetMapping("/administrator")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<String> getAdminAuthenticationTest() {
		return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
	}

	@DeleteMapping("/administrator/delete/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<String> deleteUser(@PathVariable String id) throws Exception {
		try {
			UUID uuid = UUID.fromString(id);
			userService.deleteById(uuid);
			return new ResponseEntity<>("Usuário deletado com sucesso!", HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(value = "/administrator/list")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<List<User>> findAll() {
		return ResponseEntity.ok().body(userService.getAll());
	}
}

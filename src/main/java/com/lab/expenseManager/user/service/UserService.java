package com.lab.expenseManager.user.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lab.expenseManager.security.JwtTokenService;
import com.lab.expenseManager.security.SecurityConfig;
import com.lab.expenseManager.user.dataAcess.UserDetailsImpl;
import com.lab.expenseManager.user.domain.Role;
import com.lab.expenseManager.user.domain.User;
import com.lab.expenseManager.user.dto.CreateUserDto;
import com.lab.expenseManager.user.dto.LoginUserDto;
import com.lab.expenseManager.user.dto.RecoveryJwtTokenDto;
import com.lab.expenseManager.user.dto.UpdateUserDto;
import com.lab.expenseManager.user.enums.RoleName;
import com.lab.expenseManager.user.enums.Status;
import com.lab.expenseManager.user.repository.IRoleRepository;
import com.lab.expenseManager.user.repository.IUserRepository;

@Service
public class UserService {

	private final IUserRepository userRepository;

	private final IRoleRepository roleRepository;

	private final UserDetailsServiceImpl userServiceImpl;

	private final JwtTokenService jwtTokenService;

	private final SecurityConfig securityConfig;

	private final GCloudStorageService storageService;

	private final EmailService emailService;

	public UserService(IUserRepository userRepositoy, IRoleRepository roleRepository, JwtTokenService jwtTokenService,
			SecurityConfig securityConfig, UserDetailsServiceImpl userServiceImpl, EmailService emailService,
			GCloudStorageService storageService) {
		this.userRepository = userRepositoy;
		this.roleRepository = roleRepository;
		this.userServiceImpl = userServiceImpl;
		this.jwtTokenService = jwtTokenService;
		this.securityConfig = securityConfig;
		this.storageService = storageService;
		this.emailService = emailService;
	}

	public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = userRepository.findByEmail(loginUserDto.email())
				.orElseThrow(() -> new BadCredentialsException("As credenciais fornecidas são inválidas."));

		// Checa se a senha está correta
		if (!encoder.matches(loginUserDto.password(), user.getPassword())) {
			throw new BadCredentialsException("As credenciais fornecidas são inválidas.");
		}

		if (user.getStatus() == Status.INACTIVE) {
			throw new BadCredentialsException("Sua conta ainda não foi ativada, por favor cheque seu email");
		}

		// Gera um token JWT para o usuário autenticado
		UserDetailsImpl userDetails = new UserDetailsImpl(user);

		String token = jwtTokenService.generateToken(userDetails);
		return new RecoveryJwtTokenDto(token);
	}

	public RecoveryJwtTokenDto generateNewToken(String token) {
		String email = jwtTokenService.getSubjectFromToken(token);
		if (jwtTokenService.isTokenNearExpiration(token)) {

			UserDetailsImpl user = userServiceImpl.loadUserByUsername(email);

			String newToken = jwtTokenService.generateToken(user);
			return new RecoveryJwtTokenDto(newToken);
		} else {
			throw new JWTVerificationException("Token ainda válido, nenhuma renovação necessária.");
		}
	}

	public RecoveryJwtTokenDto activateAccount(String token) {
		String email = jwtTokenService.getSubjectFromToken(token);
		UserDetailsImpl user = userServiceImpl.loadUserByUsername(email);

		User u = user.getUser();

		u.setStatus(Status.ACTIVE);

		this.update(u.getId(),
				new UpdateUserDto(u.getName(), u.getLastName(), u.getEmail(), u.getUserImage(), u.getStatus()));

		String newToken = jwtTokenService.generateToken(user);
		return new RecoveryJwtTokenDto(newToken);
	}

	public void resendVerifyAccountEmail(String email) throws Exception {
		UserDetailsImpl user = userServiceImpl.loadUserByUsername(email);
		String token = jwtTokenService.generateToken(user);
		emailService.sendVerifyAccountEmail(email, token);
	}

	public void sendResetPasswordEmail(String email) throws IOException, GeneralSecurityException {
		UserDetailsImpl user = userServiceImpl.loadUserByUsername(email);
		String token = jwtTokenService.generateToken(user);
		emailService.sendResetPasswordEmail(email, token);
	}

	public void create(CreateUserDto createUserDto) throws Exception {
		Optional<User> userOpt = userRepository.findByEmail(createUserDto.email());
		if (userOpt.isPresent()) {
			throw new BadCredentialsException("Já existe um usuário com esse email.");
		}
		// Se no corpo da requisição não for passado um role especifico, como padrão ele
		// será setado como "CUSTOMER"
		String requestRole = createUserDto.role() == null ? "ROLE_CUSTOMER" : createUserDto.role();

		Role role = roleRepository.findByName(RoleName.valueOf(requestRole));

		User user = userRepository.save(User.builder().id(UUID.randomUUID()).name(createUserDto.name())
				.lastName(createUserDto.lastName()).email(createUserDto.email())
				.password(securityConfig.passwordEncoder().encode(createUserDto.password()))
				.role(Role.builder().name(RoleName.valueOf(requestRole)).id(role.getId()).build())
				.status(Status.INACTIVE).userImage(storageService.uploadFile(createUserDto.profileImage())).build());

		String token = jwtTokenService.generateToken(new UserDetailsImpl(user));
		emailService.sendVerifyAccountEmail(createUserDto.email(), token);
	}

	public void deleteById(UUID id) throws Exception {
		try {
			userRepository.deleteById(id);
		} catch (Exception exception) {
			throw new Exception("Erro ao executar a operação!");
		}
	}

	public void updatePassword(String token, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String email = jwtTokenService.getSubjectFromToken(token);
		User existingUser = userServiceImpl.loadUserByUsername(email).getUser();

		if (encoder.matches(existingUser.getPassword(), password)) {
			throw new BadCredentialsException("A nova senha deve ser diferente das 5 ultimas.");
		}
		existingUser.setPassword(securityConfig.passwordEncoder().encode(password));

		userRepository.save(existingUser);
	}

	public void update(UUID uuid, UpdateUserDto updateUserDto) {
		try {
			User existingUser = userServiceImpl.loadUserByUsername(updateUserDto.email()).getUser();

			existingUser.setName(updateUserDto.name());
			existingUser.setLastName(updateUserDto.lastName());
			existingUser.setEmail(updateUserDto.email());
			existingUser.setUserImage(updateUserDto.userImage());
			existingUser.setStatus(updateUserDto.status());

			userRepository.save(existingUser);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao realizar a operação", e.getCause());
		}

	}

	public UUID retrieveUserId(String email) throws Exception {
		return userRepository.findByEmail(email).get().getId();
	}

	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return userRepository.findByEmail(userDetails.getEmail()).get();
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

}
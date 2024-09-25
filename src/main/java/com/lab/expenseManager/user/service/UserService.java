package com.lab.expenseManager.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.lab.expenseManager.user.repository.IRoleRepository;
import com.lab.expenseManager.user.repository.IUserRepository;

@Service
public class UserService {

	private final IUserRepository userRepository;

	private final IRoleRepository roleRepository;

	private final UserDetailsServiceImpl userServiceImpl;

	private final JwtTokenService jwtTokenService;

	private final SecurityConfig securityConfig;

	//private final EmailService emailService;

	public UserService(IUserRepository userRepositoy, IRoleRepository roleRepository, JwtTokenService jwtTokenService,
			SecurityConfig securityConfig, UserDetailsServiceImpl userServiceImpl) {
		this.userRepository = userRepositoy;
		this.roleRepository = roleRepository;
		this.userServiceImpl = userServiceImpl;
		this.jwtTokenService = jwtTokenService;
		this.securityConfig = securityConfig;
	}

	public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			User user = userRepository.findByEmail(loginUserDto.email()).orElseThrow(
					() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + loginUserDto.email()));

			// Checa se a senha está correta
			if (!encoder.matches(loginUserDto.password(), user.getPassword())) {
				throw new BadCredentialsException("Credenciais inválidas");
			}

			// Gera um token JWT para o usuário autenticado
			UserDetailsImpl userDetails = new UserDetailsImpl(user);

			String token = jwtTokenService.generateToken(userDetails);
			return new RecoveryJwtTokenDto(token);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new RuntimeException("Erro ao autenticar o usuário", exception);
		}
	}

	public RecoveryJwtTokenDto generateNewToken(String token) {
		try {
			String email = jwtTokenService.getSubjectFromToken(token);
			if (jwtTokenService.isTokenNearExpiration(token)) {

				UserDetailsImpl user = userServiceImpl.loadUserByUsername(email);

				String newToken = jwtTokenService.generateToken(user);
				return new RecoveryJwtTokenDto(newToken);
			} else {
				throw new JWTVerificationException("Token ainda válido, nenhuma renovação necessária.");
			}
		} catch (JWTVerificationException ex) {
			throw new JWTVerificationException("Token inválido ou expirado.");
		}
	}

	public void create(CreateUserDto createUserDto) {
		try {
			// Se no corpo da requisição não for passado um role especifico, como padrão ele
			// será setado como "CUSTOMER"
			String requestRole = createUserDto.role() == null ? "ROLE_CUSTOMER" : createUserDto.role();

			Role role = roleRepository.findByName(RoleName.valueOf(requestRole));

			// Por questão de modelagem eu preferi gerar um UUID aleatorio
			userRepository.save(User.builder().id(UUID.randomUUID()).name(createUserDto.name())
					.lastName(createUserDto.lastName()).email(createUserDto.email())
					.password(securityConfig.passwordEncoder().encode(createUserDto.password()))
					.role(Role.builder().name(RoleName.valueOf(requestRole)).id(role.getId()).build()).build());

			//emailService.verifyAccountEmail(createUserDto.email());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar a operação", e.getCause());
		}
	}

	public void deleteById(UUID id) throws Exception {
		try {
			userRepository.deleteById(id);
		} catch (Exception exception) {
			throw new Exception("Erro ao executar a operação!");
		}
	}

	public void update(UUID uuid, UpdateUserDto updateUserDto) {
		try {
			userServiceImpl.loadUserByUsername(updateUserDto.email());

			userRepository.save(User.builder().name(updateUserDto.name()).lastName(updateUserDto.lastName())
					.email(updateUserDto.email()).userImage(updateUserDto.userImage()).build());

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
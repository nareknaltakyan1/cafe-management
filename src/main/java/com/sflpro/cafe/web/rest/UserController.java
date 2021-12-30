package com.sflpro.cafe.web.rest;

import com.sflpro.cafe.dto.UserDTO;
import com.sflpro.cafe.dto.payload.RegistrationDTO;
import com.sflpro.cafe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class UserController
{

	private final UserService userService;

	public UserController(UserService userService)
	{
		this.userService = userService;
	}

	@PostMapping(value = "/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO)
	{

		UserDTO userDTO = userService.userRegistration(registrationDTO);
		return ResponseEntity.ok(userDTO);
	}

	@GetMapping
	public ResponseEntity<?> getAllUsers()
	{

		return ResponseEntity.ok(userService.getAllUsers());
	}
}

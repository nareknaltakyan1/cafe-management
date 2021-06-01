package com.sflpro.cafe.service;

import com.sflpro.cafe.dto.UserDTO;
import com.sflpro.cafe.entity.User;
import com.sflpro.cafe.exception.UsernameAlreadyExistException;
import com.sflpro.cafe.repository.UserRepository;
import com.sflpro.cafe.dto.payload.RegistrationDTO;
import com.sflpro.cafe.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO userRegistration(RegistrationDTO registrationDTO) {

        Optional<User> existingUserOpt = userRepository.findByUsernameIgnoreCase(registrationDTO.getUsername());

        if (existingUserOpt.isPresent()) {
            log.error("Username already exists");
            throw new UsernameAlreadyExistException();
        }

        User user = userRepository.save(constructNewUserEntity(registrationDTO));

        return userEntityToDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);

        if (!userOpt.isPresent()) {
            final String errorMsg = "User with such username doesn't exist";
            log.error(errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }

        return constructUserPrincipal(userOpt.get());
    }

    public List<UserDTO> getAllUsers() {

        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(UserService::userEntityToDto)
                .collect(Collectors.toList());
    }

    public static UserDTO userEntityToDto(User user) {

        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        userDTO.setEnabled(user.isEnabled());

        return userDTO;
    }


    private User constructNewUserEntity(RegistrationDTO payload) {

        User user = new User();

        user.setUsername(payload.getUsername());
        user.setRole(payload.getRole());

        user.setEnabled(true);

        String encodedPassword = passwordEncoder.encode(payload.getPassword());

        user.setPassword(encodedPassword);

        return user;
    }

    private UserPrincipal constructUserPrincipal(User user) {

        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .encodedPassword(user.getPassword())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }
}

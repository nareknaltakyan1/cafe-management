package com.sflpro.cafe.repository;

import com.sflpro.cafe.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>
{

	Optional<User> findByUsernameIgnoreCase(String username);
}

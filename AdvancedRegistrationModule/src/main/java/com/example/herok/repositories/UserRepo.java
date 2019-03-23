package com.example.herok.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.herok.enities.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
	 Optional<User> findByResetToken(String resetToken);
	 Optional<User> findByEmail(String email);
	 Optional<User> findByUsername(String username);

}

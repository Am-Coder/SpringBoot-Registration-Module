package com.example.secure.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.secure.enities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	User findByUserName(String userName);
}

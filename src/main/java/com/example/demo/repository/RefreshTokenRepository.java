package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    RefreshToken findByUserId(Long userId);

}
package com.rouvsen.demo.dao;

import com.rouvsen.demo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardDAO extends JpaRepository<Card, Integer> {

}

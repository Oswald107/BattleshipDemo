package com.battleship.demo.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.battleship.demo.store.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    List<User> findAllByOrderByWinsDesc();
    User findByUsername(String name);
}

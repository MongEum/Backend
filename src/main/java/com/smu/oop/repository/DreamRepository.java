package com.smu.oop.repository;

import com.smu.oop.entity.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Long> {

    List<Dream> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Dream> findByUserId(Long userId);
}

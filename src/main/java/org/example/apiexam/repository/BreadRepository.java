package org.example.apiexam.repository;

import org.example.apiexam.model.Bread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreadRepository extends JpaRepository<Bread,Long> {
}

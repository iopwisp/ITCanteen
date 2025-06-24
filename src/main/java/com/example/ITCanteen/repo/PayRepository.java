package com.example.ITCanteen.repo;


import com.example.ITCanteen.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRepository extends JpaRepository<Card , Long> {
    Optional<Card> findStudentById(Long studentId);
}

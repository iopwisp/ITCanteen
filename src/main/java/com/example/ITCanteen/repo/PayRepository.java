package com.example.ITCanteen.repo;


import com.example.ITCanteen.model.Card;
import com.example.ITCanteen.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRepository extends JpaRepository<Card , Long> {
    Optional<Card> findByStudentId(Long studentId);
    Optional<Card> findCardById(Long cardId);
}

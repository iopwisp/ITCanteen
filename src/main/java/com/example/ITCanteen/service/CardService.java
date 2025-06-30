package com.example.ITCanteen.service;

import com.example.ITCanteen.model.Card;
import com.example.ITCanteen.model.Student;
import com.example.ITCanteen.repo.PayRepository;
import com.example.ITCanteen.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final PayRepository payRepository;
    private final StudentRepository studentRepository;

    public Card createCardForStudent(Long studentId, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Ученик не найден с ID: " + studentId));
        Card card = new Card();
        card.setStudent(student);
        card.setBalance(initialBalance);
        card.setCreatedDate(LocalDateTime.now());
        return payRepository.save(card);
    }

    public Optional<Card> findByStudentId(Long studentId) {
        return payRepository.findByStudentId(studentId);
    }

    public Card rechargeCard(Long cardId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть больше 0");
        }
        Card card = payRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена с ID: " + cardId));
        card.setBalance(card.getBalance() + amount);
        return payRepository.save(card);
    }

    public void deleteCard(Long cardId) {
        if (!payRepository.existsById(cardId)) {
            throw new IllegalArgumentException("Карта не найдена с ID: " + cardId);
        }
        payRepository.deleteById(cardId);
    }
}
package com.example.ITCanteen.service;

import com.example.ITCanteen.model.Card;
import com.example.ITCanteen.model.Payment;
import com.example.ITCanteen.model.PaymentStatus;
import com.example.ITCanteen.repo.PayRepository;
import com.example.ITCanteen.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final PaymentRepository paymentRepository;

    public Optional<Card> findCardById(Long id) {
        return payRepository.findById(id);
    }

    public Payment createPayment(@Valid Payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    public Optional<Card> findPaymentsByCardId(Long cardId) {
        return payRepository.findCardById(cardId);
    }
}
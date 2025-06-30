package com.example.ITCanteen.controller;

import com.example.ITCanteen.model.Card;
import com.example.ITCanteen.model.Payment;
import com.example.ITCanteen.service.PayService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @GetMapping("/card/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(payService.findCardById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Карта не найдена с ID: " + id)));
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        return ResponseEntity.ok(payService.createPayment(payment));
    }

    @GetMapping("/card/{cardId}/history")
    public ResponseEntity<Optional<Card>> getPaymentHistory(@PathVariable Long cardId) {
        return ResponseEntity.ok(payService.findPaymentsByCardId(cardId));
    }
}
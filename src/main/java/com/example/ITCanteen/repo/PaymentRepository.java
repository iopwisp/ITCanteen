package com.example.ITCanteen.repo;

import com.example.ITCanteen.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment , Long> {
}

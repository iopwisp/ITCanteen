package com.example.ITCanteen.repo;

import com.example.ITCanteen.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order , Long> {
    List<Order> findByStudentId(Long studentId);
}

package com.example.ITCanteen.service;

import com.example.ITCanteen.dto.OrderRequest;
import com.example.ITCanteen.model.Card;
import com.example.ITCanteen.model.Food;
import com.example.ITCanteen.model.Order;
import com.example.ITCanteen.model.Student;
import com.example.ITCanteen.repo.FoodRepository;
import com.example.ITCanteen.repo.OrderRepository;
import com.example.ITCanteen.repo.PayRepository;
import com.example.ITCanteen.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final FoodRepository foodRepository;
    private final PayRepository payRepository;

    public Order createOrder(OrderRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Ученик не найден"));

        List<Food> foods = foodRepository.findAllById(request.getFoodId());

        int totalPrice = foods.stream()
                .mapToInt(Food::getPrice)
                .sum();

        Card card = payRepository.findStudentById(student.getId())
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));
        if (card.getBalance() < totalPrice) throw new RuntimeException("Недостаточно средств");
        card.setBalance(card.getBalance() - totalPrice);
        payRepository.save(card);

        Order order = new Order();
        order.setStudent(student);
        order.setFoods(foods);
        order.setOrderTime(String.valueOf(LocalDateTime.now()));

        return orderRepository.save(order);
    }

    public List<Order> getOrderByStudentId(Long studentId){
        return orderRepository.findByStudentId(studentId);
    }
}

package com.example.ITCanteen.service;

import com.example.ITCanteen.dto.OrderRequest;
import com.example.ITCanteen.model.*;
import com.example.ITCanteen.repo.FoodRepository;
import com.example.ITCanteen.repo.OrderRepository;
import com.example.ITCanteen.repo.PayRepository;
import com.example.ITCanteen.repo.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final FoodRepository foodRepository;
    private final PayRepository payRepository;

    @Transactional
    public Order createOrder(@Valid OrderRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден с ID: " + request.getStudentId()));

        List<Long> foodIds = request.getItems().stream().map(OrderRequest.OrderItemRequest::getFoodId).collect(Collectors.toList());
        List<Food> foods = foodRepository.findAllById(foodIds);
        if (foods.isEmpty()) {
            throw new IllegalArgumentException("Не выбрано ни одно блюдо");
        }

        double totalPrice = request.getItems().stream()
                .mapToDouble(item -> {
                    Food food = foods.stream().filter(f -> f.getId().equals(item.getFoodId())).findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено с ID: " + item.getFoodId()));
                    return food.getPrice() * item.getQuantity();
                })
                .sum();

        Card card = payRepository.findByStudentId(student.getId())
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена для студента"));
        if (card.getBalance() < totalPrice) {
            throw new IllegalArgumentException("Недостаточно средств на карте");
        }

        Order order = new Order();
        order.setStudent(student);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.DRAFT);

        List<OrderItem> orderItems = request.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(foods.stream().filter(f -> f.getId().equals(item.getFoodId())).findFirst().orElseThrow());
            orderItem.setQuantity(item.getQuantity());
            return orderItem;
        }).toList();

        order.setItems(orderItems.toString());
        orderRepository.save(order);

        card.setBalance(card.getBalance() - totalPrice);
        payRepository.save(card);

        return order;
    }

    public List<Order> getOrderByStudentId(Long studentId) {
        return orderRepository.findByStudentId(studentId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден с ID: " + orderId));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
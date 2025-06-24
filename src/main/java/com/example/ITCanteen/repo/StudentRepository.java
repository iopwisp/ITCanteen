package com.example.ITCanteen.repo;

import com.example.ITCanteen.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student , Long> {
    Student findByFullname(String fullname);
}

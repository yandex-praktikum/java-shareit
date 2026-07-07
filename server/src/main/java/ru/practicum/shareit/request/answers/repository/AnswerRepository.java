package ru.practicum.shareit.request.answers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.answers.model.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByRequestId(Long requestItemId);

}

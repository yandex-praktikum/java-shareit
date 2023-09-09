package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> searchAllByRequestor_IdOrderByCreatedDesc(Long requestorId);

    List<Request> searchAllByRequestor_IdNot(Long requestorId, Pageable pageable);
}

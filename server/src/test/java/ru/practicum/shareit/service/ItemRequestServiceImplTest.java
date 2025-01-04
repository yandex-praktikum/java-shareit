package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.enums.ItemStatus;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl requestService;
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    User requestor;
    ItemRequest itemRequest;
    User responseOwner;
    Item response;

    @BeforeEach
    public void setUp() {
        requestor = new User(null, "Requestor-1", "Requestor-1::/description");
        responseOwner = new User(null, "ResponseOwner-1", "ResponseOwner-1::/description");
        itemRequest = new ItemRequest(null,
                "ItemRequest-1::/description", requestor, LocalDateTime.now().minusDays(1));
        response = new Item(null,
                "Item-1", "Item-1::/description", ItemStatus.AVAILABLE, responseOwner, itemRequest);
    }

    @Test
    public void findAllRequestsOfUser_shouldReturnRequests() {
        requestor = userRepository.save(requestor);
        responseOwner = userRepository.save(responseOwner);
        itemRequest = requestRepository.save(itemRequest);
        response = itemRepository.save(response);

        Collection<ItemRequestDto> allRequestsOfUser = requestService.findAllRequestsOfUser(requestor.getId());
        assertThat(allRequestsOfUser).isNotNull();
        assertThat(allRequestsOfUser).isNotEmpty();
        assertThat(allRequestsOfUser.size()).isEqualTo(1);
        assertThat(allRequestsOfUser.iterator().next().getResponses()
                .stream().anyMatch(i -> i.getId().equals(response.getId()))).isTrue();
    }
}

package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create_shouldSaveRequestForUser() {
        User user = new User();
        user.setName("Denis");
        user.setEmail("denis@test.ru");

        User savedUser = userRepository.save(user);

        ItemRequestCreateDto requestDto = new ItemRequestCreateDto();
        requestDto.setDescription("Нужна дрель");

        ItemRequestDto result =
                itemRequestService.create(savedUser.getId(), requestDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Нужна дрель");
        assertThat(result.getCreated()).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void getUserRequests_shouldReturnRequestsOfUser() {
        User user = new User();
        user.setName("Denis");
        user.setEmail("denis" + System.nanoTime() + "@test.ru");

        User savedUser = userRepository.save(user);

        ItemRequestCreateDto first = new ItemRequestCreateDto();
        first.setDescription("Нужна дрель");

        ItemRequestCreateDto second = new ItemRequestCreateDto();
        second.setDescription("Нужен шуруповёрт");

        itemRequestService.create(savedUser.getId(), first);
        itemRequestService.create(savedUser.getId(), second);

        List<ItemRequestDto> result =
                itemRequestService.getUserRequests(savedUser.getId());

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(ItemRequestDto::getDescription)
                .containsExactlyInAnyOrder(
                        "Нужна дрель",
                        "Нужен шуруповёрт"
                );
    }

    @Test
    void getAllRequests_shouldReturnRequestsOfOtherUsers() {
        User requestOwner = new User();
        requestOwner.setName("Denis");
        requestOwner.setEmail("denis" + System.nanoTime() + "@test.ru");

        requestOwner = userRepository.save(requestOwner);

        ItemRequestCreateDto requestDto = new ItemRequestCreateDto();
        requestDto.setDescription("Нужна дрель");

        itemRequestService.create(requestOwner.getId(), requestDto);

        User anotherUser = new User();
        anotherUser.setName("Ivan");
        anotherUser.setEmail("ivan" + System.nanoTime() + "@test.ru");

        anotherUser = userRepository.save(anotherUser);

        List<ItemRequestDto> result =
                itemRequestService.getAllRequests(anotherUser.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription())
                .isEqualTo("Нужна дрель");
    }

    @Test
    void getRequestById_shouldReturnRequest() {
        User user = new User();
        user.setName("Denis");
        user.setEmail("denis" + System.nanoTime() + "@test.ru");

        User savedUser = userRepository.save(user);

        ItemRequestCreateDto requestDto = new ItemRequestCreateDto();
        requestDto.setDescription("Нужна дрель");

        ItemRequestDto created =
                itemRequestService.create(savedUser.getId(), requestDto);

        ItemRequestDto result =
                itemRequestService.getRequestById(created.getId());

        assertThat(result.getId()).isEqualTo(created.getId());
        assertThat(result.getDescription())
                .isEqualTo("Нужна дрель");
        assertThat(result.getCreated()).isNotNull();
    }
}
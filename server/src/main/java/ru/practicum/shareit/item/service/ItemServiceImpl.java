package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.exceptions.IncorrectItemException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;
    private UserServiceImpl userService;
    private BookingMapper bookingMapper;

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank() || itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new IncorrectItemException("Имя или описание заполнены неправильно");
        }

        if (itemDto.getAvailable() == null) {
            throw new IncorrectItemException("Не заполнено поле доступности вещи");
        }
        Item item = ItemMapper.toItem(itemDto);
        userService.getUserById(userId);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        Optional<Item> itemData = itemRepository.findById(itemId);

        if (itemData.isPresent()) {
            Item itemUpd = itemData.get();

            if (itemUpd.getOwnerId() != userId) {
                throw new NotOwnerException("Редактирование вещи доступно только владельцу");
            }

            if (item.getName() != null) {
                itemUpd.setName(item.getName());
            }

            if (item.getDescription() != null) {
                itemUpd.setDescription(item.getDescription());
            }

            if (item.isAvailable() != null) {
                itemUpd.setAvailable(item.isAvailable());
            }
            itemRepository.save(itemUpd);
            return ItemMapper.toItemDto(itemUpd);
        } else {
            throw new ItemNotFoundException("Вещь не найдена");
        }
    }

    @Override
    public ItemBookingDto getItemById(long itemId, long userId) {
        Optional<Item> itemData = itemRepository.findById(itemId);

        if (itemData.isPresent()) {
            return getItemBookingDtoWithBookingsAndComments(itemData.get(), userId);
        } else {
            throw new ItemNotFoundException("Вещь не найдена");
        }
    }

    @Override
    public void delete(long userId, long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemBookingDto> getAllUsersItems(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("id"));
        return itemRepository.getAllByOwnerId(userId, pageable)
                .stream()
                .map(i -> getItemBookingDtoWithBookingsAndComments(i, userId))
                .sorted(Comparator.comparing(ItemBookingDto::getId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ItemDto> search(String text, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("id"));//Поиск вещи по наличию текста в имени или описании
        return itemRepository.search(text, pageable).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private ItemBookingDto getItemBookingDtoWithBookingsAndComments(Item item, long userId) {
        ItemBookingDto getDto = ItemMapper.toItemBookingDto(item);
        List<Booking> bookings = bookingRepository.getAllByItemIdOrderByStartDesc(item.getId());
        if (bookings != null && bookings.size() > 0) {
            if (item.getOwnerId() == userId) {
                getDto.setLastBooking(bookingMapper.toBookingDto(bookings.get(bookings.size() - 1)));
                getDto.setNextBooking(bookingMapper.toBookingDto(bookings.get(0)));
            }
        }

        getDto.setComments(commentRepository
                .getAllByItemId(item.getId())
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new)));

        return getDto;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long itemId, long userId) {
        Comment comment = CommentMapper.toComment(commentDto);
        Optional<Booking> booking = bookingRepository.findByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now());
        if (booking.isEmpty() || booking.get().getStatus() == BookingStatus.REJECTED) {
            throw new NotAvailableException("Вы не бронировали эту вещь, либо срок аренды ещё не вышел");
        }

        comment.setText(commentDto.getText());
        if (itemRepository.findById(itemId).isPresent()) {
            comment.setItem(itemRepository.findById(itemId).get());
        } else {
            throw new ItemNotFoundException("вещь не найдена");
        }
        comment.setAuthor(userService.getUserById(userId));
        return CommentMapper.toDto(commentRepository.save(comment));
    }
}


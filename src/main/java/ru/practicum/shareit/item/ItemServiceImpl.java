package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingItemOutputDto;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.mopel.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public ItemOutputDto getItem(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                                  .orElseThrow(() -> {
                                      log.error("Item with id {} not found", itemId);
                                      return new NotFoundException("Item not found with id: " + itemId);
                                  });

        return getItemOutputDto(item, ownerId);
    }

    @Override
    public List<ItemOutputDto> getOwnItems(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId);

        return items.stream()
                    .map(i -> getItemOutputDto(i, ownerId))
                    .collect(Collectors.toList());
    }

    @Override
    public ItemInputDto create(Long ownerId, ItemInputDto itemInputDto) {
        UserDto ownerDto = userService.getUser(ownerId);
        User owner = userMapper.toUser(ownerDto);

        Item item = itemMapper.toItem(itemInputDto, owner);

        return itemMapper.toInputDto(itemRepository.save(item));
    }

    @Override
    public ItemInputDto update(Long ownerId, Long itemId, ItemInputDto itemInputDto) {
        Item oldItem = itemRepository.findByIdAndOwnerId(itemId, ownerId)
                                     .orElseThrow(() -> {
                                         log.error("Item with id {} not found or user isn't owner", itemId);
                                         return new NotFoundException(
                                                 "Item with id " + itemId + " not found or user isn't owner"
                                         );
                                     });

        Item newItem = itemMapper.toItem(itemInputDto, null);

        String name = newItem.getName();
        String description = newItem.getDescription();
        Boolean available = newItem.getAvailable();

        if (name != null) {
            oldItem.setName(name);
        }
        if (description != null) {
            oldItem.setDescription(description);
        }
        if (available != null) {
            oldItem.setAvailable(available);
        }

        return itemMapper.toInputDto(itemRepository.save(oldItem));
    }


    @Override
    public List<ItemInputDto> searchItem(String text) {
        if (text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository.searchItem(text);

        return items.stream()
                    .map(itemMapper::toInputDto)
                    .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long bookerId, Long itemId, CommentDto commentDto) {
        Booking booking = bookingRepository.findByBookerIdAndPastState(bookerId)
                                           .orElseThrow(() -> {
                                               log.error("Item {} booked by User {} was not found",
                                                       itemId, bookerId);
                                               return new BadRequestException(
                                                       String.format(
                                                               "Item %s booked by User %s was not found",
                                                               itemId, bookerId)
                                               );
                                           });

        Comment comment = commentMapper.toComment(commentDto, booking.getItem(), booking.getBooker());
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    private ItemOutputDto getItemOutputDto(Item item, Long ownerId) {
        Booking lastBooking = bookingRepository.findLastBooking(item.getId(), ownerId)
                                               .orElse(null);
        Booking nextBooking = bookingRepository.findNextBooking(item.getId(), ownerId)
                                               .orElse(null);

        ItemOutputDto itemOutputDto = itemMapper.toOutputDto(item);

        BookingItemOutputDto lastBookingItemOutputDto;
        BookingItemOutputDto nextBookingItemOutputDto;

        if (lastBooking != null) {
            lastBookingItemOutputDto = new BookingItemOutputDto(lastBooking.getId(),
                    lastBooking.getBooker().getId());
            itemOutputDto.setLastBooking(lastBookingItemOutputDto);
        }
        if (nextBooking != null) {
            nextBookingItemOutputDto = new BookingItemOutputDto(nextBooking.getId(),
                    nextBooking.getBooker()
                               .getId());
            itemOutputDto.setNextBooking(nextBookingItemOutputDto);
        }

        List<Comment> comments = commentRepository.findByItemId(item.getId());
        List<CommentDto> commentsDto = comments.stream()
                                               .map(commentMapper::toCommentDto)
                                               .collect(Collectors.toList());
        itemOutputDto.setComments(commentsDto);

        return itemOutputDto;
    }

}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.validator.BookingCreateValidator;
import ru.practicum.shareit.common.Constants;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    /**
     * Запрос создания нового бронирования
     *
     * @param bookingCreateRequest данные для создания бронирования
     * @param bookerId             id пользователя, который делает запрос
     * @return заполненный объект типа BookingDto
     * @throws ValidationException если данные не прошли валидацию
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@Valid @RequestBody BookingCreateRequest bookingCreateRequest,
                             @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") long bookerId)
            throws ValidationException {
        log.info("Create booking, booker {}: " + bookingCreateRequest.toString(), bookerId);
        if (bookerId <= 0) {
            throw new ValidationException("Указан ошибочный id пользователя");
        }

        BookingCreateValidator.validate(bookingCreateRequest);

        return BookingDtoMapper.toBookingDto(bookingService.create(bookingCreateRequest, bookerId));
    }

    /**
     * Запрос подтверждения бронирования владельцем вещи
     *
     * @param bookingId id бронирования
     * @param approved  вид действия: true - подтвердить, false - отклонить
     * @param ownerId   id пользователя, который делает запрос
     * @return заполненный объект типа BookingDto
     */
    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approve(@PathVariable long bookingId,
                              @RequestParam Boolean approved,
                              @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Approve booking {}, ownerId {}, approved {}", bookingId, ownerId, approved);
        if (ownerId <= 0) {
            throw new ValidationException("Указан ошибочный id владельца");
        }

        return BookingDtoMapper.toBookingDto(bookingService.approve(bookingId, approved, ownerId));
    }

    /**
     * Запросить бронирование с указанным id
     *
     * @param bookingId id бронирования
     * @param userId    id пользователя, который делает запрос
     * @return если запрос делает автор бронирования или владелец вещи - вернётся заполненный объект типа BookingDto, иначе - возникнет исключение
     */
    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto get(@PathVariable long bookingId,
                          @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int userId) {
        log.info("Get booking {}, userId {}", bookingId, userId);
        if (userId <= 0) {
            throw new ValidationException("Указан ошибочный id пользователя");
        }

        return BookingDtoMapper.toBookingDto(bookingService.getById(bookingId, userId));
    }

    /**
     * Запросить бронирования созданные определенным пользователем, с фильтрацией или без
     *
     * @param state    вид фильтрации данных: по умолчанию равен ALL, может принимать значения CURRENT, PAST, FUTURE, WAITING, REJECTED
     * @param bookerId id пользователя, который делает запрос
     * @return список бронирований в виде списка объектов BookingDto
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsByBookerId(@RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "20") int size,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int bookerId) {
        log.info("Get bookings of booker id {}", bookerId);
        if (bookerId <= 0) {
            throw new ValidationException("Указан ошибочный id пользователя");
        }

        return BookingDtoMapper.toBookingDtoList(bookingService.getBookingsByBookerId(bookerId, state, from, size));
    }

    /**
     * Запросить бронирования вещей определенного пользователя, с фильтрацией или без
     *
     * @param state   вид фильтрации данных: по умолчанию равен ALL, может принимать значения CURRENT, PAST, FUTURE, WAITING, REJECTED
     * @param ownerId id пользователя, который делает запрос
     * @return список бронирований в виде списка объектов BookingDto
     */
    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsByOwnerId(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "20") int size,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Get bookings of item of owner id {}", ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("Указан ошибочный id пользователя");
        }

        return BookingDtoMapper.toBookingDtoList(bookingService.getBookingsByOwnerId(ownerId, state, from, size));
    }
}
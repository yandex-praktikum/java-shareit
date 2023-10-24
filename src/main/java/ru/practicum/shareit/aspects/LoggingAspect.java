package ru.practicum.shareit.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.constants.MyConstants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @AfterReturning(pointcut = MyConstants.ADD_USER_POINTCUT, returning = "userDto")
    public void afterReturningAddUserAdvices(UserDto userDto) {
        log.info("Пользователь " + userDto + "добавлен.");
    }

    @AfterThrowing(pointcut = MyConstants.ADD_USER_POINTCUT, throwing = "exception")
    public void afterThrowingAddUserAdvices(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.REMOVE_USER_POINTCUT, returning = "response")
    public void afterReturningRemoveUserByIdAdvices(String response) {
        log.info(response);
    }

    @AfterThrowing(pointcut = MyConstants.REMOVE_USER_POINTCUT, throwing = "exception")
    public void afterThrowingRemoveUserByIdAdvices(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.GET_ALL_USERS_POINTCUT)
    public void afterReturningGetAllUserAdvice() {
        log.info("Список всех пользователей получен.");
    }

    @AfterReturning(pointcut = MyConstants.UPDATE_USER_POINTCUT, returning = "userDto")
    public void afterReturningUpdateUserAdvice(UserDto userDto) {
        log.info("Пользователь с id:" + userDto.getId() + " обновлён.");
    }

    @AfterThrowing(pointcut = MyConstants.UPDATE_USER_POINTCUT, throwing = "exception")
    public void afterThrowingUpdateUserAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.ADD_ITEM_POINTCUT, returning = "itemDto")
    public void afterReturningAddItemAdvice(ItemDto itemDto) {
        log.info("Item: " + itemDto.getName() + " с id:" + itemDto.getId() + " добавлен.");
    }

    @AfterThrowing(pointcut = MyConstants.ADD_ITEM_POINTCUT, throwing = "exception")
    public void afterThrowingAddItemAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.UPDATE_ITEM_POINTCUT, returning = "itemDto")
    public void afterReturningUpdateItemAdvice(ItemDto itemDto) {
        log.info("Item с id:" + itemDto.getId() + " обновлён.");
    }

    @AfterThrowing(pointcut = MyConstants.UPDATE_ITEM_POINTCUT, throwing = "exception")
    public void afterThrowingUpdateItemAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.GET_ITEM_BY_ID_POINTCUT, returning = "itemDto")
    public void afterReturningGetItemByIdAdvice(ItemDto itemDto) {
        log.info("Получен Item: " + itemDto);
    }

    @AfterThrowing(pointcut = MyConstants.GET_ITEM_BY_ID_POINTCUT, throwing = "exception")
    public void afterThrowingGetItemByIdAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.GET_ALL_ITEM_POINTCUT)
    public void afterReturningGetAllItemForOwnerAdvice() {
        log.info("Список всех Item получен.");
    }

    @AfterThrowing(pointcut = MyConstants.GET_ALL_ITEM_POINTCUT, throwing = "exception")
    public void afterThrowingGetAllItemForOwnerAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }

    @AfterReturning(pointcut = MyConstants.SEARCH_ITEM_POINTCUT, returning = "itemDtoList")
    public void afterReturningSearchItemAdvice(List<ItemDto> itemDtoList) {
        log.info("Список Item по запросу получен: " + itemDtoList);
    }

    @AfterThrowing(pointcut = MyConstants.SEARCH_ITEM_POINTCUT, throwing = "exception")
    public void afterThrowingSearchItemAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }
}


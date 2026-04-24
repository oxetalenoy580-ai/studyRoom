package com.study.aspect;

import com.study.annotation.CheckRoomFull;
import com.study.constant.StatuConstant;
import com.study.entity.Room;
import com.study.mapper.UserMapper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RoomFullStatusAspect {

    @Autowired
    private UserMapper userMapper;

    @Before("@annotation(com.study.annotation.CheckRoomFull)")
    public void checkRoomFullStatus(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckRoomFull annotation = method.getAnnotation(CheckRoomFull.class);

        String roomId = getRoomId(joinPoint, annotation.roomIdParam());
        if (roomId == null || roomId.isEmpty()) {
            return;
        }

        Room room = userMapper.getRoomById(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found");
        }

        if (StatuConstant.ROOM_FULL.equals(room.getFullStatus())) {
            throw new RuntimeException("Room is full, cannot make reservation");
        }
    }

    private String getRoomId(JoinPoint joinPoint, String paramName) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            if (arg instanceof String) {
                if (paramNames[i].equals(paramName)) {
                    return (String) arg;
                }
            } else {
                try {
                    Field field = arg.getClass().getDeclaredField("roomId");
                    field.setAccessible(true);
                    Object value = field.get(arg);
                    if (value instanceof String) {
                        return (String) value;
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.debug("Field not found: {}", paramName);
                }
            }
        }
        return null;
    }
}
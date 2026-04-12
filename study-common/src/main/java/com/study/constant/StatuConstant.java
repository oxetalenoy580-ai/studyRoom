package com.study.constant;

public final class StatuConstant {

    private StatuConstant() {
    }

    public static final Integer ROOM_DISABLED = 0;
    public static final Integer ROOM_ENABLED = 1;
    public static final Integer ROOM_NOT_FULL = 0;
    public static final Integer ROOM_FULL = 1;

    public static final Integer SEAT_AVAILABLE = 0;
    public static final Integer SEAT_RESERVED = 1;

    public static final Integer RESERVATION_PENDING = 0;
    public static final Integer RESERVATION_USED = 1;
    public static final Integer RESERVATION_CANCELED = 2;
    public static final Integer RESERVATION_EXPIRED = 3;

    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";
}

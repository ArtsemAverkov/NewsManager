package ru.clevertec.NewsManager.common.utill;

public enum RequestId {
    VALUE_1(1L),
    VALUE_2(2L);

    private final Long value;

    RequestId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}


package ru.clevertec.NewsManager.common.utill;

public enum RequestName {

        NAME("name"),
        ADMIN("admin"),
        USERNAME("username");

        private final String value;

    RequestName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

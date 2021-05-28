package com.github.arlekinside.enums;

public enum UserStatus {
    COMMAND,
    FIND_TITLE,
    FAVOURITES;

    @Override
    public String toString() {
        return this.name();
    }
}

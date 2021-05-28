package com.github.arlekinside.services;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ButtonService {

    void addMovie();

    void deleteMovie();

    Update getUpdate();
}

package com.github.arlekinside.services;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandService {

    void start();

    void help();

    void find();

    void favorites();

    Update getUpdate();

}

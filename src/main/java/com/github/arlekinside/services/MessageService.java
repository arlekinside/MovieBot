package com.github.arlekinside.services;


import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public interface MessageService {

    void responseCommand(CommandService commandService);

    com.github.arlekinside.models.User checkUser(DefaultAbsSender bot, User user);

    void findTitle (DefaultAbsSender bot, Update update);

    void responseButtons(ButtonService buttonService);

}

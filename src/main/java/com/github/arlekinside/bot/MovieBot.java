package com.github.arlekinside.bot;

import com.github.arlekinside.enums.UserStatus;
import com.github.arlekinside.services.MessageService;
import com.github.arlekinside.services.api.MovieApiServiceImpl;
import com.github.arlekinside.services.implementations.ButtonServiceImpl;
import com.github.arlekinside.services.implementations.CommandServiceImpl;
import com.google.gson.Gson;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.github.arlekinside.models.User;

public class MovieBot extends TelegramWebhookBot {
    private MessageService messageService;

    public MovieBot(MessageService messageService) {
        super();
        this.messageService = messageService;

    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().isUserMessage()) {

                User user = messageService.checkUser(this, update.getMessage().getFrom());

                if (update.getMessage().hasText()) {

                    if (user.getStatus().equals(UserStatus.COMMAND.toString()) && update.getMessage().getText().startsWith("/")) {

                        messageService.responseCommand(new CommandServiceImpl(this, update, new MovieApiServiceImpl()));

                    } else if (user.getStatus().equals(UserStatus.FIND_TITLE.toString())) {

                        messageService.findTitle(this, update);

                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            messageService.responseButtons(new ButtonServiceImpl(this, update, new MovieApiServiceImpl()));
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return Configuration.USERNAME;
    }

    @Override
    public String getBotToken() {
        return Configuration.TOKEN;
    }

    @Override
    public String getBotPath() {
        return null;
    }
}

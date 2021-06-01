package com.github.arlekinside.services.implementations;

import com.github.arlekinside.enums.UserStatus;

import com.github.arlekinside.keyboards.InlineKeyboard;
import com.github.arlekinside.models.Movie;
import com.github.arlekinside.services.ButtonService;
import com.github.arlekinside.services.CommandService;
import com.github.arlekinside.services.MessageService;
import com.github.arlekinside.services.api.MovieApiService;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class MessageServiceImpl implements MessageService {

    private final MovieApiService apiService;

    public MessageServiceImpl(MovieApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void responseCommand(CommandService commandService) {
        switch (commandService.getUpdate().getMessage().getText()) {
            case "/start":
                commandService.start();
                break;
            case "/help":
                commandService.help();
                break;
            case "/find":
                commandService.find();
                break;
            case "/favorites":
                commandService.favorites();
                break;
        }
    }

    @Override
    public com.github.arlekinside.models.User checkUser(DefaultAbsSender bot, User user) {
        com.github.arlekinside.models.User check = null;
        try {
            check = apiService.getUser(user.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (check == null) {
            check = new com.github.arlekinside.models.User(user);
            check.setStatus(UserStatus.COMMAND);
            try {
                apiService.addUser(check);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    @Override
    public void findTitle(DefaultAbsSender bot, Update update) {
        StringBuilder string = new StringBuilder();
        Movie movie = null;
        try {
            movie = apiService.findByTitle(update.getMessage().getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (movie == null) {
            SendMessage msg = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Can't find the movie, try again please");
            try {
                bot.execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {

            SendChatAction action = new SendChatAction()
                    .setChatId(update.getMessage().getChatId())
                    .setAction(ActionType.UPLOADPHOTO);
            try {
                bot.execute(action);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            SendPhoto message = new SendPhoto()
                    .setChatId(update.getMessage().getChatId())
                    .setPhoto(movie.getImage());
            string.append("Title --> ")
                    .append(movie.getTitle())
                    .append('\n')
                    .append("Description --> ")
                    .append(movie.getDescription())
                    .append('\n')
                    .append("IMBD ID --> ")
                    .append(movie.getId());
            message.setCaption(string.toString());
            string = new StringBuilder();
            string.append('|')
                    .append(movie.getId());
            InlineKeyboard keyboard = new InlineKeyboard.Builder()
                    .button("Add", string.toString())
                    .row()
                    .build();
            message.setReplyMarkup(keyboard.getKeyboard());
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            try {
                com.github.arlekinside.models.User user = apiService.getUser(update.getMessage().getFrom().getId());
                user.setStatus(UserStatus.COMMAND);
                apiService.addUser(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void responseButtons(ButtonService buttonService) {
        if (buttonService.getUpdate().getCallbackQuery().getData().startsWith("|")) {
            buttonService.addMovie();
        } else if (buttonService.getUpdate().getCallbackQuery().getData().startsWith("-")) {
            buttonService.deleteMovie();
        }
    }
}

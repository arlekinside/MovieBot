package com.github.arlekinside.services.implementations;

import com.github.arlekinside.bot.MovieBot;
import com.github.arlekinside.enums.UserStatus;
import com.github.arlekinside.keyboards.InlineKeyboard;
import com.github.arlekinside.models.Movie;
import com.github.arlekinside.models.User;
import com.github.arlekinside.services.CommandService;
import com.github.arlekinside.services.api.MovieApiService;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

public class CommandServiceImpl implements CommandService {
    private MovieApiService apiService;
    private MovieBot bot;
    private Update update;

    public CommandServiceImpl(MovieBot bot, Update update, MovieApiService apiService) {
        this.bot = bot;
        this.update = update;
        this.apiService = apiService;
    }

    @Override
    public void start() {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("Welcome to Movie bot!\nThat's the place where you can look for movies and save them to your favourites list");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void help() {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("Oh, you want some help.\n" +
                "I'm gonna try to do something with this.\n\n" +
                "So, here is explanation to all the commands I have:\n\n" +
                "/start - the command that writes u to my database, so I can create personal favourites list.\n\n" +
                "/help - are u sure I need to explain?\n\n" +
                "/find - the command turns you to \"Find\" mode, so the next message u type would be sent to world's government servers and find there some movie matching ur message.\n\n" +
                "/favourites - the thing why I'm still working. There u can find all the movies u added before and remove them from the list.");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void find() {

        SendChatAction action = new SendChatAction();
        action.setChatId(update.getMessage().getChatId());
        action.setAction(ActionType.UPLOADPHOTO);
        try {
            bot.execute(action);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("Enter movie title you want to find below");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        User user = null;
        try {
            user = apiService.getUser(update.getMessage().getFrom().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setStatus(UserStatus.FIND_TITLE);
        try {
            apiService.addUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void favorites() {
        User user = new User(update.getMessage().getFrom());
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId());
        List<Movie> movies = null;
        try {
            movies = apiService.getFavourites(update.getMessage().getFrom().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (movies.isEmpty()) {
            message.setText("No movies yet");
            try {
                bot.execute(message);
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
            StringBuilder stringBuilder;
            InlineKeyboard.Builder builder;
            SendPhoto photo;
            for (Movie movie : movies) {
                builder = new InlineKeyboard.Builder();
                photo = new SendPhoto();
                stringBuilder = new StringBuilder();
                photo.setChatId(update.getMessage().getChatId());
                stringBuilder.append("Title --> ")
                        .append(movie.getTitle())
                        .append('\n')
                        .append("Description --> ")
                        .append(movie.getDescription())
                        .append('\n')
                        .append("IMBD ID --> ")
                        .append(movie.getId());
                photo.setCaption(stringBuilder.toString());
                photo.setPhoto(movie.getImage());
                stringBuilder = new StringBuilder();
                stringBuilder.append('-')
                        .append(movie.getId());
                photo.setReplyMarkup(builder
                        .button("Remove", stringBuilder.toString())
                        .row()
                        .build()
                        .getKeyboard()
                );
                try {
                    bot.execute(photo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public Update getUpdate() {
        return update;
    }
}

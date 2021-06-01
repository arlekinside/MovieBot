package com.github.arlekinside.services.implementations;

import com.github.arlekinside.bot.MovieBot;
import com.github.arlekinside.services.ButtonService;
import com.github.arlekinside.services.api.MovieApiService;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class ButtonServiceImpl implements ButtonService {

    private final MovieApiService apiService;
    private final MovieBot bot;
    private final Update update;

    public ButtonServiceImpl(MovieBot bot, Update update, MovieApiService apiService) {
        this.bot = bot;
        this.update = update;
        this.apiService = apiService;
    }

    @Override
    public void addMovie() {
        AnswerCallbackQuery answer = new AnswerCallbackQuery()
                .setText("Processing...")
                .setCallbackQueryId(update.getCallbackQuery().getId())
                .setShowAlert(false);
        try {
            bot.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        String movieId = update.getCallbackQuery().getData().substring(1);

        SendMessage msg = new SendMessage()
                .setChatId(update.getCallbackQuery().getId())
                .setText(update.getCallbackQuery().getData() + " " + movieId);
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            apiService.addFavouriteById(update.getCallbackQuery().getFrom().getId(), movieId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessage message = new SendMessage()
                .setChatId(Long.valueOf(update.getCallbackQuery().getFrom().getId()))
                .setText("Movie added!");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMovie() {
        AnswerCallbackQuery answer = new AnswerCallbackQuery()
                .setText("Processing...")
                .setCallbackQueryId(update.getCallbackQuery().getId())
                .setShowAlert(false);
        try {
            bot.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            apiService.deleteMovie(update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData().substring(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessage message = new SendMessage()
                .setChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()))
                .setText("Movie removed!");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Update getUpdate() {
        return this.update;
    }
}

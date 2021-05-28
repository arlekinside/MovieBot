package com.github.arlekinside.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard{

    private InlineKeyboardMarkup keyboard;


    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(InlineKeyboardMarkup keyboard) {
        this.keyboard = keyboard;
    }

    public static class Builder{

        private List<InlineKeyboardButton> row;
        private List<List<InlineKeyboardButton>> rows;

        public Builder() {
            row = new ArrayList<>();
            rows = new ArrayList<>();
        }

        public Builder button(String text, String replyData){
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData(replyData);
            button.setText(text);
            row.add(button);
            return this;
        }

        public Builder row(){
            rows.add(row);
            row = new ArrayList<>();
            return this;
        }

        public InlineKeyboard build(){
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(rows);
            InlineKeyboard keyboard = new InlineKeyboard();
            keyboard.setKeyboard(markup);
            return keyboard;
        }
    }
}

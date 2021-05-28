package com.github.arlekinside.models;

import com.github.arlekinside.enums.UserStatus;

import java.util.List;

public class User extends org.telegram.telegrambots.meta.api.objects.User {

    private  String username;
    private String status;
    private List<Movie> movies;

    public User(org.telegram.telegrambots.meta.api.objects.User user) {
        super(user.getId(), user.getFirstName(), user.getBot(), user.getLastName(), user.getUserName(), user.getLanguageCode());
        this.username = this.getUserName();
        this.status = null;
    }

    public String getStatus() {
        return status;
    }

    @Deprecated
    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatus(UserStatus status){
        this.status = status.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}

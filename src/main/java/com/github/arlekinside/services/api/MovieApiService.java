package com.github.arlekinside.services.api;

import com.github.arlekinside.models.Movie;
import com.github.arlekinside.models.User;

import java.io.IOException;
import java.util.List;

public interface MovieApiService {

    List<User> getAllUsers() throws IOException;

    User getUser(long id) throws IOException;

    void addUser(User user) throws IOException;

    void updateUser(User user) throws IOException;

    void deleteUser(long id) throws IOException;

    List<Movie> getFavourites(long id) throws IOException;

    void addFavourite(long id, Movie movie) throws IOException;

    void addFavouriteById(long id, String movieId) throws IOException;

    Movie findByTitle(String title) throws IOException;

    void deleteMovie(long id, String movieId) throws IOException;

}

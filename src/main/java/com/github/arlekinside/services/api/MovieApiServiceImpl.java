package com.github.arlekinside.services.api;

import com.github.arlekinside.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import com.github.arlekinside.models.User;

import java.io.IOException;
import java.util.List;

public class MovieApiServiceImpl implements MovieApiService{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT = MediaType.parse("text/html; charset=UTF-8");
    private OkHttpClient client;
    private static String URL = "";
    private StringBuffer urlBuffer;
    private Gson gson;

    public MovieApiServiceImpl() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    @Override
    public List<User> getAllUsers() throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users");
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        var body = response.body();
        if(body == null) return null;
        if(!response.isSuccessful()) return null;
        List<User> users = gson.fromJson(body.string(), new TypeToken<List<User>>(){}.getType());
        body.close();
        return users;
    }

    @Override
    public User getUser(long id) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(id);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        var body = response.body();
        if(body == null) return null;
        if(!response.isSuccessful()) return null;
        User user = gson.fromJson(body.string(), new TypeToken<User>(){}.getType());
        body.close();
        return user;
    }

    @Override
    public void addUser(User user) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users");
        String object = gson.toJson(user);
        RequestBody body = RequestBody.create(object, JSON);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
    }

    @Override
    public void updateUser(User user) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(user.getId());
        String object = gson.toJson(user);
        RequestBody body = RequestBody.create(object, JSON);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
    }

    @Override
    public void deleteUser(long id) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(id);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .delete()
                .build();
        Response response = client.newCall(request).execute();
    }

    @Override
    public List<Movie> getFavourites(long id) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(id)
                .append("/favourites");
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        var body = response.body();
        if(body == null) return null;
        if(!response.isSuccessful()) return null;
        List<Movie> movies = gson.fromJson(body.string(), new TypeToken<List<Movie>>(){}.getType());
        body.close();
        return movies;
    }

    @Override
    public void addFavourite(long id, Movie movie) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(id)
                .append("/favourites");
        String object = gson.toJson(movie);
        RequestBody body = RequestBody.create(object, JSON);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
    }

    @Override
    public void addFavouriteById(long id, String movieId) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(id)
                .append("/favourites/byId");
        RequestBody body = RequestBody.create(movieId, TEXT);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
    }

    @Override
    public Movie findByTitle(String title) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/movies/find?title=")
                .append(title);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        var body = response.body();
        if(body == null) return null;
        if(!response.isSuccessful()) return null;
        Movie movie = gson.fromJson(body.string(), new TypeToken<Movie>(){}.getType());
        body.close();
        return movie;
    }

    @Override
    public void deleteMovie(long id, String movieId) throws IOException {
        urlBuffer = new StringBuffer();
        urlBuffer.append(URL)
                .append("/users/")
                .append(id)
                .append("/favourites");
        Movie movie = new Movie();
        movie.setId(movieId);
        String object = gson.toJson(movie);
        RequestBody body = RequestBody.create(object, JSON);
        Request request = new Request.Builder()
                .url(urlBuffer.toString())
                .delete(body)
                .build();
        Response response = client.newCall(request).execute();
    }
}

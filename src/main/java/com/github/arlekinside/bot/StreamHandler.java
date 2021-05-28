package com.github.arlekinside.bot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arlekinside.services.api.MovieApiService;
import com.github.arlekinside.services.api.MovieApiServiceImpl;
import com.github.arlekinside.services.implementations.MessageServiceImpl;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StreamHandler implements RequestStreamHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(output, Charset.forName("US-ASCII"))));
        Update update = null;

        try {
            update = objectMapper.readValue(new JSONObject(new String(input.readAllBytes(), StandardCharsets.US_ASCII)).getString("body"), Update.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MovieBot bot = new MovieBot(new MessageServiceImpl(new MovieApiServiceImpl()));
        bot.onWebhookUpdateReceived(update);

        writer.write("{\"isBase64Encoded\": true," +
                "    \"statusCode\": 200" +
                "}");
        writer.close();
    }
}

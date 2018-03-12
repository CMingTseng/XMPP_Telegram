package XMPP_Telegram.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.ApiConstants;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.WebhookBot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BotUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, String> STANDARD_HEADERS;

    static {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        STANDARD_HEADERS = Collections.unmodifiableMap(headers);
    }

    /**
     * Creates POST request with json as body.
     *
     * @param bot     the bot
     * @param url     the url
     * @param object  the object to write
     * @return the response
     * @throws TelegramApiException the exception
     */
    public static String doPostJSONQuery(WebhookBot bot, String url, Object object) throws TelegramApiException {
        try {
            Objects.requireNonNull(bot, "bot cannot be null!");
            Objects.requireNonNull(url, "url cannot be null!");
            Objects.requireNonNull(STANDARD_HEADERS, "headers cannot be null!");
            Objects.requireNonNull(object, "object cannot be null!");

            final URL urlAddress = new URL(ApiConstants.BASE_URL + bot.getBotToken() + "/" + url);
            final HttpURLConnection connection = (HttpURLConnection) urlAddress.openConnection();

            // Set POST type
            connection.setRequestMethod("POST");

            // Set headers
            STANDARD_HEADERS.entrySet().forEach(entry -> connection.setRequestProperty(entry.getKey(), entry.getValue()));

            // Set output
            connection.setDoOutput(true);

            // Write output
            try (final OutputStream os = connection.getOutputStream();
                 final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8.name()))) {
                writer.write(OBJECT_MAPPER.writeValueAsString(object));
            }

            connection.connect();

            // Read input
            try (final InputStreamReader input = new InputStreamReader(connection.getInputStream());
                 final BufferedReader reader = new BufferedReader(input)) {
                return reader.lines().parallel().collect(Collectors.joining(System.lineSeparator()));
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            throw new TelegramApiException("Unable to execute post query", e);
        }
    }
}

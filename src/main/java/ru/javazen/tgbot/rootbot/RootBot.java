package ru.javazen.tgbot.rootbot;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class RootBot extends TelegramLongPollingBot {

    private String username;
    private String token;
    private BotSession session;


    static {
        ApiContextInitializer.init();
    }

    public RootBot(@Value("${bot.name}") String username, @Value("${bot.token}") String token) {
        this.username = username;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        session = botsApi.registerBot(this);
    }

    @PreDestroy
    public void destroy() {
        if (session != null) {
            session.stop();
        }
    }
}

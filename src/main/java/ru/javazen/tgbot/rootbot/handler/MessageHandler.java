package ru.javazen.tgbot.rootbot.handler;

import org.telegram.telegrambots.api.objects.Update;

public abstract class MessageHandler implements Handler {

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage();
    }
}

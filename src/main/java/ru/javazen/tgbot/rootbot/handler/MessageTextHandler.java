package ru.javazen.tgbot.rootbot.handler;

import org.telegram.telegrambots.api.objects.Update;

public abstract class MessageTextHandler extends MessageHandler {

    @Override
    public boolean canHandle(Update update) {
        return super.canHandle(update) && update.getMessage().hasText();
    }

}
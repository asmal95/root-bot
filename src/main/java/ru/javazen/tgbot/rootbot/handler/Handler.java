package ru.javazen.tgbot.rootbot.handler;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public interface Handler {

    boolean handleUpdate(Update update, AbsSender sender) throws TelegramApiException;


    boolean canHandle(Update update);


}

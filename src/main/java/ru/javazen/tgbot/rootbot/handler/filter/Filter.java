package ru.javazen.tgbot.rootbot.handler.filter;

import org.telegram.telegrambots.api.objects.Update;

public interface Filter {

    boolean check(Update update);

}

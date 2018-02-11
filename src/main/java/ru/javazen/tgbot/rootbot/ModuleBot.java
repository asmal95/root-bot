package ru.javazen.tgbot.rootbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.javazen.tgbot.rootbot.handler.Handler;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModuleBot extends RootBot {

    @Resource
    private List<Handler> modules;

    public ModuleBot(@Value("${bot.username}") String username, @Value("${bot.token}") String token) {
        super(username, token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            for (Handler module : modules) {
                if (module.canHandle(update) && module.handleUpdate(update, this)) {
                    break;
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
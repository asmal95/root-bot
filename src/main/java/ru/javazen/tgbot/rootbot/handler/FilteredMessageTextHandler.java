package ru.javazen.tgbot.rootbot.handler;

import org.telegram.telegrambots.api.objects.Update;
import ru.javazen.tgbot.rootbot.handler.filter.Filter;

import java.util.List;

public abstract class FilteredMessageTextHandler extends MessageTextHandler {

    private List<Filter> filters;

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean canHandle(Update update) {
        return super.canHandle(update)
                && (filters == null || filters.stream().allMatch(filter -> filter.check(update)));
    }
}

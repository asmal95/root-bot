package ru.javazen.tgbot.rootbot.quote.load;

import ru.javazen.tgbot.rootbot.quote.entity.Quote;

import java.util.List;
import java.util.concurrent.Future;

public interface QuoteLoader {

    List<Quote> getQuotes();
}

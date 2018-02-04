package ru.javazen.tgbot.rootbot.quote;

import ru.javazen.tgbot.rootbot.quote.entity.Quote;

import java.util.List;

public interface QuoteService {

    List<Quote> allQuotes();

    List<Quote> findQuoteByTheme(String theme);
}

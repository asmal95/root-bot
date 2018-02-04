package ru.javazen.tgbot.rootbot.quote;

import org.springframework.stereotype.Service;
import ru.javazen.tgbot.rootbot.quote.entity.Quote;
import ru.javazen.tgbot.rootbot.quote.load.QuoteLoader;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuoteServiceImpl implements QuoteService {

    private QuoteLoader quoteLoader;


    public QuoteServiceImpl(QuoteLoader quoteLoader) {
        this.quoteLoader = quoteLoader;
    }

    @Override
    public List<Quote> allQuotes() {
        return quoteLoader.getQuotes();
    }

    @Override
    public List<Quote> findQuoteByTheme(String theme) {
        List<Quote> quotes = quoteLoader.getQuotes();
        if (quotes == null) {
            return null;
        }
        List<Quote> res = new ArrayList<>();
        for (Quote quote : quotes) {
            if (!quote.getThemes().isEmpty()
                    && theme.equalsIgnoreCase(quote.getThemes().iterator().next().getName())) {
                res.add(quote);
            }
        }
        return res;
    }

}

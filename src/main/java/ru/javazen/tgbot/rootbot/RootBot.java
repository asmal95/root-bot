package ru.javazen.tgbot.rootbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;
import ru.javazen.tgbot.rootbot.nlp.grapheme.GraphemeAnalyzer;
import ru.javazen.tgbot.rootbot.nlp.util.Lemmatizer;
import ru.javazen.tgbot.rootbot.quote.QuoteService;
import ru.javazen.tgbot.rootbot.quote.entity.Quote;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class RootBot extends TelegramLongPollingBot {

    private String name;
    private String token;
    private BotSession session;

    private QuoteService quoteService;
    private Lemmatizer lemmatizer;
    private GraphemeAnalyzer graphemeAnalyzer;

    @Autowired
    public void setLemmatizer(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Autowired
    public void setGraphemeAnalyzer(GraphemeAnalyzer graphemeAnalyzer) {
        this.graphemeAnalyzer = graphemeAnalyzer;
    }

    @Autowired
    public void setQuoteService(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    static {
        ApiContextInitializer.init();
    }

    public RootBot(@Value("${bot.name}") String name, @Value("${bot.token}") String token) {
        this.name = name;
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            return;
        }
        String text = update.getMessage().getText();
        if (!text.contains("Сергей") && !text.contains("сергей")) {
            return;
        }

        String[] words = graphemeAnalyzer.extractGraphemes(text);

        List<Quote> quotes = new ArrayList<>();
        for (String s : words) {
            s = lemmatizer.resolveLemma(s);
            List<Quote> quoteByTheme = quoteService.findQuoteByTheme(s);
            if (quoteByTheme != null) {
                quotes.addAll(quoteByTheme);
            }
        }



        if (!quotes.isEmpty()) {

            Quote quote = quotes.get(new Random().nextInt(quotes.size()-1));
            SendMessage message = new SendMessage()
                    .setText(quote.getText())
                    .setChatId(update.getMessage().getChatId());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return name;
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

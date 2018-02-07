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
import ru.javazen.tgbot.rootbot.association.WordAssociationService;
import ru.javazen.tgbot.rootbot.nlp.grapheme.GraphemeAnalyzer;
import ru.javazen.tgbot.rootbot.nlp.util.Lemmatizer;
import ru.javazen.tgbot.rootbot.quote.QuoteService;
import ru.javazen.tgbot.rootbot.quote.entity.Quote;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Component
public class RootBot extends TelegramLongPollingBot {

    private String username;
    private String token;
    private BotSession session;

    private String givenName;

    private QuoteService quoteService;
    private WordAssociationService wordAssociationService;

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

    @Autowired
    public void setAssociationService(WordAssociationService wordAssociationService) {
        this.wordAssociationService = wordAssociationService;
    }

    static {
        ApiContextInitializer.init();
    }

    public RootBot(@Value("${bot.name}") String username, @Value("${bot.token}") String token, @Value("${bot.given_name") String givenName) {
        this.username = username;
        this.token = token;
        this.givenName = givenName;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            return;
        }
        String text = update.getMessage().getText();
        if (!text.toLowerCase().contains(givenName.toLowerCase())) {
            return;
        }

        String[] words = graphemeAnalyzer.extractGraphemes(text);

        List<Quote> quotes = new ArrayList<>();
        for (String s : words) {
            s = lemmatizer.resolveLemma(s);
            quotes.addAll(findQuotes(s));
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

    private List<Quote> findQuotes(String word) {
        List<Quote> quotes = new ArrayList<>();

        Set<String> associations = null;
        try { //TODO see comment in ru.javazen.tgbot.rootbot.association.WordAssociationServiceWeb
            associations = wordAssociationService.findAssociations(word);
        } catch (Exception e) {
            return quotes;
        }
        associations.add(word);

        for (String association : associations) {
            List<Quote> quoteByTheme = quoteService.findQuoteByTheme(association);
            if (quoteByTheme != null) {
                quotes.addAll(quoteByTheme);
            }
        }

        return quotes;

    }

    @Override
    public String getBotUsername() {
        return username;
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

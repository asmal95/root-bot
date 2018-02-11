package ru.javazen.tgbot.rootbot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.javazen.tgbot.rootbot.association.WordAssociationService;
import ru.javazen.tgbot.rootbot.nlp.grapheme.GraphemeAnalyzer;
import ru.javazen.tgbot.rootbot.nlp.util.Lemmatizer;
import ru.javazen.tgbot.rootbot.quote.QuoteService;
import ru.javazen.tgbot.rootbot.quote.entity.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class QuoteHandler extends MessageTextHandler {

    private QuoteService quoteService;
    private WordAssociationService wordAssociationService;

    private Lemmatizer lemmatizer;
    private GraphemeAnalyzer graphemeAnalyzer;

    private String givenName;

    public QuoteHandler(@Value("${bot.given_name}") String givenName) {
        this.givenName = givenName;
    }

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


    @Override
    public boolean handleUpdate(Update update, AbsSender sender) throws TelegramApiException {
        String text = update.getMessage().getText();

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
                sender.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean canHandle(Update update) {
        return super.canHandle(update) && update.getMessage().getText().toLowerCase().contains(givenName.toLowerCase());
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
}

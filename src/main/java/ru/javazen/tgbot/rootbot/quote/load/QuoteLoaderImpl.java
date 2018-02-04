package ru.javazen.tgbot.rootbot.quote.load;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import ru.javazen.tgbot.rootbot.quote.entity.Quote;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class QuoteLoaderImpl implements QuoteLoader{

    private static final String API = "http://javazen.ru/quote-be/api/quote/get/last10000000";

    private AsyncRestTemplate asyncRestTemplate;
    private QuoteResponse quoteResponse;

    public QuoteLoaderImpl(AsyncRestTemplate asyncRestTemplate) {
        this.asyncRestTemplate = asyncRestTemplate;
    }

    @PostConstruct
    public void load() {
        ParameterizedTypeReference<List<Quote>> responseType = new ParameterizedTypeReference<List<Quote>>() {};

        ListenableFuture<ResponseEntity<List<Quote>>> future =
                asyncRestTemplate.exchange(API, HttpMethod.GET, null, responseType);


        QuoteResponse callback = new QuoteResponse();
        future.addCallback(callback);
        quoteResponse = callback;

    }

    @Override
    public List<Quote> getQuotes() {
        return quoteResponse.getQuotes();
    }









    private class QuoteResponse implements ListenableFutureCallback<ResponseEntity<List<Quote>>> {

        private List<Quote> quotes;

        @Override
        public void onSuccess(ResponseEntity<List<Quote>> responseEntity) {
            quotes = responseEntity.getBody();
        }

        @Override
        public void onFailure(Throwable throwable) {
            System.out.println("ERROR " + throwable.getMessage());
            throwable.printStackTrace();
        }

        public List<Quote> getQuotes() {
            return quotes;
        }
    }
}

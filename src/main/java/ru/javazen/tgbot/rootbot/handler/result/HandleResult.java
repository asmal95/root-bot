package ru.javazen.tgbot.rootbot.handler.result;

import ru.javazen.tgbot.rootbot.handler.Handler;

import java.util.HashMap;
import java.util.Map;

public class HandleResult {

    private Map<Handler, Boolean> results = new HashMap<>();
    private Boolean isHandled = false;

    public Map<Handler, Boolean> getResults() {
        return results;
    }

    public void setResults(Map<Handler, Boolean> results) {
        this.results = results;
    }

    public Boolean getHandled() {
        return isHandled;
    }

    public void setHandled(Boolean handled) {
        isHandled = handled;
    }
}

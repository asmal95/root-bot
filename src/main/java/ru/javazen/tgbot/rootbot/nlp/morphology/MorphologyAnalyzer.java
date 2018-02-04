package ru.javazen.tgbot.rootbot.nlp.morphology;

import java.util.Set;

public interface MorphologyAnalyzer {

    @Deprecated
    String[] extractLemmas(String[] words);

    @Deprecated
    String extractLemma(String word);

    Set<String> extractLemmas(String word);
}

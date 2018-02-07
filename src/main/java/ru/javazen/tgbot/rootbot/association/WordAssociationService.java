package ru.javazen.tgbot.rootbot.association;

import java.util.Set;

public interface WordAssociationService {

    public Set<String> findAssociations(String word);
}

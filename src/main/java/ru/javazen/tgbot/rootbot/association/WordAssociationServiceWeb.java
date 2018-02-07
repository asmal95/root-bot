package ru.javazen.tgbot.rootbot.association;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Set;

@Service
public class WordAssociationServiceWeb implements WordAssociationService {

    private String wordAssociationServiceUrl;
    private RestTemplate restTemplate;

    private static final String GET_ASSOCIATION_PATH = "/associations/find/{0}";

    @Value("${association.service.url}")
    public void setWordAssociationServiceUrl(String wordAssociationServiceUrl) {
        this.wordAssociationServiceUrl = wordAssociationServiceUrl;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Set<String> findAssociations(String word) {
        String url = wordAssociationServiceUrl + GET_ASSOCIATION_PATH;

        url = MessageFormat.format(url, word);

        //TODO need to encode special characters by UriUtils.encodePath
        //but it is'n work for rest template with path variable...

        ResponseEntity<Set<String>> assocResponse =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<Set<String>>() {});


        Set<String> associations = assocResponse.getBody();
        return associations;
    }
}
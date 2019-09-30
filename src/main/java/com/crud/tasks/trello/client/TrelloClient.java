package com.crud.tasks.trello.client;

import com.crud.tasks.domain.TrelloBoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TrelloClient {
    @Value("${trello.api.endpoint.prod}")
    private String trelloApiEndpoint;
    @Value("${trello.app.key}")
    private String trelloAppKey;
    @Value("${trello.app.token}")
    private String trelloAppToken;
    @Value("${trello.app.username}")
    private String trelloAppUsername;
    @Autowired
    private RestTemplate restTemplate;

    private URI createUrl(){
        return UriComponentsBuilder.fromHttpUrl(trelloApiEndpoint + "/members/" + trelloAppUsername + "/boards")
                .queryParam("key", trelloAppKey )
                .queryParam("token", trelloAppToken)
                .queryParam("fields", "name,id")
                .build().encode().toUri();
    }

    public List<TrelloBoardDto> getTrelloBoards() {

        return Arrays.asList(Optional
                .ofNullable(restTemplate.getForObject(createUrl(), TrelloBoardDto[].class))
                .orElse(new TrelloBoardDto[0]));
    }
}

package com.crud.tasks.trello.validator;

import com.crud.tasks.domain.TrelloBoard;
import com.crud.tasks.domain.TrelloCard;
import com.crud.tasks.domain.TrelloList;
import com.crud.tasks.validator.TrelloValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidatorTestSuite {

    @Autowired
    private TrelloValidator trelloValidator;

    private List<ILoggingEvent> prepareLogList() {
        Logger trelloValidatorLogger = (Logger) LoggerFactory.getLogger(TrelloValidator.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        trelloValidatorLogger.addAppender(listAppender);
        return listAppender.list;
    }

    @Test
    public void shouldReturnEmptyList() {
        //Given
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloList("1", "test_list", false));
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoard("1", "test", trelloLists));

        //When
        List<TrelloBoard> returnedTrelloBoards = trelloValidator.validateBoards(trelloBoards);

        //Then
        assertNotNull(returnedTrelloBoards);
        assertEquals(0, returnedTrelloBoards.size());
    }

    @Test
    public void testLoggingValidateBoardsMethod() {
        //Given
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        List<ILoggingEvent> logsList = prepareLogList();

        //When
        trelloValidator.validateBoards(trelloBoards);

        //Then
        assertEquals("Starting filtering boards...", logsList.get(0).getMessage());
        assertEquals(Level.INFO, logsList.get(0).getLevel());
        assertEquals("Boards have been filtered. Current list size: 0", logsList.get(1).getMessage());
        assertEquals(Level.INFO, logsList.get(1).getLevel());
    }

    @Test
    public void shouldReturnList() {
        //Given
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloList("1", "my_list", false));
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoard("1", "my_board", trelloLists));

        //When
        List<TrelloBoard> returnedTrelloBoards = trelloValidator.validateBoards(trelloBoards);

        //Then
        assertNotNull(returnedTrelloBoards);
        assertEquals(1, returnedTrelloBoards.size());

        returnedTrelloBoards.forEach(trelloBoard ->{
            assertEquals("1", trelloBoard.getId());
            assertEquals("my_board", trelloBoard.getName());

            trelloBoard.getLists().forEach(trelloList -> {
                assertEquals("1", trelloList.getId());
                assertEquals("my_list", trelloList.getName());
                assertFalse(trelloList.isClosed());
            });
        });
    }

    @Test
    public void shouldLogTestingInfo() {
        //Given
        TrelloCard card = new TrelloCard("test", "d","p", "lI");
        List<ILoggingEvent> logsList = prepareLogList();

        //When
        trelloValidator.validateTrelloCard(card);

        //Then
        assertEquals("Someone is testing my application!", logsList.get(0).getMessage());
        assertEquals(Level.INFO, logsList.get(0).getLevel());
    }

    @Test
    public void shouldLogProperWayInfo() {
        //Given
        TrelloCard card = new TrelloCard("my_card", "d","p", "lI");
        List<ILoggingEvent> logsList = prepareLogList();

        //When
        trelloValidator.validateTrelloCard(card);

        //Then
        assertEquals("Seams that my application is using in proper way.", logsList.get(0).getMessage());
        assertEquals(Level.INFO, logsList.get(0).getLevel());
    }
}

package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MapperTestSuite {

    private TrelloMapper mapper = new TrelloMapper();

    @Test
    public void testMapToCard() {
        //Given
        TrelloCardDto cardDto = new TrelloCardDto("card1", "desc.1", "pos.1.","1");
        //When
        TrelloCard card = mapper.mapToCard(cardDto);
        //Then
        assertEquals("card1", card.getName());
        assertEquals("desc.1", card.getDescription());
        assertEquals("pos.1.", card.getPos());
        assertEquals("1", card.getListId());
    }

    @Test
    public void testMapToCardDto() {
        //Given
        TrelloCard card = new TrelloCard("card1", "desc.1", "pos.1.","1");
        //When
        TrelloCardDto cardDto = mapper.mapToCardDto(card);
        //Then
        assertEquals("card1", cardDto.getName());
        assertEquals("desc.1", cardDto.getDescription());
        assertEquals("pos.1.", cardDto.getPos());
        assertEquals("1", cardDto.getListId());
    }

    @Test
    public void testMapToList() {
        //Given
        TrelloListDto listDto1 = new TrelloListDto("1", "list1", true);
        TrelloListDto listDto2 = new TrelloListDto("2", "list2", true);
        TrelloListDto listDto3 = new TrelloListDto("3", "list3", false);
        List<TrelloListDto> listDtos = new ArrayList<>();
        listDtos.add(listDto1);
        listDtos.add(listDto2);
        listDtos.add(listDto3);
        //When
        List<TrelloList> lists = mapper.mapToList(listDtos);
        TrelloList list1 = lists.get(0);
        //Then
        assertEquals(3, lists.size());
        assertEquals("1", list1.getId());
        assertEquals("list1", list1.getName());
        assertTrue(list1.isClosed());
    }

    @Test
    public void testMapToListDto() {

        //Given
        TrelloList list1 = new TrelloList("1", "list1", true);
        TrelloList list2 = new TrelloList("2", "list2", true);
        TrelloList list3 = new TrelloList("3", "list3", false);
        List<TrelloList> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        //When
        List<TrelloListDto> listDtos = mapper.mapToListDto(lists);
        TrelloListDto listDto3 = listDtos.get(2);
        //Then
        assertEquals(3, listDtos.size());
        assertEquals("3", listDto3.getId());
        assertEquals("list3", listDto3.getName());
        assertFalse(listDto3.isClosed());
    }

    @Test
    public void testMapToBoards() {
        //Given
        TrelloListDto listDto1 = new TrelloListDto("1", "list1", true);
        TrelloListDto listDto2 = new TrelloListDto("2", "list2", true);
        TrelloListDto listDto3 = new TrelloListDto("3", "list3", false);
        TrelloListDto listDto4 = new TrelloListDto("4", "list4", false);
        TrelloListDto listDto5 = new TrelloListDto("5", "list5", true);
        List<TrelloListDto> listDtos1 = new ArrayList<>();
        List<TrelloListDto> listDtos2 = new ArrayList<>();
        listDtos1.add(listDto1);
        listDtos1.add(listDto2);
        listDtos2.add(listDto3);
        listDtos2.add(listDto4);
        listDtos2.add(listDto5);
        TrelloBoardDto boardDto1 = new TrelloBoardDto("1", "board1", listDtos1);
        TrelloBoardDto boardDto2 = new TrelloBoardDto("2", "board2", listDtos2);
        List<TrelloBoardDto> boardDtos = new ArrayList<>();
        boardDtos.add(boardDto1);
        boardDtos.add(boardDto2);
        //When
        List<TrelloBoard> boards = mapper.mapToBoards(boardDtos);
        TrelloBoard board = boards.get(0);
        //Then
        assertEquals(2, boards.size());
        assertEquals("1", board.getId());
        assertEquals("board1", board.getName());
        assertEquals(2, board.getLists().size());
    }

    @Test
    public void mapToBoardsDto() {
        //Given
        TrelloList list1 = new TrelloList("1", "list1", true);
        TrelloList list2 = new TrelloList("2", "list2", true);
        TrelloList list3 = new TrelloList("3", "list3", false);
        TrelloList list4 = new TrelloList("4", "list4", false);
        TrelloList list5 = new TrelloList("5", "list5", false);
        List<TrelloList> lists1 = new ArrayList<>();
        List<TrelloList> lists2 = new ArrayList<>();
        lists1.add(list1);
        lists1.add(list2);
        lists2.add(list3);
        lists2.add(list4);
        lists2.add(list5);
        TrelloBoard board1 = new TrelloBoard("1", "board1", lists1);
        TrelloBoard board2 = new TrelloBoard("2", "board2", lists2);
        List<TrelloBoard> boards = new ArrayList<>();
        boards.add(board1);
        boards.add(board2);
        //When
        List<TrelloBoardDto> boardDtos = mapper.mapToBoardsDto(boards);
        TrelloBoardDto boardDto = boardDtos.get(1);
        //Then
        assertEquals(2, boardDtos.size());
        assertEquals("2", boardDto.getId());
        assertEquals("board2", boardDto.getName());
        assertEquals(3, boardDto.getLists().size());

    }
}
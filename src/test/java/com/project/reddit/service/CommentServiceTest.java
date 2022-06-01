package com.project.reddit.service;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.user.UserInfo;
import com.project.reddit.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CommentServiceTest {

    private List<CommentDto> commentDtos = new ArrayList<>();

    private void populateList() {

        commentDtos.add(new CommentDto(1L, "Random text 1", new UserInfo(1L, "Username1", "ImageUrl"), null, new ArrayList<>()));
        commentDtos.add(new CommentDto(2L, "Random text 2", new UserInfo(2L, "Username2", "ImageUrl"), 1L, new ArrayList<>()));
        commentDtos.add(new CommentDto(3L, "Random text 3", new UserInfo(3L, "Username3", "ImageUrl"), null, new ArrayList<>()));
        commentDtos.add(new CommentDto(4L, "Random text 4", new UserInfo(4L, "Username4", "ImageUrl"), 1L, new ArrayList<>()));
        commentDtos.add(new CommentDto(5L, "Random text 5", new UserInfo(5L, "Username5", "ImageUrl"), null, new ArrayList<>()));
        commentDtos.add(new CommentDto(6L, "Random text 6", new UserInfo(6L, "Username6", "ImageUrl"), 2L, new ArrayList<>()));
        commentDtos.add(new CommentDto(7L, "Random text 7", new UserInfo(7L, "Username7", "ImageUrl"), null, new ArrayList<>()));
        commentDtos.add(new CommentDto(8L, "Random text 8", new UserInfo(8L, "Username8", "ImageUrl"), null, new ArrayList<>()));
        commentDtos.add(new CommentDto(9L, "Random text 9", new UserInfo(9L, "Username9", "ImageUrl"), 5L, new ArrayList<>()));
        commentDtos.add(new CommentDto(10L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 6L, new ArrayList<>()));
        commentDtos.add(new CommentDto(11L, "Random text 7", new UserInfo(7L, "Username7", "ImageUrl"), 9L, new ArrayList<>()));
        commentDtos.add(new CommentDto(12L, "Random text 8", new UserInfo(8L, "Username8", "ImageUrl"), 8L, new ArrayList<>()));
        commentDtos.add(new CommentDto(13L, "Random text 9", new UserInfo(9L, "Username9", "ImageUrl"), 11L, new ArrayList<>()));
        commentDtos.add(new CommentDto(14L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 4L, new ArrayList<>()));
        commentDtos.add(new CommentDto(15L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 8L, new ArrayList<>()));
        commentDtos.add(new CommentDto(16L, "Random text 7", new UserInfo(7L, "Username7", "ImageUrl"), 10L, new ArrayList<>()));
        commentDtos.add(new CommentDto(17L, "Random text 8", new UserInfo(8L, "Username8", "ImageUrl"), 12L, new ArrayList<>()));
        commentDtos.add(new CommentDto(18L, "Random text 9", new UserInfo(9L, "Username9", "ImageUrl"), 5L, new ArrayList<>()));
        commentDtos.add(new CommentDto(19L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 7L, new ArrayList<>()));

    }

    public ListMultimap<Long, CommentDto> populateExpectedOutcome () {
        ListMultimap<Long, CommentDto> expectedOutcome = MultimapBuilder.treeKeys().arrayListValues().build();

        expectedOutcome.put(1L, new CommentDto(1L, "Random text 1", new UserInfo(1L, "Username1", "ImageUrl"), null, new ArrayList<>()));
        expectedOutcome.put(1L, new CommentDto(2L, "Random text 2", new UserInfo(2L, "Username2", "ImageUrl"), 1L, new ArrayList<>()));
        expectedOutcome.put(1L, new CommentDto(4L, "Random text 4", new UserInfo(4L, "Username4", "ImageUrl"), 1L, new ArrayList<>()));
        expectedOutcome.put(1L, new CommentDto(6L, "Random text 6", new UserInfo(6L, "Username6", "ImageUrl"), 2L, new ArrayList<>()));
        expectedOutcome.put(1L, new CommentDto(10L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 6L, new ArrayList<>()));
        expectedOutcome.put(1L, new CommentDto(14L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 4L, new ArrayList<>()));
        expectedOutcome.put(1L, new CommentDto(16L, "Random text 7", new UserInfo(7L, "Username7", "ImageUrl"), 10L, new ArrayList<>()));

        expectedOutcome.put(3L, new CommentDto(3L, "Random text 3", new UserInfo(3L, "Username3", "ImageUrl"), null, new ArrayList<>()));

        expectedOutcome.put(5L, new CommentDto(5L, "Random text 5", new UserInfo(5L, "Username5", "ImageUrl"), null, new ArrayList<>()));
        expectedOutcome.put(5L, new CommentDto(9L, "Random text 9", new UserInfo(9L, "Username9", "ImageUrl"), 5L, new ArrayList<>()));
        expectedOutcome.put(5L, new CommentDto(18L, "Random text 9", new UserInfo(9L, "Username9", "ImageUrl"), 5L, new ArrayList<>()));
        expectedOutcome.put(5L, new CommentDto(11L, "Random text 7", new UserInfo(7L, "Username7", "ImageUrl"), 9L, new ArrayList<>()));
        expectedOutcome.put(5L, new CommentDto(13L, "Random text 9", new UserInfo(9L, "Username9", "ImageUrl"), 11L, new ArrayList<>()));

        expectedOutcome.put(7L, new CommentDto(7L, "Random text 7", new UserInfo(7L, "Username7", "ImageUrl"), null, new ArrayList<>()));
        expectedOutcome.put(7L, new CommentDto(19L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 7L, new ArrayList<>()));

        expectedOutcome.put(8L, new CommentDto(8L, "Random text 8", new UserInfo(8L, "Username8", "ImageUrl"), null, new ArrayList<>()));
        expectedOutcome.put(8L, new CommentDto(12L, "Random text 8", new UserInfo(8L, "Username8", "ImageUrl"), 8L, new ArrayList<>()));
        expectedOutcome.put(8L, new CommentDto(15L, "Random text 10", new UserInfo(10L, "Username10", "ImageUrl"), 8L, new ArrayList<>()));
        expectedOutcome.put(8L, new CommentDto(17L, "Random text 8", new UserInfo(8L, "Username8", "ImageUrl"), 12L, new ArrayList<>()));



        return expectedOutcome;
    }

    @Test
    public void testSortingMethod() {

        //populate list
        populateList();

        List<CommentDto> commentList = commentDtos;

        //check if list is null
        if (commentList == null) {
            throw new NotFoundException("List is null");
        }

        if (commentList.isEmpty()) {
            log.info("List is empty");
            throw new NotFoundException("List empty not the throw i want tbh");
        }

        var multimap = addBaseCommentAndFirstChildrenToMap(commentList);
        var rest = addRestCommentAccordinglyToMultiMap(multimap, commentList);


        if (!rest.isEmpty()) {
            multimap.putAll(rest);
        }

        //sort by id
        multimap.values().stream().sorted(Comparator.comparing(i -> i.getId())).collect(Collectors.toList());

        // rest of comments

        Assertions.assertEquals(populateExpectedOutcome(), multimap);

    }

    private ListMultimap<Long, CommentDto> addBaseCommentAndFirstChildrenToMap(List<CommentDto> commentDtos) {
        ListMultimap<Long, CommentDto> multimap = MultimapBuilder.treeKeys().arrayListValues().build();

        commentDtos.removeIf(e -> {
            if (e.getParentId() == null) {
                multimap.put(e.getId(), e);
                return true;
            }

            return false;
        });


        //iterate through list again and find all children from base ids
        commentDtos.removeIf(item -> {
            if (!multimap.containsValue(item)) {
                if (multimap.containsKey(item.getParentId())) {
                    multimap.put(item.getParentId(), item);
                    return true;
                }
            }
            return false;
        });



        return multimap;
    }

    private ListMultimap<Long, CommentDto> addRestCommentAccordinglyToMultiMap(ListMultimap<Long, CommentDto> multimap, List<CommentDto> commentDtos) {
        ListMultimap<Long, CommentDto> rest = MultimapBuilder.treeKeys().arrayListValues().build();


        for (var temp: commentDtos
        ) {

            for (var map: multimap.entries()
            ) {

                if (temp.getParentId() == map.getValue().getId()) {
                    rest.put(map.getKey(), temp);
                    break;
                }

            }

            multimap.putAll(rest);
            rest.clear();
        }

        return rest;
    }


}

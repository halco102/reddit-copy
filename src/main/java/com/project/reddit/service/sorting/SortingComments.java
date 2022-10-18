package com.project.reddit.service.sorting;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.project.reddit.dto.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SortingComments implements SortingCommentsInterface {

    /*
     *This method is used for sorting comments by parentId,
     *every comment that points to base comment will be stored in a multimap
     */
    @Override
    public List<CommentDto> sortingComments(List<CommentDto> commentsDto) {

        var multimap = addBaseCommentAndFirstChildrenToMap(commentsDto);
        var rest = addRestCommentAccordinglyToMultiMap(multimap, commentsDto);

        if (!rest.isEmpty()) {
            multimap.putAll(rest);
        }

        multimap.keys().stream().sorted(Comparator.reverseOrder());
        // change it back to list
        return multimap.values().stream().collect(Collectors.toList());
    }



    /*
     * Helper method for storing comments.
     * Takes the initial list and finds all comments that are base comments
     * and first elements that are pointing to base comment,
     * after its found it is deleted from list
     *
     * */
    private ListMultimap<Long, CommentDto> addBaseCommentAndFirstChildrenToMap(List<CommentDto> commentDtos) {
        ListMultimap<Long, CommentDto> multimap = MultimapBuilder.treeKeys(Comparators.comparable().reversed()).arrayListValues().build();

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

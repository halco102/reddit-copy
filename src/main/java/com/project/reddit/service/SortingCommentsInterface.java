package com.project.reddit.service;

import com.project.reddit.dto.comment.CommentDto;

import java.util.List;

public interface SortingCommentsInterface {

    List<CommentDto> sortingComments(List<CommentDto> commentDto);

}

package com.project.reddit.mapper;

import com.project.reddit.dto.post.*;
import com.project.reddit.model.content.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

@Mapper(componentModel = "spring", uses = {CommentMapper.class, CategoryMapper.class})
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Post toEntity(PostRequestDto requestDto);

    @Mapping(source = "user", target = "postedBy")
    PostResponseDto postResponse(Post post);

    @Mapping(source = "post.postLikeOrDislikes", target = "postLikeOrDislikeDtos")
    PostForFrontPageDto toFrontPageDto(Post post);


    @Mapping(source = "user", target = "postedBy")
    @Mapping(target = "commentsDto", source = "comments")
    @Mapping(source = "post.postLikeOrDislikes", target = "postLikeOrDislikeDtos")
    PostDto toPostDto(Post post);
}

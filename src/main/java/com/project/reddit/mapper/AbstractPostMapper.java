package com.project.reddit.mapper;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostForFrontPageDto;
import com.project.reddit.dto.post.PostNotificationDto;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.dto.user.notification.UserNotification;
import com.project.reddit.model.content.Post;
import lombok.RequiredArgsConstructor;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {AbstractCommentMapper.class})
@RequiredArgsConstructor
public abstract class AbstractPostMapper {

    public abstract Post toEntity(PostRequestDto requestDto);

    @Mappings({
            @Mapping(source = "user", target = "postedBy"),
            @Mapping(target = "commentsDto", source = "comments"),
            @Mapping(source = "post.postLikeOrDislikes", target = "postLikeOrDislikeDtos")
    })
    public abstract PostDto toPostDto(Post post);


    @Mapping(source = "post.postLikeOrDislikes", target = "postLikeOrDislikeDtos")
    public abstract PostForFrontPageDto toFrontPageDto(Post post);

    public abstract Post postDtoToEntity(PostDto postDto);


    public abstract PostNotificationDto notifications(Post post);
}

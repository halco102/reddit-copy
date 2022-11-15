package com.project.reddit.mapper;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.dto.user.UserInfo;
import com.project.reddit.model.category.Category;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
class AbstractPostMapperTest {

    private final User user = new User(1L, "username", "password", "email@email.com", LocalDate.now(), "imageUrl", UserRole.ROLE_USER);

    private Post post;

    private PostDto postDto;

    private UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), user.getImageUrl());

    private PostRequestDto postRequestDto;

    private  Set<CategoryDto> categoryDtos = new HashSet<>(
            Arrays.asList(
                    new CategoryDto(1L, "cat1", "image1"),
                    new CategoryDto(2L, "cat2", "image2"),
                    new CategoryDto(3L, "cat3", "image3")
            )
    );


    @BeforeEach
    public void beforeEachSetup() {

        post = new Post(1L, "title", "text", "imageUrl", false, LocalDateTime.now(), null, user, null, null, null, null );
        postDto = new PostDto(1L, "title", "text", "imageUrl", LocalDateTime.now(), null, false, userInfo, new ArrayList<>(), new ArrayList<>(), categoryDtos);
        postRequestDto = new PostRequestDto("title", "text", "imageUrl", false, categoryDtos);
    }

    @Test
    void toEntity() {
        var toEntity = Mappers.getMapper(AbstractPostMapper.class).toEntity(postRequestDto);

        Assertions.assertEquals(toEntity.getText(), postRequestDto.getText());
        Assertions.assertEquals(toEntity.getTitle(), postRequestDto.getTitle());
        Assertions.assertEquals(toEntity.getImageUrl(), postRequestDto.getImageUrl());
        Assertions.assertEquals(toEntity.isAllowComments(), postRequestDto.isAllowComments());
        Assertions.assertEquals(toEntity.getCategories().size(), postRequestDto.getCategories().size());

    }

    @Test
    void toPostDto() {

        post.setCategories(
                new HashSet<>(
                        Arrays.asList(
                                new Category(1L, "cat1", "image1"),
                                new Category(2L, "cat2", "image2"),
                                new Category(3L, "cat3", "image3")
                        )
                )
        );

        var toPostDto = Mappers.getMapper(AbstractPostMapper.class).toPostDto(post);

        Assertions.assertEquals(toPostDto.getId(), post.getId());
        Assertions.assertEquals(toPostDto.getText(), post.getText());
        Assertions.assertEquals(toPostDto.getTitle(), post.getTitle());
        Assertions.assertEquals(toPostDto.getPostedBy().getId(), post.getUser().getId());
        Assertions.assertEquals(toPostDto.getPostedBy().getUsername(), post.getUser().getUsername());
        Assertions.assertEquals(toPostDto.getPostedBy().getImageUrl(), post.getUser().getImageUrl());

        //comments and like/dislike are empty when Post is initialized
        Assertions.assertNull(toPostDto.getCommentsDto());
        Assertions.assertNull(toPostDto.getPostLikeOrDislikeDtos());

        Assertions.assertEquals(toPostDto.getCategories().size(), post.getCategories().size());

    }


}
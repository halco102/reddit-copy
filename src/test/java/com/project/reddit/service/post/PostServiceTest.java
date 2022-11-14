package com.project.reddit.service.post;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.user.UserInfo;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.kafka.service.generic.NotificationContext;
import com.project.reddit.mapper.AbstractCategoryMapper;
import com.project.reddit.mapper.AbstractPostMapper;
import com.project.reddit.model.category.Category;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.service.cloudinary.CloudinaryService;
import com.project.reddit.service.likedislike.SimpleLikeOrDislikeFactory;
import com.project.reddit.service.search.FilterUserContent;
import com.project.reddit.service.sorting.SortingCommentsInterface;
import com.project.reddit.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
class PostServiceTest {

    /*
    * Mocks
    * */
    @Mock
    PostRepository postRepository;

    @Mock
    AbstractPostMapper postMapper;

    @Mock
    UserService userService;

    @Mock
    CloudinaryService cloudinaryService;

    @Mock
    SortingCommentsInterface sortingCommentsInterface;

    @Mock
    FilterUserContent<Post> filterUserContent;

    @Mock
    AbstractCategoryMapper categoryMapper;

    @Mock
    SimpleLikeOrDislikeFactory simpleLikeOrDislikeFactory;

    @Mock
    NotificationContext notificationContext;

    @InjectMocks
    PostService postService;


    /*
    * Data
    * */
    private List<Comment> comments = new ArrayList<>();

    private Set<Category> categories = new HashSet<>();

    private List<Post> posts = new ArrayList<>();

    private User user = new User();

    private UserInfo userInfo = new UserInfo();

    private  Set<CategoryDto> categoryDtos = new HashSet<>(
            Arrays.asList(
                    new CategoryDto(1L, "cat1", "image1"),
                    new CategoryDto(2L, "cat2", "image2"),
                    new CategoryDto(3L, "cat3", "image3")
            )
    );

    private List<PostDto> postDtos = new ArrayList<>();


    @BeforeEach
    public void beforeEach() {

        user = new User(1L, "halco", "password123", "email@email.com", LocalDate.now(), "userImage", UserRole.ROLE_USER);
        userInfo = new UserInfo(user.getId(), user.getUsername(), user.getImageUrl());

        categories.add(new Category(1L, "name1", "icon", new ArrayList<>()));
        categories.add(new Category(2L, "name2", "icon", new ArrayList<>()));
        categories.add(new Category(3L, "name3", "icon", new ArrayList<>()));
        categories.add(new Category(4L, "name4", "icon", new ArrayList<>()));
        categories.add(new Category(5L, "name5", "icon", new ArrayList<>()));

        user = new User(1L, "halco", "987456321aa", "email@email.com", LocalDate.now(), "userImage", UserRole.ROLE_USER);

        posts.add(new Post(1L, "title", "text", "image", false, LocalDateTime.now(), null, user, null, null, categories, null));
        posts.add(new Post(2L, "title2", "text2", "image2", false, LocalDateTime.now(), null, user, null, null, categories, null));
        posts.add(new Post(3L, "title3", "text3", "image3", false, LocalDateTime.now(), null, user, null, null, categories, null));
        posts.add(new Post(4L, "title4", "text4", "image4", false, LocalDateTime.now(), null, user, null, null, categories, null));


        postDtos.add(new PostDto(1L, "title", "text", "image", LocalDateTime.now(), null, false, userInfo, new ArrayList<>(), new ArrayList<>(), categoryDtos));
        postDtos.add(new PostDto(2L, "title2", "text2", "image2", LocalDateTime.now(), null, false, userInfo, new ArrayList<>(), new ArrayList<>(), categoryDtos));
        postDtos.add(new PostDto(3L, "title3", "text3", "image3", LocalDateTime.now(), null, false, userInfo, new ArrayList<>(), new ArrayList<>(), categoryDtos));
        postDtos.add(new PostDto(4L, "title4", "text4", "image4", LocalDateTime.now(), null, false, userInfo, new ArrayList<>(), new ArrayList<>(), categoryDtos));

    }

    @Test
    void savePost() {
    }

    @Test
    void deletePostById() {

    }

    @Test
    void getAllPostsTest() {

        Mockito.when(postRepository.findAll()).thenReturn(posts);

        Mockito.when(postMapper.toPostDto(Mockito.any())).thenReturn(postDtos.get(0), postDtos.get(1), postDtos.get(2), postDtos.get(3));


        var test = postService.getAllPosts();

        Collections.reverse(posts);

        Assertions.assertEquals(test.size(), posts.size());
        Assertions.assertEquals(test.get(0).getId(), posts.get(0).getId());
        Assertions.assertEquals(test.get(0).getText(), posts.get(0).getText());

        Assertions.assertEquals(test.get(0).getPostedBy().getId(), posts.get(0).getUser().getId());
        Assertions.assertEquals(test.get(0).getPostedBy().getUsername(), posts.get(0).getUser().getUsername());
    }

    @Test
    void getPostEntityByIdTest() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(posts.get(0)));
        var testActualValue = postService.getPostEntityById(1L);

        Assertions.assertNotNull(testActualValue);
        Assertions.assertEquals(testActualValue.getId(), posts.get(0).getId());

        Assertions.assertNotNull(testActualValue.getTitle());
        Assertions.assertEquals(testActualValue.getTitle(), posts.get(0).getTitle());
    }

    @Test
    void  getPostByEntityIdThrowNotFoundException() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NotFoundException.class, () -> postService.getPostEntityById(1L));
    }

    @Test
    void getPostDtoByIdTest() {

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(posts.get(0)));
        Mockito.when(postMapper.toPostDto(Mockito.any())).thenReturn(postDtos.get(0));


        var testValue = postService.getPostDtoById(1L);

        Assertions.assertEquals(testValue.getId(), posts.get(0).getId());
        Assertions.assertEquals(testValue.getTitle(), posts.get(0).getTitle());

    }

    @Test
    void getPostDtoByIdThrowExceptionWhenParamIdIsNull() {
        Assertions.assertThrows(BadRequestException.class, () -> postService.getPostDtoById(null));
    }

    @Test
    void getPostDtoByIdThrowExceptionIfPostDoesNotExist() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> postService.getPostDtoById(1L));
    }

    @Test
    void getSimilarPostsByTitleTest() {
        var similarPosts = new HashSet<>(Arrays.asList(posts.get(0), posts.get(3)));
        Mockito.when(postRepository.getPostsByTitle(Mockito.any(String.class))).thenReturn(similarPosts);

        Mockito.when(postMapper.toPostDto(Mockito.any(Post.class))).thenReturn(postDtos.get(0), postDtos.get(3));

        var toTest = postService.getSimilarPostsByTitle("title");
        var toList = similarPosts.stream().collect(Collectors.toList());

        Assertions.assertEquals(toTest.size(), toList.size());

        Assertions.assertEquals(toTest.get(0).getId(), toList.get(0).getId());
        Assertions.assertEquals(toTest.get(0).getTitle(), toList.get(0).getTitle());

        Assertions.assertEquals(toTest.get(1).getId(), toList.get(1).getId());
        Assertions.assertEquals(toTest.get(1).getTitle(), toList.get(1).getTitle());

    }

    @Test
    void getSimilarPostsByTitleThrowBadRequestIfTitleIsNullOrBlank() {
        //when param is null
        Assertions.assertThrows(BadRequestException.class, () -> postService.getSimilarPostsByTitle(null));

        //when param is empty
        Assertions.assertThrows(BadRequestException.class, () -> postService.getSimilarPostsByTitle(""));
    }

    @Test
    void getSimilarPostsByTitleTestThrowNotFoundExceptionWhenListIsNull() {
        Mockito.when(postRepository.getPostsByTitle(Mockito.any(String.class))).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> postService.getSimilarPostsByTitle("nothing"));
    }

    @Test
    void sortPostByNumberOfLikesTest() {


        Mockito.when(postRepository.sortPostByLikesOrDislikes(Mockito.anyBoolean())).thenReturn(new HashSet<>(Arrays.asList(posts.get(3), posts.get(1), posts.get(0), posts.get(2))));
        Mockito.when(postMapper.toPostDto(Mockito.any(Post.class))).thenReturn(postDtos.get(3), postDtos.get(1), postDtos.get(0), postDtos.get(2));

        var testValue = postService.sortPostByNumberOfLikes();

        Assertions.assertEquals(testValue.size(), posts.size());

        //most likes
        Assertions.assertEquals(testValue.get(0).getId(), posts.get(3).getId());

        //least likes
        Assertions.assertEquals(testValue.get(testValue.size() - 1).getId(), posts.get(2).getId());
    }

    @Test
    void sortPostByNumberOfLikesThrowNotFoundExceptionIfListIsNull() {
        Mockito.when(postRepository.sortPostByLikesOrDislikes(Mockito.anyBoolean())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> postService.sortPostByNumberOfLikes());
    }

    @Test
    void sortPostByNumberOfDislikesTest() {


        Mockito.when(postRepository.sortPostByLikesOrDislikes(Mockito.anyBoolean())).thenReturn(new HashSet<>(Arrays.asList(posts.get(3), posts.get(1), posts.get(0), posts.get(2))));
        Mockito.when(postMapper.toPostDto(Mockito.any(Post.class))).thenReturn(postDtos.get(3), postDtos.get(1), postDtos.get(0), postDtos.get(2));

        var testValue = postService.sortPostByNumberOfDislikes();

        Assertions.assertEquals(testValue.size(), posts.size());

        //least likes
        Assertions.assertEquals(testValue.get(0).getId(), posts.get(3).getId());

        //most likes
        Assertions.assertEquals(testValue.get(testValue.size() - 1).getId(), posts.get(2).getId());

    }

    @Test
    public void sortPostByNumberOfDislikesThrowNotFoundExceptionIfListIsNull() {
        Mockito.when(postRepository.sortPostByLikesOrDislikes(Mockito.anyBoolean())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> postService.sortPostByNumberOfDislikes());
    }


}
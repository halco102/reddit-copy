package com.project.reddit.service.post;

import com.project.reddit.constants.UserProfileSearchType;
import com.project.reddit.dto.likeordislike.LikeOrDislikeRequest;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.dto.post.UpdatePostDto;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.mapper.AbstractCategoryMapper;
import com.project.reddit.mapper.AbstractPostMapper;
import com.project.reddit.model.content.Post;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.service.cloudinary.CloudinaryService;
import com.project.reddit.service.likedislike.PostLikeOrDislikeService;
import com.project.reddit.service.likedislike.SimpleLikeOrDislikeFactory;
import com.project.reddit.service.search.FilterUserContent;
import com.project.reddit.service.sorting.SortingCommentsInterface;
import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor()
public class PostService implements PostInterface, PostCategory{

    private final PostRepository postRepository;
    private final AbstractPostMapper postMapper;

    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    private final SortingCommentsInterface sortingCommentsInterface;

    private final FilterUserContent<Post> filterUserContent;

    private final AbstractCategoryMapper categoryMapper;

    private final SimpleLikeOrDislikeFactory factory;



    /*
    * This method is used for adding new post to database.
    * The user sends the post request and optionaly a file
    *
    * If a user sends a file, the cloudinary method executes, it saves the image
    * on the server and gives back the URL that will be saved in db.
    * */
    public PostDto savePost(PostRequestDto requestDto, MultipartFile file) {

        var user = userService.getCurrentlyLoggedUser();


        if ((requestDto.getImageUrl() == null || requestDto.getImageUrl().isBlank()) && file == null) {
            throw new BadRequestException("Image url and files are blank or null");
        }else if (file != null) {
            // upload file and get url
            requestDto.setImageUrl(cloudinaryService.getUrlFromUploadedMedia(file));
        }

        if (requestDto.getCategories() == null || requestDto.getCategories().isEmpty()) {
            throw new BadRequestException("Categories are empty!");
        }

        Post post = postMapper.toEntity(requestDto);

        post.setAllowComments(requestDto.isAllowComments());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        var savedPost = this.postRepository.save(post);
        log.info("Post is saved " + savedPost);

        var postDto = postMapper.toPostDto(savedPost);

        //workaround
        postDto.setCommentsDto(new ArrayList<>());
        postDto.setPostLikeOrDislikeDtos(new ArrayList<>());


        return postDto;
    }

    public void deletePostById(Long id) {
        var user = userService.getCurrentlyLoggedUser();
        var post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new NotFoundException("This post of id "  + id + " does not exist");
        }

        if (user.getPosts().stream().anyMatch(item -> item.equals(post.get()))) {
            // can delete
            postRepository.deleteById(id);
            log.info("Post deleted");
        }else {
            throw new NotFoundException("User does not have the right to delete this post");
        }
    }

    public List<PostDto> getAllPosts() {
        var getPosts = this.postRepository.findAll();

        if (getPosts == null) {
            throw new NotFoundException("Posts list is null");
        }

        //until date is implemented
        Collections.reverse(getPosts);

        var posts = getPosts.stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
        return posts;
    }

    public Post getPostEntityById(Long id) {
        return this.postRepository.findById(id).orElseThrow(
                () -> {throw new NotFoundException("The post of id: " + id + " cannot be found");});
    }

    public PostDto getPostDtoById(Long id) {

        if (id == null) {
            throw new BadRequestException("The id is null");
        }
        var getPostById =  postRepository.findById(id);

        if(getPostById.isEmpty()) {
            throw new NotFoundException("The post of id: " + id + " cannot be found");
        }

        var post = postMapper.toPostDto(getPostById.get());

        var sortComments = sortingCommentsInterface.sortingComments(post.getCommentsDto());
        post.setCommentsDto(sortComments);

        return post;
    }

    public List<PostDto> getSimilarPostsByTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new BadRequestException("The title is eather null or empty");
        }
        var listOfPosts = this.postRepository.getPostsByTitle(title);

        if(listOfPosts == null) {
            throw new NotFoundException("The post list is null");
        }

        return listOfPosts.stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
    }

    public PostDto saveLikeOrDislikeForPost(LikeOrDislikeRequest request) {
        return (PostDto) factory.getImplementation(PostLikeOrDislikeService.class)
                .likeOrDislike(request);
    }

    public void deleteAllPosts() {
        this.postRepository.deleteAll();
    }


    /*
    * This method is used for sorting posts by number of likes
    * */
    public List<PostDto> sortPostByNumberOfLikes() {
        var sortByNumberOfLikes = this.postRepository.sortPostByLikesOrDislikes(true);

        if (sortByNumberOfLikes == null) {
           throw new NotFoundException("List is null");
        }

        return sortByNumberOfLikes.stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
    }

    /*
    * This method is used for sorting posts by number of dislikes
    * */
    public List<PostDto> sortPostByNumberOfDislikes() {
        var sortByNumberOfDislikes = this.postRepository.sortPostByLikesOrDislikes(false);

        if (sortByNumberOfDislikes == null) {
            throw new NotFoundException("List is null");
        }

        return sortByNumberOfDislikes.stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
    }

    public List<PostDto> filterPostsFromUserProfile(Long userId) {
        return this.filterUserContent.filterUserContent(userId, UserProfileSearchType.POST).stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPostByCategoryName(String categoryName) {
        var temp = postRepository.getAllPostByCategoryName(categoryName);
        return this.postRepository.getAllPostByCategoryName(categoryName).stream()
                .map(m -> postMapper.toPostDto(m)).collect(Collectors.toList());
    }

    @Override
    public PostDto updatePostById(Long id, UpdatePostDto request) {

        var findPost = getPostEntityById(id);

        findPost.setTitle(!request.getTitle().isBlank() ? request.getTitle() : findPost.getTitle());
        findPost.setText(!request.getText().isBlank() && request.getText() != null ? request.getText() : findPost.getText());

        if (findPost.isAllowComments() != request.isAllowComments()) {
            if (!request.isAllowComments()) {
                // false
                //delete all comments from post
                findPost.getComments().clear();
            }

            findPost.setAllowComments(request.isAllowComments());
        }

        findPost.getCategories().clear();

        request.getCategories().forEach(cat -> findPost.getCategories().add(categoryMapper.toCategoryEntity(cat)));

        findPost.setEditedAt(LocalDateTime.now());

        var savedPost = postRepository.save(findPost);

        return postMapper.toPostDto(savedPost);
    }
}

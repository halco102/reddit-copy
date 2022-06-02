package com.project.reddit.service;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostLikeOrDislikeRequest;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.mapper.PostMapper;
import com.project.reddit.model.content.EmbedablePostLikeOrDislikeId;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.content.PostLikeOrDislike;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    private final SortingCommentsInterface sortingCommentsInterface;


    /*
    * This method is used for adding new post to database.
    * The user sends the post request and optionaly a file
    *
    * If a user sends a file, the cloudinary method executes, it saves the image
    * on the server and gives back the URL that will be saved in db.
    * */
    public PostDto savePost(PostRequestDto requestDto, MultipartFile file) {

        //var getUserById = userService.getUserById(requestDto.getUserId());
        var user = userService.getCurrentlyLoggedUser();


        if ((requestDto.getImageUrl() == null || requestDto.getImageUrl().isBlank()) && file == null) {
            throw new BadRequestException("Image url and files are blank or null");
        }else if (file != null) {
            // upload file and get url
            requestDto.setImageUrl(cloudinaryService.getUrlFromUploadedMedia(file));
        }

        Post post = postMapper.toEntity(requestDto);

        post.setAllowComments(requestDto.isAllowComments());
        post.setUser(user);

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

    public Post findPostById(Long id) {
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

    public PostDto saveLikeOrDislikeForPost(PostLikeOrDislikeRequest request) {
        var getPostById = this.postRepository.findById(request.getPostId());
        var user = userService.getCurrentlyLoggedUser();

        if (getPostById.isEmpty()) {
            throw new NotFoundException("Post by id: " + request.getPostId() + " cannot be found");
        }

        PostLikeOrDislike postLikeOrDislike = new PostLikeOrDislike(new EmbedablePostLikeOrDislikeId(user.getId(), getPostById.get().getId()),
                getPostById.get(), user, request.isLikeOrDislike());


        if (!getPostById.get().getPostLikeOrDislikes().isEmpty()){
            var findComment = checkIfUserLikedPost(getPostById.get(), user);

            if (findComment.isEmpty()) {
                getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
            }else {
                // if the request is same set likeDislike to null
                if (findComment.get().isLikeOrDislike() == request.isLikeOrDislike()) {
                    // same like request as in the db, remove it from list
                    getPostById.get().getPostLikeOrDislikes()
                            .removeIf(i -> i.getEmbedablePostLikeOrDislikeId().getPostId().equals(findComment.get().getEmbedablePostLikeOrDislikeId().getPostId())
                            && i.getEmbedablePostLikeOrDislikeId().getUserId().equals(user.getId()));
                }else {
                    // there is no same request as in db add it to the list
                    findComment.get().setLikeOrDislike(request.isLikeOrDislike());
                }
            }

        }else {
            // add if its empty
            getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
        }

        var savePost = this.postRepository.save(getPostById.get());
        return postMapper.toPostDto(savePost);
    }

    /*
    * Helper method for save like or dislike on posts
    * Checks if the user already liked or disliked the post
    * */
    private Optional<PostLikeOrDislike> checkIfUserLikedPost(Post post, User user) {
        var findComment = post.getPostLikeOrDislikes()
                .stream()
                .filter(item -> item.getEmbedablePostLikeOrDislikeId().getPostId().equals(post.getId()) &&
                        item.getEmbedablePostLikeOrDislikeId().getUserId().equals(user.getId())).findAny();
        return findComment;
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

}

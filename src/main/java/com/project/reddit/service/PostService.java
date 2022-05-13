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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    //private final SimpMessagingTemplate simpMessagingTemplate;
    private final CloudinaryService cloudinaryService;

    public PostDto savePost(PostRequestDto requestDto, MultipartFile file) {

        //var getUserById = userService.getUserById(requestDto.getUserId());
        var user = userService.getCurrentlyLoggedUser();
        String imageUrl = null;

        if (requestDto.getImageUrl().isBlank() && file == null) {
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

        var postMap = postMapper.postResponse(savedPost);
        var postDto = postMapper.toPostDto(savedPost);

        //workaround
        postDto.setCommentsDto(new ArrayList<>());
        postDto.setPostLikeOrDislikeDtos(new ArrayList<>());

        //simpMessagingTemplate.convertAndSend("/topic/post", postMap);

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

        return postMapper.toPostDto(getPostById.get());
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
            throw new NotFoundException("Post by id: " + request.getPostId() + " cannot befound");
        }

        PostLikeOrDislike postLikeOrDislike = new PostLikeOrDislike(new EmbedablePostLikeOrDislikeId(user.getId(), getPostById.get().getId()),
                getPostById.get(), user, request.isLikeOrDislike());


        if (!getPostById.get().getPostLikeOrDislikes().isEmpty()){
            /*
            var findComment = getPostById.get().getPostLikeOrDislikes()
                    .stream()
                    .filter(e -> e.getEmbedablePostLikeOrDislikeId().getPostId() == request.getPostId()
                            && e.getEmbedablePostLikeOrDislikeId().getUserId() == user.getId()).findAny();
            if (!findComment.isEmpty()) {
                findComment.get().setLikeOrDislike(request.isLikeOrDislike());
            }else {
                getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
            }*/
            var findComment = checkIfUserLikedPost(getPostById.get(), user);

            if (findComment.isEmpty()) {
                getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
            }else {
                // if the request is same set likeDislike to null
                if (findComment.get().isLikeOrDislike() == request.isLikeOrDislike()) {
                    // same likedislike as in db remove it

                    getPostById.get().getPostLikeOrDislikes()
                            .removeIf(i -> i.getEmbedablePostLikeOrDislikeId().getPostId().equals(findComment.get().getEmbedablePostLikeOrDislikeId().getPostId())
                            && i.getEmbedablePostLikeOrDislikeId().getUserId().equals(user.getId()));
                }else {
                    findComment.get().setLikeOrDislike(request.isLikeOrDislike()); // just change
                }
            }

        }else {
            // add if its empty
            getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
        }

        var savePost = this.postRepository.save(getPostById.get());
        return postMapper.toPostDto(savePost);
    }

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

    public List<PostDto> getAllPostsByUser(Long id) {
        var getAllPostsByUser = this.postRepository.getAllPostByUserId(id);
        if (getAllPostsByUser.isEmpty()) {
            throw new NotFoundException("The object is null");
        }

        return getAllPostsByUser.get().stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
    }

}

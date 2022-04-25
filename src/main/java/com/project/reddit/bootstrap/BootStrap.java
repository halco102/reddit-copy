package com.project.reddit.bootstrap;

import com.project.reddit.model.content.Post;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.message.CommentLikeDislike;
import com.project.reddit.model.message.EmbedableCommentLikeDislikeId;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import com.project.reddit.repository.CommentRepository;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootStrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        //create users
        final String password = "123123123";
        User user = new User(null, "halco", passwordEncoder.encode(password), "email@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb358bab.svg", UserRole.ROLE_ADMIN);
        User user1 = new User(null, "weejws", passwordEncoder.encode(password), "email1@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb3258bab.svg",UserRole.ROLE_USER);
        User user2 = new User(null, "nedim", passwordEncoder.encode(password), "email2@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb3548bab.svg",UserRole.ROLE_ADMIN);
        User user3 = new User(null, "amar", passwordEncoder.encode(password), "email3@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb35zbab.svg",UserRole.ROLE_USER);
        User user4 = new User(null, "igor", passwordEncoder.encode(password), "email4@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb3f8bab.svg",UserRole.ROLE_ADMIN);

        List<User> users = Arrays.asList(user, user1, user2, user3, user4);

        users.stream().forEach(i -> userRepository.save(i));

        //create posts
        Post post = new Post(null, "Blah", "", "https://img-9gag-fun.9cache.com/photo/aYrMb30_700bwp.webp",  true, user);
        Post post1 = new Post(null, "SOME IMAGE", "Some random text", "https://i.imgur.com/ZxtSYA7.jpg",  true, user1);
        Post post2 = new Post(null, "got death threats over place. how's your day? :D", "" +
                "got death threats over place. how's your day? :Dgot death threats over place. how's your day? :Dgot death threats over place. how's your day? :D" , "https://preview.redd.it/6x3xwhvr04s81.jpg?width=640&crop=smart&auto=webp&s=12e286f10d8ce741ad1e36a0074c9796f0fbb58f",  true, user2);
        Post post3 = new Post(null, "aepgojaepgjapog gaeoigjaeiogjaeiogjaeiog gaeoigjaeiogjaeiogjaeiog", "", "https://avatars.dicebear.com/api/bottts/cb35dab1.svg",  true, user3);
        Post post4 = new Post(null, "KGEPOAGKAEPOGKAEPOGKAEPOGPO", "gjpaegjoaepgjaepgojaepgo', 'gaeoigjaeiogjaeiogjaeiog gaeoigjaeiogjaeiogjaeiog gaeoigjaeiogjaeiogjaeiog", "https://avatars.dicebear.com/api/bottts/cb35adab1.svg",  true, user4);

        List<Post> posts = Arrays.asList(post, post1, post2, post3, post4);
        posts.stream().forEach(i -> postRepository.save(i));

        //comments
        Comment comment = new Comment(null, "comment1", LocalDate.now(), post, user, null);
        Comment comment1 = new Comment(null, "comment2", LocalDate.now(), post, user2, Long.valueOf(1));
        Comment comment2 = new Comment(null, "comment3", LocalDate.now(), post, user3, Long.valueOf(2));
        Comment comment3 = new Comment(null, "comment4", LocalDate.now(), post, user4, Long.valueOf(1));

        Comment comment4 = new Comment(null, "comment5", LocalDate.now(), post1, user, null);
        Comment comment5 = new Comment(null, "comment6", LocalDate.now(), post1, user2, Long.valueOf(5));

        Comment comment6 = new Comment(null, "comment7", LocalDate.now(), post2, user3, null);
        Comment comment7 = new Comment(null, "comment8", LocalDate.now(), post2, user4, Long.valueOf(7));
        Comment comment8 = new Comment(null, "comment9", LocalDate.now(), post2, user, null);

        Comment comment9 = new Comment(null, "comment10", LocalDate.now(), post3, user, null);
        Comment comment10 = new Comment(null, "comment11", LocalDate.now(), post3, user2, Long.valueOf(10));
        Comment comment11 = new Comment(null, "comment12", LocalDate.now(), post3, user3, Long.valueOf(10));
        Comment comment12 = new Comment(null, "comment13", LocalDate.now(), post3, user3, Long.valueOf(10));

        Comment comment13 = new Comment(null, "comment14", LocalDate.now(), post4, user, null);
        Comment comment14 = new Comment(null, "comment15", LocalDate.now(), post4, user4, Long.valueOf(14));
        Comment comment15 = new Comment(null, "comment16", LocalDate.now(), post4, user4, null);

        List<Comment> comments = Arrays.asList(comment,comment1,comment2,comment3,
                comment4,comment5,comment6,comment7,comment8,comment9,
                comment10,comment11,comment12,comment13,comment14,comment15);

        comments.stream().forEach(i -> commentRepository.save(i));

        // add likes to comments

        CommentLikeDislike commentLikeDislike = new CommentLikeDislike();
        commentLikeDislike.setComment(comment);
        commentLikeDislike.setUser(user);
        commentLikeDislike.setLikeOrDislike(true);
        user.setLikeDislikes(Arrays.asList(commentLikeDislike));


        userRepository.save(user);


        log.info("Added all to db");

    }
}

package com.project.reddit.model.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.category.Category;
import com.project.reddit.model.likedislike.PostLikeOrDislike;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "allow_comments")
    private boolean allowComments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLikeOrDislike> postLikeOrDislikes;


    @ManyToMany
    @JoinTable(
            name = "post_categories",
            joinColumns = @JoinColumn(name = "posts_id"), inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    private Set<Category> categories;

    @ManyToMany(mappedBy = "notifications")
    @JsonIgnore
    private Set<User> notifications;


    public Post(Long id, String title, String text, String imageUrl, boolean allowComments, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.allowComments = allowComments;
        this.user = user;
    }


}

package com.project.reddit.model.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Comment {

    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate createdAt;

    @Column(name = "edited")
    private LocalDate edited;

    @ManyToOne
    @JoinColumn(name = "posts_id")
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Column(name = "parent_comment_id")
    private Long parentId;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CommentLikeOrDislike> likeDislikes = new ArrayList<>();

    public Comment(Long id, String text, LocalDate createdAt, Post post, User user, Long parentId) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.post = post;
        this.user = user;
        this.parentId = parentId;
    }
}

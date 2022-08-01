package com.project.reddit.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.likedislike.PostLikeOrDislike;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.user.follow.Follows;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "roles")
    private UserRole userRole;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate createdAt;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentLikeOrDislike> likeDislikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostLikeOrDislike> postLikeOrDislikes;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verified")
    private boolean verified;

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    private Set<Follows> following;

    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
    private Set<Follows> followers;

/*    @ManyToMany
    @JoinTable(name = "user_follow", joinColumns = @JoinColumn(name = "follow_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<User> followers;

    @ManyToMany
    @JoinTable(name = "user_follow", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "follow_id"))
    private Set<User> following;*/


    public User(Long id, String username, String password, String email, LocalDate createdAt, String imageUrl, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.userRole = role;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}

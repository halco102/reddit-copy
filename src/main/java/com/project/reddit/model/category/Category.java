package com.project.reddit.model.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.content.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Entity
public class Category {

    @Id
    @SequenceGenerator(name = "categories_sequence", sequenceName = "categories_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_sequence")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JsonIgnore
    private List<Post> posts;

}

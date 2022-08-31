package com.project.reddit.kafka.model;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.model.user.follow.Follows;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

   private PostDto postDto;

   private Set<Follows> follows;

}

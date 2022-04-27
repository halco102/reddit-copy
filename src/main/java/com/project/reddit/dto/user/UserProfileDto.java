package com.project.reddit.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.project.reddit.dto.comment.LikedOrDislikedCommentsUser;
import com.project.reddit.dto.post.PostForFrontPageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;

    private String username;

    private String email;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    private String imageUrl;

    private List<PostForFrontPageDto> posts;

    private List<LikedOrDislikedCommentsUser> likedOrDislikedComments;

}

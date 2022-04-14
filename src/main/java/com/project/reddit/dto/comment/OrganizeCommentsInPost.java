package com.project.reddit.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class OrganizeCommentsInPost {

    Map<Long, List<Long>> organizedComments;

    public OrganizeCommentsInPost() {
        organizedComments = new HashMap<>();
    }

}

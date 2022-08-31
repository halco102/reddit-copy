package com.project.reddit.mapper;

import com.project.reddit.dto.user.follower.FollowDto;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.follow.Follows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AbstractFollowMapper {


    public abstract Set<FollowDto> toFollwing(Set<Follows> following);

    @Mapping(target = "FollowDto", source = "")
    public abstract Set<FollowDto> toFollowers(Set<Follows> followers);

}

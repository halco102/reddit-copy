package com.project.reddit.repository;

import com.project.reddit.dto.user.notification.UserNotification;

import java.util.Set;

public interface IUserEntityManager {
    Set<UserNotification> getUserNotificationsFromTempTable(Long userId);

}

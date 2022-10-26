package com.project.reddit.repository;

import com.project.reddit.dto.user.notification.UserNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Join;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class UserRepositoryEntityManager implements IUserEntityManager{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Set<UserNotification> getUserNotificationsFromTempTable(Long userId) {


        Query query1 = entityManager.createNativeQuery("Select \n" +
                "u.id as usersId, \n" +
                "u.username, \n" +
                "u.image_url as usersImage, \n" +
                "p.id as postsId, \n" +
                "p.title, \n" +
                "p.image_url as postImage \n" +
                "from user_notifications_temp as unt\n" +
                "inner join users as u on unt.users_id = u.id\n" +
                "left join posts as p on unt.posts_id = p.id");


        List<Object> result = (List<Object>) query1.getResultList();
        Iterator iterator = result.listIterator();
        Set<UserNotification> userNotifications = new HashSet<>();

        while (iterator.hasNext()){
            Object[] objects = (Object[]) iterator.next();
            userNotifications.add(new UserNotification(
                    Long.parseLong(String.valueOf(objects[0])),
                    String.valueOf(objects[1]),
                    String.valueOf(objects[2]),

                    //potential nulls
                    objects[3] != null ? Long.parseLong(String.valueOf(objects[3])) : null,
                    objects[4] != null ? String.valueOf(objects[4]) : null,
                    objects[5] != null ? String.valueOf(objects[5]) : null
            ));
        }

        return userNotifications;
    }
}

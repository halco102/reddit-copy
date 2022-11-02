package com.project.reddit.repository;

import com.project.reddit.dto.user.notification.UserNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class UserRepositoryEntityManager implements IUserEntityManager{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Set<UserNotification> getUserNotificationsFromTempTable(Long userId) {


        Query query1 = entityManager.createNativeQuery("select pu.id as postedById," +
                " pu.username, pu.image_url as userImage," +
                "p.id as postsId, p.title, p.image_url as postImageUrl  from user_notifications_temp unt \n" +
                "inner join users u on unt.users_id  = u.id\n" +
                "left join posts p on unt.posts_id = p.id\n" +
                "inner join users pu on p.users_id = pu.id \n" +
                "where unt.users_id = ?1 order by p.created_at desc").setParameter(1, userId);


        List<Object> result = (List<Object>) query1.getResultList();
        Iterator iterator = result.listIterator();
        Set<UserNotification> userNotifications = new LinkedHashSet<>();


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

package com.project.reddit.repository;

import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void itShouldFindUserBasedOnUsername() {
        //given
        User user = new User(null, "halco123", "123123123", "email123123@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb358bab.svg", UserRole.ROLE_ADMIN);
        user.setLikeDislikes(new ArrayList<>());
        user.setComments(new ArrayList<>());
        user.setPosts(new ArrayList<>());
        userRepository.save(user);

        //when
        var userFromDb = userRepository.getUserByUsername(user.getUsername());
        //then

        assertThat(userFromDb.isEmpty()).isFalse();
    }

    @Test
    void itShouldCheckIfUserExistsById() {
        //given
        User user = new User(null, "halco1234", "123123123", "email1231234@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb358bab.svg", UserRole.ROLE_ADMIN);
        user.setLikeDislikes(new ArrayList<>());
        user.setComments(new ArrayList<>());
        user.setPosts(new ArrayList<>());
        userRepository.save(user);

        //when
        var userFromDb = userRepository.getById(user.getId());
        //then

        Assertions.assertEquals(user, userFromDb, "User Object is same");
    }

    @Test
    void itShouldCheckIfUserExistsByEmail() {
        //given
        User user = new User(null, "halco1234", "123123123", "email1231234@email.com", LocalDate.now(), "https://avatars.dicebear.com/api/bottts/cb358bab.svg", UserRole.ROLE_ADMIN);
        user.setLikeDislikes(new ArrayList<>());
        user.setComments(new ArrayList<>());
        user.setPosts(new ArrayList<>());
        userRepository.save(user);

        //when
        var userFromDb = userRepository.getUserByEmail(user.getEmail());
        //then

        Assertions.assertEquals(user, userFromDb.get(), "User Object is same");
    }

}

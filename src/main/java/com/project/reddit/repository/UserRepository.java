package com.project.reddit.repository;

import com.project.reddit.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "Select * from users where username = :username", nativeQuery = true)
    Optional<User> getUserByUsername(@Param("username") String username);

    @Query(value = "Select * from users where email = :email", nativeQuery = true)
    Optional<User> getUserByEmail(@Param("email") String email);

    @Query(value = "Select * from users where verification_code = :code", nativeQuery = true)
    Optional<User> verifieUserCode(@Param("code") String code);

    @Query(value = "select * from users inner join posts as p on p.users_id = users.id where users_id = :id", nativeQuery = true)
    Optional<User> getAllPostByUserId(@Param("id") Long id);

    @Query(value = "Select * from users where name like %:name%", nativeQuery = true)
    Set<User> searchPostsByName(@Param("name") String name);
}

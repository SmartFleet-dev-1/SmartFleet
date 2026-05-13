package com.edutech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     // ✅ Find user by username
     @Query("SELECT u FROM User u WHERE u.username = :username")
     Optional<User> findByUsername(@Param("username") String username);

     // ✅ Check if username exists
     @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
     boolean existsByUsername(@Param("username") String username);

     // ✅ FIXED: Removed invalid roles join
     @Query("SELECT u FROM User u WHERE u.username = :username")
     Optional<User> findByUsernameWithRoles(@Param("username") String username);

}

package com.edutech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.Role;
import com.edutech.entity.User;
import com.edutech.entity.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ EXISTING — unchanged
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    // ✅ EXISTING — unchanged
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    // ✅ EXISTING — unchanged
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);

    // ✅ NEW — find pending users by role
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.role = :role")
    List<User> findByStatusAndRole(@Param("status") UserStatus status, @Param("role") Role role);

    // ✅ NEW — find all pending users
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") UserStatus status);

    // ✅ NEW — count pending users
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    long countByStatus(@Param("status") UserStatus status);

    // ✅ NEW — count by role
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.status = 'ACTIVE'")
    long countByRoleAndActive(@Param("role") Role role);

    // ✅ NEW — find first admin (super admin = lowest id with ADMIN role)
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN' AND u.status = 'ACTIVE' ORDER BY u.id ASC")
    List<User> findAllActiveAdminsOrderById();
}
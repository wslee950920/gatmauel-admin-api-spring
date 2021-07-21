package com.gatmauel.admin.repository.admin;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gatmauel.admin.entity.admin.Admin;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT admin FROM Admin admin WHERE admin.email=:email")
    Optional<Admin> findByEmail(String email);
}

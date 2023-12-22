package com.apirest.apirest.repository;

import com.apirest.apirest.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    @Query("select u from UserEntity u where u.username = ?1")
    Optional<UserEntity> getName(String username);
}


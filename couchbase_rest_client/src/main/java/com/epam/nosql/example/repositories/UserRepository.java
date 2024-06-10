package com.epam.nosql.example.repositories;

import com.epam.nosql.example.dto.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CouchbaseRepository<User, String> {
    Optional<User> getUserByEmail(String email);

    @Query("SELECT META(t).`id` AS __id, t.* FROM `test-sample` t UNNEST t.sports AS sport WHERE sport.sportName = $1")
    List<User> findUserBySportName(String sportName);
}

package ru.abolsoft.core.workspace.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.abolsoft.core.common.repositories.BaseRepository;
import ru.abolsoft.core.workspace.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {

    @Query("select u from User u where u.name = :name")
    Optional<User> findByName(@Param("name") String name);
}
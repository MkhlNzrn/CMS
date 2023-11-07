package ru.abolsoft.core.workspace.repositories;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.abolsoft.core.common.repositories.BaseRepository;
import ru.abolsoft.core.workspace.entities.Folder;

import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends BaseRepository<Folder, UUID> {
    @Query("select f from Folder f where f.name = ?1")
    Folder findByName(String name);


    @Query("select f from Folder f where f.id = :id")
    Optional<Folder> findById( @Param("id") UUID id);
}
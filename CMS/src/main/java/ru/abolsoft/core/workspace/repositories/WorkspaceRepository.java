package ru.abolsoft.core.workspace.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.abolsoft.core.common.repositories.BaseRepository;
import ru.abolsoft.core.workspace.entities.Workspace;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends BaseRepository<Workspace, UUID> {

    @Override
    @NotNull
    @Query(value = "select w from Workspace w where w.id = :id")
    Optional<Workspace> findById(@Param("id") @NotNull UUID id);
}
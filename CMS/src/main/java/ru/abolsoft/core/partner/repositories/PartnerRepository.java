package ru.abolsoft.core.partner.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.abolsoft.core.common.repositories.BaseRepository;
import ru.abolsoft.core.partner.Partner;
import ru.abolsoft.core.workspace.entities.Workspace;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerRepository extends BaseRepository<Partner, UUID> {
}
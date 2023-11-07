package ru.abolsoft.core.common.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ru.abolsoft.core.common.entities.BaseEntity;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean                               // Todo: change to BaseAggregate
public interface BaseRepository<Aggregate extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<Aggregate, ID>, JpaSpecificationExecutor<Aggregate> {

    default @NotNull Aggregate getById(@NotNull ID id) {
        Optional<Aggregate> op = findById(id);
        return op.orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}

package ru.abolsoft.core.common.entities;

import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Optional;

public interface Entity<TId extends Serializable> extends Persistable<TId> {
    @Override
    @Transient
    TId getId();

    @Override
    @Transient
    boolean isNew();

    Optional<Long> getVersion();

    @Override
    String toString();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}

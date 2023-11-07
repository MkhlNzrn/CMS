package ru.abolsoft.core.common;

import org.jetbrains.annotations.NotNull;
import ru.abolsoft.core.common.entities.BaseAggregateRoot;

import java.io.Serializable;
import java.util.Objects;

public class DomainEvent<TId extends Serializable, T extends BaseAggregateRoot<TId>> {
    private final T aggregate;

    protected DomainEvent(@NotNull T aggregate) {
        this.aggregate = aggregate;
    }

    public @NotNull TId getAggregateId() {
        return Objects.requireNonNull(aggregate.getId());
    }
}

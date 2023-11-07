package ru.abolsoft.core.common.entities;

import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
public abstract class BaseAggregateRoot<TId extends Serializable>
        extends BaseEntity<TId> {

    private transient final @Transient List<Object> domainEvents = new ArrayList<>();

    protected <T> T registerEvent(T event) {

        Assert.notNull(event, "Domain event must not be null");

        this.domainEvents.add(event);
        return event;
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }


    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}

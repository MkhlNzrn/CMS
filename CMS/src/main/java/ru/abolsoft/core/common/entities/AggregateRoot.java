package ru.abolsoft.core.common.entities;

import jakarta.persistence.MappedSuperclass;


import java.io.Serializable;

@MappedSuperclass
public abstract class AggregateRoot<TId extends Serializable> extends BaseAggregateRoot<TId> {
}

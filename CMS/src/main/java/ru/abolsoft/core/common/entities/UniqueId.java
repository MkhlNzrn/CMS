package ru.abolsoft.core.common.entities;

import java.io.Serializable;
import java.util.Objects;


public class UniqueId<T> implements Serializable {
    private final T id;
    public UniqueId(T id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return id.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueId<?> uniqueId = (UniqueId<?>) o;
        return Objects.equals(id, uniqueId.id);
    }
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

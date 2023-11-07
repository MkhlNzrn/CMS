package ru.abolsoft.core.common.exceptions;

public class EntityNotFound extends DomainException {
    public EntityNotFound(String domainName, String msg) {
        super(domainName, msg);
    }
    public EntityNotFound(String domainName) {
        super(domainName, "entity not found");
    }
}

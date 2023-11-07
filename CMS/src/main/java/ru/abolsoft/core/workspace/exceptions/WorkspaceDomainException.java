package ru.abolsoft.core.workspace.exceptions;


import ru.abolsoft.core.common.exceptions.DomainException;

public class WorkspaceDomainException extends DomainException {

    public WorkspaceDomainException(String msg) {
        super("workspace", msg);
    }
}



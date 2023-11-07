package ru.abolsoft.core.workspace.exceptions;

public class EmployeeLimitReached extends WorkspaceDomainException {
    public EmployeeLimitReached() {
        super("employee limit reached");
    }
}

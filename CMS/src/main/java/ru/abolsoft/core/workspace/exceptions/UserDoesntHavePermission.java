package ru.abolsoft.core.workspace.exceptions;

public class UserDoesntHavePermission extends WorkspaceDomainException{
    public UserDoesntHavePermission() {
        super("user doesn't have permission");
    }
}

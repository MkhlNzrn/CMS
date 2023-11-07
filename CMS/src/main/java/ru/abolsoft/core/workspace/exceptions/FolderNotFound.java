package ru.abolsoft.core.workspace.exceptions;

public class FolderNotFound extends WorkspaceDomainException {
    public FolderNotFound() {
        super("folder not found");
    }

    public FolderNotFound(String msg) {
        super(msg);
    }
}

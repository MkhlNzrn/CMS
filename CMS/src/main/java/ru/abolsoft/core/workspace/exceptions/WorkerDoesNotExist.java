package ru.abolsoft.core.workspace.exceptions;

import java.util.UUID;

public class WorkerDoesNotExist extends WorkspaceDomainException {
    public WorkerDoesNotExist(UUID workspaceId, UUID userId) {
        super("workspace(" + workspaceId + ")does not contain worker(" + userId + ")");
    }
}

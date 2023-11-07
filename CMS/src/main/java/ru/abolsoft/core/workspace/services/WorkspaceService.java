package ru.abolsoft.core.workspace.services;


import ru.abolsoft.core.workspace.entities.Action;
import ru.abolsoft.core.workspace.entities.Limits;
import ru.abolsoft.core.workspace.entities.Workspace;

import java.util.Set;
import java.util.UUID;

public interface WorkspaceService {
    Workspace get(UUID id);

    UUID createWorkspace(UUID userId);

    UUID createFolder(UUID id, UUID folderId, String s, UUID doerId);

    UUID createFolder(UUID id, String s, UUID doerId);

    UUID createStaff(UUID workspaceId, String newStaffName, UUID doerId);

    void addPermissions(UUID workspaceId, UUID userId, Set<Action> actions, UUID doerId);

    void setPermissions(UUID workspaceId, UUID userId, Set<Action> actions, UUID doerId);

    void setNewLimits(UUID workspaceId, Limits limits);

    void addStaff(UUID staffId);

}

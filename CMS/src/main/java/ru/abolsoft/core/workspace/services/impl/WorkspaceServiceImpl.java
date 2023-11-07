package ru.abolsoft.core.workspace.services.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.abolsoft.core.workspace.entities.*;
import ru.abolsoft.core.workspace.repositories.UserRepository;
import ru.abolsoft.core.workspace.repositories.WorkspaceRepository;
import ru.abolsoft.core.workspace.services.WorkspaceService;

import java.util.Set;
import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;


    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Workspace get(UUID id) {
        return workspaceRepository.getById(id);
    }

    @Override
    @Transactional
    public UUID createWorkspace(UUID userId) {
        var admin = userRepository.getById(userId);
        var w = Workspace.createBy(UUID.randomUUID(), admin, null);
        w = workspaceRepository.saveAndFlush(w);
        return w.getId();
    }

    @Override
    public UUID createFolder(UUID workspaceId, UUID targetId, String newFolderName, UUID doerId) {
        var f = Folder.create(UUID.randomUUID(), newFolderName);
        Workspace w = workspaceRepository.getById(workspaceId);
        w.createFolder(f.getId(), f.getName(), targetId, doerId);
        return f.getId();
    }

    @Override
    public UUID createFolder(UUID workspaceId, String newFolderName, UUID doerId) {
        var f = Folder.create(UUID.randomUUID(), newFolderName);
        Workspace w = workspaceRepository.getById(workspaceId);
        w.createFolder(f.getId(), f.getName(), doerId);
        workspaceRepository.save(w);
        return f.getId();
    }

    @Override
    public UUID createStaff(UUID workspaceId, String newStaffName, UUID doerId) {
        Workspace w = workspaceRepository.getById(workspaceId);
        User staff = w.createStaff(UUID.randomUUID(), newStaffName, doerId);
        workspaceRepository.save(w);
        return staff.getId();
    }

    @Override
    public void addPermissions(UUID workspaceId, UUID userId, Set<Action> actions, UUID doerId) {
        Workspace w = workspaceRepository.getById(workspaceId);
        w.addPermissions(userId, actions, doerId);
        workspaceRepository.save(w);
    }

    @Override
    public void setPermissions(UUID workspaceId, UUID userId, Set<Action> actions, UUID doerId) {
        Workspace w = workspaceRepository.getById(workspaceId);
        w.setPermissions(userId, actions, doerId);
        workspaceRepository.save(w);
    }

    @Override
    public void setNewLimits(UUID id, Limits limits) {
        Workspace w = workspaceRepository.getById(id);
        w.updateLimits(limits);
        workspaceRepository.save(w);
    }

    @Override
    public void addStaff(UUID staffId) {
        Workspace w = workspaceRepository.getById(staffId);
        w.addStaff(userRepository.getById(staffId));
        workspaceRepository.save(w);
    }
}



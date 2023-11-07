package ru.abolsoft.core.workspace.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.abolsoft.core.workspace.entities.*;
import ru.abolsoft.core.workspace.exceptions.EmployeeLimitReached;
import ru.abolsoft.core.workspace.exceptions.UserDoesntHavePermission;
import ru.abolsoft.core.workspace.repositories.UserRepository;
import ru.abolsoft.core.workspace.repositories.WorkspaceRepository;

import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Transactional
class WorkspaceServiceTest {
    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Rollback
    public void createNewTest() {
        var admin = User.create(UUID.randomUUID(), "TEST ADMIN");
        userRepository.save(admin);
        UUID newWorkspaceId = workspaceService.createWorkspace(admin.getId());

        Workspace savedWorkspace = workspaceRepository.getById(newWorkspaceId);

        Assertions.assertEquals(admin, savedWorkspace.getAdmin());
        Assertions.assertEquals(0, savedWorkspace.getRootFolder().getLayerLevel());


        Folder folder1 = Folder.create(UUID.randomUUID(), "Folder 1");
        Folder folder2 = Folder.create(UUID.randomUUID(), "Folder 2");
        savedWorkspace.createFolder(folder1.getId(), folder1.getName(), admin.getId());

        Assertions.assertTrue(savedWorkspace.isContains(folder1.getId()));

        savedWorkspace.createFolder(folder2.getId(), folder2.getName(), folder1.getId(), admin.getId());
        Assertions.assertTrue(savedWorkspace.isContains(folder2.getId()));
    }

    @Test
    public void createNewStaffWithPermissions() {
        var admin = User.create(UUID.randomUUID(), "TEST ADMIN");
        userRepository.save(admin);
        UUID newWorkspaceId = workspaceService.createWorkspace(admin.getId());
        Workspace workspace = workspaceService.get(newWorkspaceId);

        var staff = User.create(UUID.randomUUID(), "TEST STAFF 1");

        Assertions.assertThrows(EmployeeLimitReached.class, () -> {
            workspaceService.createStaff(workspace.getId(), staff.getName(), admin.getId());
        });

        workspaceService.setNewLimits(workspace.getId(), Limits.create(3L));
        var staffUUID = workspaceService.createStaff(workspace.getId(), staff.getName(), admin.getId());

        var savedWorker = userRepository.findByName(staff.getName());
        Assertions.assertEquals(savedWorker.get().getId(), staffUUID);



        Assertions.assertThrows(UserDoesntHavePermission.class, () -> {
            workspaceService.createFolder(workspace.getId(), "Folder 1", staffUUID);
        });
        workspaceService.setPermissions(workspace.getId(), staffUUID, Set.of(Action.AddFolder), admin.getId());

        var folderId = workspaceService.createFolder(workspace.getId(), "Folder 1", staffUUID);
        workspaceService.createFolder(workspace.getId(), folderId, "Folder 2", staffUUID);





//
//        Assertions.assertTrue(workspace.isContains(folder1.getId()));
//        workspace.createFolder(folder2.getId(), folder2.getName(), folder1.getId(), staffUUID);
//
//
//        Assertions.assertTrue(workspace.isContains(folder2.getId()));
    }

}
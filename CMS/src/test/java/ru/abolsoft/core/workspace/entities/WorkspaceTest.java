package ru.abolsoft.core.workspace.entities;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import ru.abolsoft.core.workspace.entities.Action;
import ru.abolsoft.core.workspace.entities.Folder;
import ru.abolsoft.core.workspace.entities.User;
import ru.abolsoft.core.workspace.entities.Workspace;
import ru.abolsoft.core.workspace.exceptions.UserDoesntHavePermission;

import java.util.Set;
import java.util.UUID;

@SpringBootTest
class WorkspaceTest {


//    @Test
    public void baseWorkspaceUsageTest() {
        var admin = User.create(UUID.randomUUID(), "ADMIN");
        var user = User.create(UUID.randomUUID(), "staff 1");
        var w = Workspace.createBy(UUID.randomUUID(), admin, null);

        var folder1 = Folder.create(UUID.randomUUID(), "Folder 1");
        var folder2 = Folder.create(UUID.randomUUID(), "Folder 2");


        Assertions.assertThrows(UserDoesntHavePermission.class, () -> {
            w.createFolder(folder1.getId(), folder1.getName(), user.getId());
        });

        w.createFolder(folder1.getId(), folder1.getName(), admin.getId());
        w.addPermissions(user.getId(), Set.of(Action.AddFolder), admin.getId());

        w.createFolder(folder2.getId(), folder2.getName(), folder1.getId(), user.getId());


        Assertions.assertThrows(UserDoesntHavePermission.class, () -> {
            w.delete(folder1, user.getId());
        });

        w.delete(folder1, admin.getId());
        w.delete(folder2, admin.getId());

        w.delete(folder2, user.getId());
    }
}
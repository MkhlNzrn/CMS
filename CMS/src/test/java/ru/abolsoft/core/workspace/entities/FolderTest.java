package ru.abolsoft.core.workspace.entities;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.abolsoft.core.workspace.entities.Action;
import ru.abolsoft.core.workspace.entities.Folder;
import ru.abolsoft.core.workspace.entities.User;
import ru.abolsoft.core.workspace.exceptions.CyclicRecursionDetected;
import ru.abolsoft.core.workspace.exceptions.UserDoesntHavePermission;
import ru.abolsoft.core.workspace.repositories.FolderRepository;

import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Transactional
class FolderTest {
    @Autowired
    FolderRepository folderRepository;

    @Test
    public void folderInsertTest() {
        var root = Folder.create(UUID.randomUUID(), "ROOT");
        var folder1 = Folder.create(UUID.randomUUID(), "Folder 1");
        var folder2 = Folder.create(UUID.randomUUID(), "Folder 2");
        var folder3 = Folder.create(UUID.randomUUID(), "Folder 3");
        var folder4 = Folder.create(UUID.randomUUID(), "Folder 4");

        root.addInnerFolder(folder1);
        root.addInnerFolder(folder2, folder1);

        Assertions.assertThrows(CyclicRecursionDetected.class, () -> {
            root.addInnerFolder(root);
        }, "Check base recursion protect");

        Assertions.assertThrows(CyclicRecursionDetected.class, () -> {
            folder2.addInnerFolder(folder3);
            folder3.addInnerFolder(folder4);
            folder4.addInnerFolder(root);
        }, "Test basic cyclic recursion protection");

    }

    @Test
    public void checkFolderLayerLevelTest() {
        var root = Folder.create(UUID.randomUUID(), "ROOT");
        var folder1 = Folder.create(UUID.randomUUID(), "Folder 1");
        var folder2 = Folder.create(UUID.randomUUID(), "Folder 2");
        var folder3 = Folder.create(UUID.randomUUID(), "Folder 3");
        var folder4 = Folder.create(UUID.randomUUID(), "Folder 4");

        root.addInnerFolder(folder1);
        root.addInnerFolder(folder2, folder1);
        root.addInnerFolder(folder3, folder2);
        root.addInnerFolder(folder4, folder3);

        Assertions.assertEquals(0, root.getLayerLevel());
        Assertions.assertEquals(1, folder1.getLayerLevel());
        Assertions.assertEquals(2, folder2.getLayerLevel());
        Assertions.assertEquals(3, folder3.getLayerLevel());
        Assertions.assertEquals(4, folder4.getLayerLevel());
    }

    @Test
    @Transactional
    public void checkFolderLayerLevelFromRepoTest() {
        var root = Folder.create(UUID.randomUUID(), "ROOT");
        var folder1 = Folder.create(UUID.randomUUID(), "Folder 1");
        var folder2 = Folder.create(UUID.randomUUID(), "Folder 2");
        var folder3 = Folder.create(UUID.randomUUID(), "Folder 3");
        var folder4 = Folder.create(UUID.randomUUID(), "Folder 4");

        root.addInnerFolder(folder1);
        root.addInnerFolder(folder2, folder1);
        root.addInnerFolder(folder3, folder2);
        root.addInnerFolder(folder4, folder3);

        folderRepository.save(root);
        Folder savedRoot = folderRepository.getById(root.getId());

        folder1 = savedRoot.getInnerFolders().stream().findFirst().get();
        folder2 = folder1.getInnerFolders().stream().findFirst().get();
        folder3 = folder2.getInnerFolders().stream().findFirst().get();
        folder4 = folder3.getInnerFolders().stream().findFirst().get();


        Assertions.assertEquals(0, savedRoot.getLayerLevel());
        Assertions.assertEquals(1, folder1.getLayerLevel());
        Assertions.assertEquals(2, folder2.getLayerLevel());
        Assertions.assertEquals(3, folder3.getLayerLevel());
        Assertions.assertEquals(4, folder4.getLayerLevel());
    }

    @Test
    public void permissionCheckTest() {
        var root = Folder.create(UUID.randomUUID(), "ROOT");
        var folder1 = Folder.create(UUID.randomUUID(), "Folder 1");
        var folder2 = Folder.create(UUID.randomUUID(), "Folder 2");
        var folder3 = Folder.create(UUID.randomUUID(), "Folder 3");
        var folder4 = Folder.create(UUID.randomUUID(), "Folder 4");

        var user = User.create(UUID.randomUUID(), "USER");

        Assertions.assertThrows(UserDoesntHavePermission.class, () -> {
            root.addInnerFolder(folder1, user.getId());
        }, "Check user permissions: AddFolder");

        root.addInnerFolder(folder1);
        root.addPermissionsRecursive(user.getId(), Set.of(Action.AddFolder));
        root.addInnerFolder(folder2, folder1, user.getId());
        root.addPermissions(user.getId(), folder2.getId(), Set.of(Action.AddFolder));


        Assertions.assertTrue(() -> folder2.getPermissions().stream()
                .filter(permission -> permission.getPermissionId().getAction().equals(Action.AddFolder))
                .anyMatch(permission -> permission.getUserId().equals(user.getId())));


        root.addInnerFolder(folder3, folder1);
        root.addInnerFolder(folder3, folder1);



        root.addPermissions(user.getId(), Set.of(Action.Delete));

        Assertions.assertThrows(UserDoesntHavePermission.class, () -> {
            root.deleteFolder(folder3, user.getId());
        }, "Should throw because user only has root permissions");

        root.safeDeleteFolder(folder1, user.getId());


        root.addPermissions(user.getId(), folder3.getId(), Set.of(Action.Delete));
        root.deleteFolder(folder3, user.getId());

    }
}
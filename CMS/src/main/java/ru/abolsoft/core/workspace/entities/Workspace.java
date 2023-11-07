package ru.abolsoft.core.workspace.entities;



import lombok.*;
import org.jetbrains.annotations.NotNull;


import jakarta.persistence.*;
import ru.abolsoft.core.common.entities.BaseAggregateRoot;
import ru.abolsoft.core.workspace.exceptions.EmployeeLimitReached;
import ru.abolsoft.core.workspace.exceptions.UserDoesntHavePermission;
import ru.abolsoft.core.workspace.exceptions.WorkerDoesNotExist;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity(name = "WorkspaceWorkspace")
@Table(name = "workspaces")
@Getter // Todo: make protected
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace extends BaseAggregateRoot<UUID> {

    private String name;

    @OneToOne
    @NotNull
    private User admin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "workspace_staff",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> staff;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Folder rootFolder;

//    Todo: impl func for configure this
    @Transient
    private Set<Action> defaultOwnerPermissions = Set.of(Action.values());

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "id")
    private Limits limits;


    @NotNull
    public static Workspace createBy(UUID uuid, @NotNull User admin, @NotNull Limits limits) {
        var w = new Workspace();
        w.setId(uuid);
        w.setName("WS by " + admin.getName());
        w.setAdmin(admin);
        w.setRootFolder(Folder.create(UUID.randomUUID(), w.getName()));
        w.setStaff(Set.of(admin));
        w.addAllPermissions(admin.getId());
        limits.setWorkspaceId(uuid);
        w.setLimits(limits);
        return w;
    }


    private void addFolder(Folder folder, User user) {
        if (!user.equals(admin))
            folder.addAllPermissions(admin.getId());
        rootFolder.addInnerFolder(folder, user.getId());
        rootFolder.addPermissions(user.getId(), folder.getId(), defaultOwnerPermissions);
    }

    private void addFolder(Folder folder, Folder target, User user) {
        if (!user.equals(admin))
            folder.addAllPermissions(admin.getId());
        rootFolder.addInnerFolder(folder, target, user.getId());
        rootFolder.addPermissions(user.getId(), folder.getId(), defaultOwnerPermissions);
    }

    public void addAllPermissions(UUID userId) {
        rootFolder.addAllPermissions(userId);
    }


    private void addPermissions(UUID userId, Set<Action> actions) {
        rootFolder.addPermissions(userId, actions);
    }

    public void setPermissions(UUID userId, Set<Action> actions, UUID doerId) {
        setPermissions(userId, actions, rootFolder.getId(), doerId);
    }

    public void setPermissions(UUID userId, Set<Action> actions, UUID folderId, UUID doerId) {
        if (!admin.getId().equals(doerId))
            throw new UserDoesntHavePermission();
        User user = this.findStaffOrThrow(userId);
        rootFolder.setPermissions(user.getId(), actions, folderId, doerId);
    }
    public void addPermissions(UUID userId, Set<Action> actions, UUID doerId) {
        if (!admin.getId().equals(doerId))
            throw new UserDoesntHavePermission();
        Optional<User> user = this.findStaff(userId);
        if (user.isEmpty())
            throw new WorkerDoesNotExist(this.getId(), userId);
        addPermissions(userId, actions);
    }

    @NotNull
    private Optional<User> findStaff(UUID userId) {
        return this.staff.stream().filter(user -> user.getId().equals(userId)).findFirst();
    }

    @NotNull
    private User findStaffOrThrow(UUID userId) {
        Optional<User> user = this.findStaff(userId);
        if (user.isEmpty())
            throw new WorkerDoesNotExist(this.getId(), userId);
        return user.get();
    }

    public void delete(Folder target, UUID userId) {
        rootFolder.deleteFolder(target, userId);
    }

    public void safeDelete(Folder target, UUID userId) {
        rootFolder.safeDeleteFolder(target, userId);
    }

    public UUID createFolder(UUID newFolderId, String newFolderName, UUID doerId) {
        Folder f = Folder.create(newFolderId, newFolderName);
        User user = findStaffOrThrow(doerId);
        addFolder(f, user);
        return f.getId();
    }



    public void createFolder(UUID newFolderId, String newFolderName, UUID innerFolderId, UUID doerId) {
        Folder f = Folder.create(newFolderId, newFolderName);
        User user = findStaffOrThrow(doerId);
        Folder innerFolder =  rootFolder.searchOrThrow(innerFolderId);
        addFolder(f, innerFolder, user);
    }

    public boolean isContains(UUID folderId) {
        return rootFolder.search(folderId).isPresent();
    }
    public User createStaff(UUID userId, String userName, UUID doerId) {
        if (!admin.getId().equals(doerId))
            throw new UserDoesntHavePermission();
        if (limits.getStaffCount() <= this.staff.size())
            throw new EmployeeLimitReached();

        var u = User.create(userId, userName);
        this.staff.add(u);
        return u;
    }


    public void updateLimits(Limits limits) {
        limits.setWorkspaceId(this.getId());
        this.setLimits(limits);
    }

    public void addStaff(User user) {
        staff.add(user);
    }
}


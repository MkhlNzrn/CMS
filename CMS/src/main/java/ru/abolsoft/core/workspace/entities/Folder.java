package ru.abolsoft.core.workspace.entities;


import jakarta.persistence.*;
import lombok.*;
import ru.abolsoft.core.common.entities.BaseEntity;
import ru.abolsoft.core.common.exceptions.DomainException;
import ru.abolsoft.core.workspace.exceptions.CyclicRecursionDetected;
import ru.abolsoft.core.workspace.exceptions.FolderNotFound;
import ru.abolsoft.core.workspace.exceptions.UserDoesntHavePermission;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "folders")
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder extends BaseEntity<UUID> {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "folder_relationships",
            joinColumns = @JoinColumn(name = "parent_id"),
                inverseJoinColumns = @JoinColumn(name = "child_id"))
    private Set<Folder> innerFolders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "folder_id")
    private Set<Photo> photos;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "folder_id")
    private Set<Permission> permissions;

    @JoinColumn(name = "layer_level")
    private Integer layerLevel = 0;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "folder_relationships", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "parent_id"))
    private Folder parent;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "is_visible")
    private boolean isVisible = true;

    public static Folder create(UUID uuid, String name) {
        var f = new Folder();
        f.setId(uuid);
        f.setName(name);
        f.setInnerFolders(new HashSet<>());
        f.setPhotos(new HashSet<>());
        f.setPermissions(new HashSet<>());
        return f;
    }

    public void addInnerFolder( Folder folder) {
        if (getId().equals(folder.getId())) throw new CyclicRecursionDetected();
        if (folder.search(this).isPresent()) throw new CyclicRecursionDetected();
        folder.setLayerLevel(getLayerLevel() + 1);
        innerFolders.add(folder);
    }

    public void addInnerFolder(Folder folder, Folder target) {
        if (folder.equals(target)) throw new DomainException.NotImplemented();
        Optional<Folder> oT = this.search(target);
        if (oT.isEmpty()) throw new DomainException.NotImplemented();
        Folder t = oT.get();
        t.addInnerFolder(folder);
    }

    private Optional<Folder> search( Folder target) {
        if (target.equals(this)) return Optional.of(this);

        Optional<Optional<Folder>> res = innerFolders.stream()
                .map(folder -> folder.search(target)).filter(Optional::isPresent).findFirst();

        return res.orElseGet(Optional::empty);
    }

    protected Optional<Folder> search( UUID targetId) {
        if (targetId.equals(this.id)) return Optional.of(this);

        Optional<Optional<Folder>> res = innerFolders.stream()
                .map(folder -> folder.search(targetId)).filter(Optional::isPresent).findFirst();

        return res.orElseGet(Optional::empty);
    }


    private Folder searchOrThrow( Folder target) {
        Optional<Folder> oT = this.search(target);
        if (oT.isEmpty()) throw new DomainException.NotImplemented();
        return oT.get();
    }


    protected Folder searchOrThrow( UUID targetId) {
        Optional<Folder> oT = this.search(targetId);
        if (oT.isEmpty()) throw new DomainException.NotImplemented();
        return oT.get();
    }


    public void addPhoto(Photo photo) {
        if (photos.contains(photo)) throw new DomainException.NotImplemented();
        photos.add(photo);
    }

    public void addPhotoIn(Photo photo, Folder target) {
        this.searchOrThrow(target).addPhoto(photo);
    }

    public void addInnerFolder(Folder folder, UUID userId) {
        if (permissions.stream().noneMatch(permission -> permission.isAllow(userId, Action.AddFolder)))
            throw new UserDoesntHavePermission();
        addInnerFolder(folder);
    }

    public void addInnerFolder(Folder folder, Folder target, UUID userId) {
        this.searchOrThrow(target).addInnerFolder(folder, userId);
    }

    protected void addAllPermissions(UUID userId) {
        var p = Arrays.stream(Action.values())
                .map(action -> Permission.create(userId, this, true, action))
                .collect(Collectors.toSet());
        permissions.addAll(p);
    }

    public void addPermissionsRecursive(UUID userId, Set<Action> actions) {
        var p = actions.stream()
                .map(action -> Permission.create(userId, this, true, action))
                .collect(Collectors.toSet());
        innerFolders.forEach(folder -> folder.addPermissionsRecursive(userId, actions));
        permissions.addAll(p);
    }

    public void addPermissions(UUID userId, Set<Action> actions) {
        var p = actions.stream()
                .map(action -> Permission.create(userId, this, true, action))
                .collect(Collectors.toSet());
        permissions.addAll(p);
    }
    public void addPermissions(UUID userId, UUID targetId, Set<Action> actions) {
        searchOrThrow(targetId).addPermissions(userId, actions);
    }

    public void addPhoto(Photo photo, UUID userId) {
        isAllowedOrTrow(userId, Action.AddPhoto);
        addPhoto(photo);
    }

    private boolean isAllowed(UUID userId, Action action) {
        return permissions.stream().anyMatch(permission -> permission.isAllow(userId, action));
    }

    private void isAllowedOrTrow(UUID userId, Action action) {
        if (isAllowed(userId, action))
            return;
        throw new UserDoesntHavePermission();
    }

    public void addPhotoIn(Photo photo, Folder target, UUID userId) {
        var t = this.searchOrThrow(target);
        t.addPhoto(photo, userId);
    }


    private void delete(UUID userId) {
        isAllowedOrTrow(userId, Action.Delete);
        setDeleted(true);
        photos.forEach(Photo::delete);
    }

    private void safeDelete(UUID userId) {
        if (!isAllowed(userId, Action.Delete)) return;
        delete(userId);
        innerFolders.forEach(folder -> folder.safeDelete(userId));
    }

    public void deleteFolder(Folder target, UUID userId) {
        var t = search(target);
        if (t.isEmpty()) throw new FolderNotFound();
        t.get().delete(userId);
    }
    public void safeDeleteFolder(Folder target, UUID userId) {
        var t = search(target);
        if (t.isEmpty()) throw new FolderNotFound();
        t.get().safeDelete(userId);
    }


    private void setPermissions(UUID userId, Set<Action> actions, UUID doerId) {
        isAllowedOrTrow(doerId, Action.SetPermissions);
        permissions.stream()
                .filter(permission -> permission.getUserId().equals(userId))
                .toList().forEach(permissions::remove);

        var p = actions.stream()
                .map(action -> Permission.create(userId, this, true, action))
                .collect(Collectors.toSet());
        permissions.addAll(p);
    }

    public void setPermissions(UUID userId, Set<Action> actions, UUID folderId, UUID doerId) {
        searchOrThrow(folderId).setPermissions(userId, actions, doerId);
    }
}

package ru.abolsoft.core.workspace.entities;

import lombok.*;
import org.jetbrains.annotations.NotNull;


import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "permissions")
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission {
    @EmbeddedId
    private PermissionId permissionId;
    @Column(name = "is_allowed")
    private boolean isAllowed;

    @NotNull
    public static Permission create(@NotNull UUID userId, @NotNull Folder folder, boolean isAllowed, Action action) {
        var p = new Permission();
        var pid = new PermissionId();
        pid.setUserId(userId);
        pid.setFolderId(folder.getId());
        pid.setAction(action);
        p.setPermissionId(pid);
        p.setAllowed(isAllowed);
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId);
    }

    private boolean isAllow(@NotNull UUID userId) {
        return permissionId.getUserId().equals(userId);
    }

    public boolean isAllow(@NotNull UUID userId, Action action) {
        return this.getPermissionId().getAction().equals(action) && isAllow(userId);
    }


    public UUID getUserId() {
        return this.permissionId.getUserId();
    }
}


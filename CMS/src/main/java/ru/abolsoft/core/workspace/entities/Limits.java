package ru.abolsoft.core.workspace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;


@Entity(name = "WorkspaceLimits")
@Table(name = "workspace_limits")
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//TODO: mark this like entity or value object
public class Limits {
    @Id
    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "staff_count")
    private Long staffCount;

    public static Limits create(Long staffCount) {
        var l = new Limits();
        l.setStaffCount(staffCount);
        return l;
    }
}

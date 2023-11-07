package ru.abolsoft.core.workspace.entities;

import lombok.*;


import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PermissionId implements Serializable {
    private static final long serialVersionUID = 7794185397134384074L;
    @EqualsAndHashCode.Include
    @Column(name = "user_id")
    private UUID userId;

    @EqualsAndHashCode.Include
    @Column(name = "folder_id")
    private UUID folderId;

    @EqualsAndHashCode.Include
    @Enumerated(EnumType.ORDINAL)
    private Action action;
}

package ru.abolsoft.core.workspace.entities;

import lombok.*;
import org.jetbrains.annotations.NotNull;


import jakarta.persistence.*;
import ru.abolsoft.core.common.entities.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity<UUID> {
    private String name;
    @NotNull
    public static User create(UUID id, String name) {
        var u = new User();
        u.setId(id);
        u.setName(name);
        return u;
    }
}



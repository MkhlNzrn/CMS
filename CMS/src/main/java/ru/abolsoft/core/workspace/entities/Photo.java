package ru.abolsoft.core.workspace.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.abolsoft.core.common.entities.BaseEntity;

@Entity
@Table(name = "photos")
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity<Long> {



    @Column(name = "is_deleted")
    private boolean isDeleted;

    @NotNull
    @Contract(" -> new")
    public static Photo create() {
        return new Photo();
    }

    public void delete() {
        isDeleted = true;
    }
}

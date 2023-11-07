package ru.abolsoft.core.workspace.entities;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString // TODO: remove me !!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum Action {
    AddFolder,
    AddPhoto,
    Delete,
    ChangePrice,
    ChangeData,
    SetPermissions,
}

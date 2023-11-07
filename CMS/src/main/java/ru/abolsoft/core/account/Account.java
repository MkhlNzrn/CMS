package ru.abolsoft.core.account;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import ru.abolsoft.core.account.events.AccountCreatedEvent;
import ru.abolsoft.core.common.entities.BaseAggregateRoot;
import ru.abolsoft.core.common.exceptions.DomainException;

import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseAggregateRoot<UUID> {

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @Transient
    private Password password;



    public static @NotNull Account create(UUID id, String email, String name) {
        var a = new Account();
        a.setId(id);
        a.setEmail(email);
        a.setPassword(Password.generate());
        a.setName(name);
        a.registerEvent(AccountCreatedEvent.of(a));
        return a;
    }


    public void resetPassword() {
        throw new DomainException.NotImplemented();
    }

    public void changePassword(String password) {
        throw new DomainException.NotImplemented();
    }

    public void changeEmail(String email) {
        throw new DomainException.NotImplemented();
    }

    public boolean checkPassword(@NotNull String password) {
        return password.equals(this.getPassword());
    }
}

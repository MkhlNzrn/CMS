package ru.abolsoft.core.account.events;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.abolsoft.core.account.Account;
import ru.abolsoft.core.common.DomainEvent;

import java.util.UUID;


public class AccountCreatedEvent extends DomainEvent<UUID, Account> {

    protected AccountCreatedEvent(@NotNull Account aggregate) {
        super(aggregate);
    }

    @Contract("_ -> new")
    public static @NotNull AccountCreatedEvent of(Account a) {
        return new AccountCreatedEvent(a);
    }
}

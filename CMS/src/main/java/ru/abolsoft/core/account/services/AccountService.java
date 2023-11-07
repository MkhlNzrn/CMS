package ru.abolsoft.core.account.services;

import ru.abolsoft.core.account.Account;
import ru.abolsoft.infr.api.dtos.CreateAccountDto;

import java.util.UUID;

public interface AccountService {
    UUID createAccount(String name, String email);
}

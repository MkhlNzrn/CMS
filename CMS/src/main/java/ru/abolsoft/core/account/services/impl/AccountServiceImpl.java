package ru.abolsoft.core.account.services.impl;

import org.springframework.stereotype.Service;
import ru.abolsoft.core.account.Account;
import ru.abolsoft.core.account.services.AccountService;
import ru.abolsoft.core.common.UUIDGenerator;
import ru.abolsoft.infr.api.dtos.CreateAccountDto;

import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public UUID createAccount(String name, String email) {
        return Account.create(UUIDGenerator.generateUUIDv7(),
                email,
                name).getId();
    }
}

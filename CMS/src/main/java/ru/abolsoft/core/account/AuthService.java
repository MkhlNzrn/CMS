package ru.abolsoft.core.account;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.abolsoft.core.common.exceptions.DomainException;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final EmailSender emailSender;

    public AuthService(AccountRepository accountRepository, EmailSender emailSender ) {
        this.accountRepository = accountRepository;
        this.emailSender = emailSender;
    }

    public UUID auth(String email, String password) {
        Optional<Account> a = accountRepository.findByEmail(email);
        if (a.isEmpty()) throw new DomainException.NotImplemented();

        var account = a.get();

        if (!account.checkPassword(password)) {
            throw new DomainException.NotImplemented();
        }

        return account.getId();
    }


    public UUID auth(String email) {
        Optional<Account> a = accountRepository.findByEmail(email);
        if (a.isEmpty()) throw new DomainException.NotImplemented();
        var account = a.get();
        return account.getId();
    }
    @Transactional
    public UUID registration(String email, String name) {
        var isAlreadyExist = accountRepository.existByEmail(email);
        if (isAlreadyExist) throw new DomainException.NotImplemented();

        var id = UUID.randomUUID();
        var a = Account.create(id, email, name);
        accountRepository.save(a);

        emailSender.sendSuccessRegistrationEmail(email);
        return a.getId();
    }
}

package ru.abolsoft.core.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.abolsoft.core.common.repositories.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends BaseRepository<Account, UUID> {
    @Query("select (count(a) > 0) from Account a where upper(a.email) = upper(:email)")
    boolean existByEmail(@Param("email") String email);

    @Query(value = "select a from Account a where a.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);
}
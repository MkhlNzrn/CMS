package ru.abolsoft.core.workspace;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.abolsoft.core.account.events.AccountCreatedEvent;
import ru.abolsoft.core.workspace.services.WorkspaceService;

@Component
@Slf4j
public class EventHandler {

    private final WorkspaceService workspaceService;

    public EventHandler(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onMyDomainEvent(@NotNull AccountCreatedEvent event) {
        var uuid = workspaceService.createWorkspace(event.getAggregateId());
    }
}

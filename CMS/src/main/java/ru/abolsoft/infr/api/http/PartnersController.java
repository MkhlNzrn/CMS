package ru.abolsoft.infr.api.http;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.abolsoft.core.account.services.AccountService;
import ru.abolsoft.core.common.exceptions.NotImplementedException;
import ru.abolsoft.core.partner.Partner;
import ru.abolsoft.core.workspace.services.WorkspaceService;
import ru.abolsoft.infr.api.dtos.CreateAccountDto;
import ru.abolsoft.infr.services.PartnerService;

import java.util.UUID;

@RequestMapping("partners")
public class PartnersController {
    private final PartnerService partnerService;
    private final WorkspaceService workspaceService;
    private final AccountService accountService;

    public PartnersController(PartnerService partnerService, WorkspaceService workspaceService, AccountService accountService) {
        this.partnerService = partnerService;
        this.workspaceService = workspaceService;
        this.accountService = accountService;
    }

    @GetMapping
    public void getAllPartners() {
        throw new NotImplementedException();
    }

    @PostMapping
    public void registerNewPartner(@RequestBody CreateAccountDto createAccountDto){
        Partner partner = partnerService.createPartner(createAccountDto.getContractId(), createAccountDto.getGenDirFullName(), createAccountDto.getInn(), createAccountDto.getOrgName());
        UUID accountUUID = accountService.createAccount(createAccountDto.getName(), createAccountDto.getEmail());
        UUID workspaceUUID = workspaceService.createWorkspace(accountUUID);
        partner.setWorkspaceId(workspaceUUID);

    }

    @PostMapping
    public void activatePartner(){
        throw new NotImplementedException();
    }
}

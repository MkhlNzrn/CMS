package ru.abolsoft.infr.services;

import ru.abolsoft.core.partner.Partner;
import ru.abolsoft.infr.api.dtos.CreateAccountDto;

public interface PartnerService {
    Partner createPartner(String contractId, String genDirFullName, String inn, String orgName);
}

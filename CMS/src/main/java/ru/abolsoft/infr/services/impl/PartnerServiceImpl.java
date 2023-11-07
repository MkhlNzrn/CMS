package ru.abolsoft.infr.services.impl;


import ru.abolsoft.core.common.UUIDGenerator;
import ru.abolsoft.core.partner.Partner;
import ru.abolsoft.core.partner.repositories.PartnerRepository;
import ru.abolsoft.infr.services.PartnerService;
import ru.abolsoft.infr.api.dtos.CreateAccountDto;

public class PartnerServiceImpl implements PartnerService {
    private final PartnerRepository partnerRepository;

    public PartnerServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public Partner createPartner(String contractId, String genDirFullName, String inn, String orgName) {
        Partner partner = new Partner(contractId,
                genDirFullName,
                inn,
                orgName, true);
        partnerRepository.save(partner);
        return partner;
    }
}

package ru.abolsoft.core.partner;

import jakarta.persistence.*;
import lombok.*;
import ru.abolsoft.core.common.entities.BaseEntity;

import java.util.UUID;


@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner extends BaseEntity<UUID> {

    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "gen_dir_full_name")
    private String genDirFullName;

    @Column(name = "inn")
    private String inn;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "is_contract_active")
    private boolean isContractActive;

    @Column(name = "workspace_id")
    private UUID workspaceId;

    public Partner(String contractId, String genDirFullName, String inn, String orgName, boolean isContractActive) {
        this.contractId = contractId;
        this.genDirFullName = genDirFullName;
        this.inn = inn;
        this.orgName = orgName;
        this.isContractActive = isContractActive;
    }
}

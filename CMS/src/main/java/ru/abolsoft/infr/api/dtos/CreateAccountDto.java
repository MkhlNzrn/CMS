package ru.abolsoft.infr.api.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateAccountDto {
    private String contractId;
    private String genDirFullName;
    private String inn;
    private String orgName;
    private String name;
    private String email;
}

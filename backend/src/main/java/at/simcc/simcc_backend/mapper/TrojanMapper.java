package at.simcc.simcc_backend.mapper;

import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.models.TrojanPlainDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrojanMapper {
    Trojan toEntity(TrojanPlainDto trojanPlainDto);

    TrojanPlainDto toDto(Trojan trojan);
}
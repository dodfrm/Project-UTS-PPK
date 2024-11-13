package stis.contactapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import stis.contactapi.dto.ContactOrganizationDto;
import stis.contactapi.entity.ContactOrganization;

@Mapper(componentModel = "spring")
public interface ContactOrganizationMapper {

    @Mapping(source = "contactId.id", target = "contactId")
    @Mapping(source = "organizationId.id", target = "organizationId")
    ContactOrganizationDto toDto(ContactOrganization entity);

    @Mapping(source = "contactId", target = "contactId.id")
    @Mapping(source = "organizationId", target = "organizationId.id")
    ContactOrganization toEntity(ContactOrganizationDto dto);
}
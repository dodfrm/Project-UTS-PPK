package stis.contactapi.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import stis.contactapi.dto.ContactDto;
import stis.contactapi.dto.OrganizationDto;
import stis.contactapi.entity.ContactOrganization;
import stis.contactapi.entity.Organization;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "contacts", source = "contactOrganizations", qualifiedByName = "mapContacts")
    OrganizationDto toDto(Organization organization);

    Organization toEntity(OrganizationDto organizationDto);

    @Named("mapContacts")
    default List<ContactDto> mapContacts(Set<ContactOrganization> contactOrganizations) {
        if (contactOrganizations == null) {
            return null;
        }
        return contactOrganizations.stream()
                .map(contactOrganization -> new ContactDto(
                        contactOrganization.getContactId().getId(),
                        contactOrganization.getContactId().getFullName(),
                        contactOrganization.getContactId().getPhone(),
                        contactOrganization.getContactId().getEmail(),
                        contactOrganization.getContactId().getContactType(),
                        null, // contactOrganizations jika diperlukan
                        null // contactSubjects jika diperlukan
                ))
                .collect(Collectors.toList());
    }
}
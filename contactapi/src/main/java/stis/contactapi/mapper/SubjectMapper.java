package stis.contactapi.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import stis.contactapi.dto.ContactDto;
import stis.contactapi.dto.SubjectDto;
import stis.contactapi.entity.ContactSubject;
import stis.contactapi.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    @Mapping(target = "contacts", source = "contactSubjects", qualifiedByName = "mapContacts")
    SubjectDto toDto(Subject subject);

    Subject toEntity(SubjectDto subjectDto);

    @Named("mapContacts")
    default List<ContactDto> mapContacts(Set<ContactSubject> contactSubjects) {
        if (contactSubjects == null) {
            return null;
        }
        return contactSubjects.stream()
                .map(contactSubject -> new ContactDto(
                        contactSubject.getContact().getId(),
                        contactSubject.getContact().getFullName(),
                        contactSubject.getContact().getPhone(),
                        contactSubject.getContact().getEmail(),
                        contactSubject.getContact().getContactType(),
                        null, // contactOrganizations
                        null // contactSubjects
                ))
                .collect(Collectors.toList());
    }
}

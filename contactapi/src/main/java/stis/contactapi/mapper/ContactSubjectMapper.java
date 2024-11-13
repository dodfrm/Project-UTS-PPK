package stis.contactapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import stis.contactapi.dto.ContactSubjectDto;
import stis.contactapi.entity.ContactSubject;

@Mapper
public interface ContactSubjectMapper {
    ContactSubjectMapper INSTANCE = Mappers.getMapper(ContactSubjectMapper.class);

    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "subject.id", target = "subjectId")
    ContactSubjectDto toDto(ContactSubject entity);

    @Mapping(source = "contactId", target = "contact.id")
    @Mapping(source = "subjectId", target = "subject.id")
    ContactSubject toEntity(ContactSubjectDto dto);
}

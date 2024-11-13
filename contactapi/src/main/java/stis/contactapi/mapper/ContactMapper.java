package stis.contactapi.mapper;

import org.mapstruct.Mapper;

import stis.contactapi.dto.ContactDto;
import stis.contactapi.entity.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    Contact toEntity(ContactDto contactDto);

    ContactDto toDto(Contact contact);
}
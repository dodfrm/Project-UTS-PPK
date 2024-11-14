package stis.contactapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stis.contactapi.dto.ContactDto;
import stis.contactapi.entity.Contact;
import stis.contactapi.entity.ECType;
import stis.contactapi.entity.EJabatan;
import stis.contactapi.mapper.ContactMapper;
import stis.contactapi.repository.ContactRepository;
import stis.contactapi.repository.ContactSpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    // Get all contacts
    @Transactional(readOnly = true)
    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    // Search for contacts
    @Transactional(readOnly = true)
    public List<ContactDto> searchContacts(String fullName, EJabatan jabatan, String email, ECType contactType,
            String organizationName, String subjectName) {
        Specification<Contact> specification = Specification.where(ContactSpecification.hasFullName(fullName))
                .and(ContactSpecification.hasJabatan(jabatan))
                .and(ContactSpecification.hasEmail(email))
                .and(ContactSpecification.hasContactType(contactType))
                .and(ContactSpecification.hasOrganizationName(organizationName))
                .and(ContactSpecification.hasSubjectName(subjectName));

        return contactRepository.findAll(specification).stream()
                .map(contactMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get contact by ID
    @Transactional(readOnly = true)
    public Optional<ContactDto> getContactById(Long id) {
        return contactRepository.findById(id)
                .map(contactMapper::toDto);
    }

    // Create a new contact
    @Transactional
    public ContactDto createContact(ContactDto contactDto) {
        Contact contact = contactMapper.toEntity(contactDto);
        contact = contactRepository.save(contact);
        return contactMapper.toDto(contact);
    }

    // Update an existing contact
    @Transactional
    public Optional<ContactDto> updateContact(Long id, ContactDto contactDto) {
        return contactRepository.findById(id)
                .map(existingContact -> {
                    Contact updatedContact = contactMapper.toEntity(contactDto);
                    updatedContact.setId(id);
                    updatedContact = contactRepository.save(updatedContact);
                    return contactMapper.toDto(updatedContact);
                });
    }

    // Delete a contact
    @Transactional
    public boolean deleteContact(Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

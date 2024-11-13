package stis.contactapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import stis.contactapi.dto.ContactOrganizationDto;
import stis.contactapi.entity.Contact;
import stis.contactapi.entity.ContactOrganization;
import stis.contactapi.entity.ECType;
import stis.contactapi.mapper.ContactOrganizationMapper;
import stis.contactapi.repository.ContactOrganizationRepository;
import stis.contactapi.repository.ContactRepository;

@Service
public class ContactOrganizationService {
    @Autowired
    private ContactOrganizationRepository contactOrganizationRepository;

    @Autowired
    private ContactOrganizationMapper contactOrganizationMapper;

    @Autowired
    private ContactRepository contactRepository;

    @Transactional(readOnly = true)
    public List<ContactOrganizationDto> getAllContactOrganizations() {
        return contactOrganizationRepository.findAll().stream()
                .map(contactOrganizationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ContactOrganizationDto> getContactOrganizationById(Long id) {
        return contactOrganizationRepository.findById(id)
                .map(contactOrganizationMapper::toDto);
    }

    @Transactional
    public ContactOrganizationDto createContactOrganization(ContactOrganizationDto contactOrganizationDto) {
        // Check if the contact exists and has a contactType of MAHASISWA
        Contact contact = contactRepository.findById(contactOrganizationDto.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        if (contact.getContactType() != ECType.MAHASISWA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only contacts with contactType 'MAHASISWA' can be associated with an organization.");
        }

        // Proceed to create the ContactOrganization if the type is MAHASISWA
        ContactOrganization contactOrganization = contactOrganizationMapper.toEntity(contactOrganizationDto);
        contactOrganization = contactOrganizationRepository.save(contactOrganization);
        return contactOrganizationMapper.toDto(contactOrganization);
    }

    @Transactional
    public Optional<ContactOrganizationDto> updateContactOrganization(Long id,
            ContactOrganizationDto contactOrganizationDto) {
        return contactOrganizationRepository.findById(id)
                .map(existingContactOrg -> {
                    // Check if the contact exists and has a contactType of MAHASISWA
                    Contact contact = contactRepository.findById(contactOrganizationDto.getContactId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

                    if (contact.getContactType() != ECType.MAHASISWA) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Only contacts with contactType 'MAHASISWA' can be associated with an organization.");
                    }
                    ContactOrganization updatedContactOrg = contactOrganizationMapper.toEntity(contactOrganizationDto);
                    updatedContactOrg.setId(id);
                    updatedContactOrg = contactOrganizationRepository.save(updatedContactOrg);
                    return contactOrganizationMapper.toDto(updatedContactOrg);
                });
    }

    @Transactional
    public boolean deleteContactOrganization(Long id) {
        if (contactOrganizationRepository.existsById(id)) {
            contactOrganizationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

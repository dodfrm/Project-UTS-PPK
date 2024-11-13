package stis.contactapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import stis.contactapi.dto.ContactSubjectDto;
import stis.contactapi.entity.Contact;
import stis.contactapi.entity.ContactSubject;
import stis.contactapi.entity.ECType;
import stis.contactapi.mapper.ContactSubjectMapper;
import stis.contactapi.repository.ContactRepository;
import stis.contactapi.repository.ContactSubjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactSubjectService {

    @Autowired
    private ContactSubjectRepository contactSubjectRepository;

    @Autowired
    private ContactRepository contactRepository;

    private final ContactSubjectMapper contactSubjectMapper = ContactSubjectMapper.INSTANCE;

    @Transactional(readOnly = true)
    public List<ContactSubjectDto> getAllContactSubjects() {
        return contactSubjectRepository.findAll().stream()
                .map(contactSubjectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ContactSubjectDto> getContactSubjectById(Long id) {
        return contactSubjectRepository.findById(id)
                .map(contactSubjectMapper::toDto);
    }

    @Transactional
    public ContactSubjectDto createContactSubject(ContactSubjectDto contactSubjectDto) {
        // Check if the contact exists and has a contactType of DOSEN
        Contact contact = contactRepository.findById(contactSubjectDto.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        if (contact.getContactType() != ECType.DOSEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only contacts with contactType 'DOSEN' can be associated with an Subject.");
        }
        ContactSubject contactSubject = contactSubjectMapper.toEntity(contactSubjectDto);
        contactSubject = contactSubjectRepository.save(contactSubject);
        return contactSubjectMapper.toDto(contactSubject);
    }

    @Transactional
    public Optional<ContactSubjectDto> updateContactSubject(Long id, ContactSubjectDto contactSubjectDto) {
        return contactSubjectRepository.findById(id)
                .map(existingContactSubject -> {
                    // Check if the contact exists and has a contactType of DOSEN
                    Contact contact = contactRepository.findById(contactSubjectDto.getContactId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

                    if (contact.getContactType() != ECType.DOSEN) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Only contacts with contactType 'DOSEN' can be associated with an Subject.");
                    }
                    ContactSubject updatedContactSubject = contactSubjectMapper.toEntity(contactSubjectDto);
                    updatedContactSubject.setId(id);
                    updatedContactSubject = contactSubjectRepository.save(updatedContactSubject);
                    return contactSubjectMapper.toDto(updatedContactSubject);
                });
    }

    @Transactional
    public boolean deleteContactSubject(Long id) {
        if (contactSubjectRepository.existsById(id)) {
            contactSubjectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

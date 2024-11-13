package stis.contactapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stis.contactapi.dto.SubjectDto;
import stis.contactapi.entity.Subject;
import stis.contactapi.mapper.SubjectMapper;
import stis.contactapi.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectMapper subjectMapper;

    @Transactional(readOnly = true)
    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SubjectDto> getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .map(subjectMapper::toDto);
    }

    @Transactional
    public SubjectDto createSubject(SubjectDto subjectDto) {
        Subject subject = subjectMapper.toEntity(subjectDto);
        subject = subjectRepository.save(subject);
        return subjectMapper.toDto(subject);
    }

    @Transactional
    public Optional<SubjectDto> updateSubject(Long id, SubjectDto subjectDto) {
        return subjectRepository.findById(id)
                .map(existingSubject -> {
                    Subject updatedSubject = subjectMapper.toEntity(subjectDto);
                    updatedSubject.setId(id);
                    updatedSubject = subjectRepository.save(updatedSubject);
                    return subjectMapper.toDto(updatedSubject);
                });
    }

    @Transactional
    public boolean deleteSubject(Long id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

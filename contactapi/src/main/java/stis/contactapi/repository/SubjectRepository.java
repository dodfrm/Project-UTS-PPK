package stis.contactapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import stis.contactapi.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @EntityGraph(attributePaths = { "contactSubjects.contact" })
    Optional<Subject> findById(Long id);
}

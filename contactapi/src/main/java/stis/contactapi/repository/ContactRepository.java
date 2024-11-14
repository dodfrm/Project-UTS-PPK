package stis.contactapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import stis.contactapi.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    @EntityGraph(attributePaths = { "contactOrganizations.organizationId", "contactSubjects.subject" })
    List<Contact> findAll();

    @EntityGraph(attributePaths = { "contactOrganizations.organizationId", "contactSubjects.subject" })
    Optional<Contact> findById(Long id);
}

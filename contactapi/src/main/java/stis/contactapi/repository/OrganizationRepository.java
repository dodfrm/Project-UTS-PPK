package stis.contactapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import stis.contactapi.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @EntityGraph(attributePaths = { "contact", "organization", "contactOrganizations.contact" })
    Optional<Organization> findById(Long id);
}

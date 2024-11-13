package stis.contactapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import stis.contactapi.entity.ContactOrganization;

public interface ContactOrganizationRepository extends JpaRepository<ContactOrganization, Long> {

}

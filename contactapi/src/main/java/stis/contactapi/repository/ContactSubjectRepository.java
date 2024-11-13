package stis.contactapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import stis.contactapi.entity.ContactSubject;

public interface ContactSubjectRepository extends JpaRepository<ContactSubject, Long> {

}

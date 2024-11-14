package stis.contactapi.repository;

import org.springframework.data.jpa.domain.Specification;
import stis.contactapi.entity.Contact;
import stis.contactapi.entity.ContactOrganization;
import stis.contactapi.entity.ContactSubject;
import stis.contactapi.entity.ECType;
import stis.contactapi.entity.EJabatan;
import jakarta.persistence.criteria.Join;

public class ContactSpecification {

    public static Specification<Contact> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) -> fullName == null ? null
                : criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<Contact> hasJabatan(EJabatan jabatan) {
        return (root, query, criteriaBuilder) -> {
            if (jabatan == null)
                return null;
            Join<Contact, ContactOrganization> organizationJoin = root.join("contactOrganizations");
            return criteriaBuilder.equal(organizationJoin.get("jabatan"), jabatan);
        };
    }

    public static Specification<Contact> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> email == null ? null : criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<Contact> hasContactType(ECType contactType) {
        return (root, query, criteriaBuilder) -> contactType == null ? null
                : criteriaBuilder.equal(root.get("contactType"), contactType);
    }

    public static Specification<Contact> hasOrganizationName(String organizationName) {
        return (root, query, criteriaBuilder) -> {
            if (organizationName == null)
                return null;
            Join<Contact, ContactOrganization> organizationJoin = root.join("contactOrganizations");
            return criteriaBuilder.like(organizationJoin.get("organizationId").get("organizationName"),
                    "%" + organizationName + "%");
        };
    }

    public static Specification<Contact> hasSubjectName(String subjectName) {
        return (root, query, criteriaBuilder) -> {
            if (subjectName == null)
                return null;
            Join<Contact, ContactSubject> subjectJoin = root.join("contactSubjects");
            return criteriaBuilder.like(subjectJoin.get("subject").get("subjectName"), "%" + subjectName + "%");
        };
    }
}

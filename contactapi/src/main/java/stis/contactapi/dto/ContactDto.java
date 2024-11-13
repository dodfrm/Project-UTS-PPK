package stis.contactapi.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stis.contactapi.entity.ContactOrganization;
import stis.contactapi.entity.ContactSubject;
import stis.contactapi.entity.ECType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private ECType contactType;
    private Set<ContactOrganization> contactOrganizations;
    private Set<ContactSubject> contactSubjects;
}
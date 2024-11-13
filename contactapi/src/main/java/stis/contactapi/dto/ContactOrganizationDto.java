package stis.contactapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stis.contactapi.entity.EJabatan;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactOrganizationDto {
    private Long id;
    private Long contactId;
    private String kelas;
    private Long organizationId;
    private EJabatan jabatan;
    private String periodeJabatan;
}

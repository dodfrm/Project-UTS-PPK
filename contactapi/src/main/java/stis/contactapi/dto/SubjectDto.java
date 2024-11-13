package stis.contactapi.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private Long id;
    @NotBlank(message = "Nama Mata Kuliah Tidak Boleh Kosong")
    private String subjectName;
    private List<ContactDto> contacts;
}

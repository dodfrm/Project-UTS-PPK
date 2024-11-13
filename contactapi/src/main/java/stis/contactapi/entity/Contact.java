package stis.contactapi.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String fullName;

    private String phone;

    private String email;

    @Column(name = "contact_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ECType contactType; // Mahasiswa, Dosen

    @OneToMany(mappedBy = "contactId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ContactOrganization> contactOrganizations;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ContactSubject> contactSubjects;
}
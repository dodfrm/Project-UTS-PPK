package stis.contactapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stis.contactapi.dto.OrganizationDto;
import stis.contactapi.entity.Organization;
import stis.contactapi.mapper.OrganizationMapper;
import stis.contactapi.repository.OrganizationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    // Get all organizations
    @Transactional(readOnly = true)
    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get organization by ID
    @Transactional(readOnly = true)
    public Optional<OrganizationDto> getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .map(organizationMapper::toDto);
    }

    // Create a new organization
    @Transactional
    public OrganizationDto createOrganization(OrganizationDto organizationDto) {
        Organization organization = organizationMapper.toEntity(organizationDto);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    // Update organization
    @Transactional
    public Optional<OrganizationDto> updateOrganization(Long id, OrganizationDto organizationDto) {
        return organizationRepository.findById(id)
                .map(existingOrganization -> {
                    Organization updatedOrganization = organizationMapper.toEntity(organizationDto);
                    updatedOrganization.setId(id);
                    updatedOrganization = organizationRepository.save(updatedOrganization);
                    return organizationMapper.toDto(updatedOrganization);
                });
    }

    // Delete organization
    @Transactional
    public boolean deleteOrganization(Long id) {
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

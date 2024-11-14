package stis.contactapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import stis.contactapi.dto.OrganizationDto;
import stis.contactapi.service.OrganizationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization", description = "Endpoint ini digunakan untuk mengelola organisasi yang ada di Politeknik Statistika STIS.")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    // Get all organizations
    @Operation(summary = "Get all organizations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all organizations"),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}")))
    })
    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations() {
        List<OrganizationDto> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    // Get organization by ID
    @Operation(summary = "Get organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Organization not found\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable Long id) {
        Optional<OrganizationDto> organization = organizationService.getOrganizationById(id);
        return organization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new organization
    @Operation(summary = "Create a new organization")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"organizationName\": \"BULSTIK\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organization created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}")))
    })
    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        OrganizationDto createdOrganization = organizationService.createOrganization(organizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganization);
    }

    // Update an organization
    @Operation(summary = "Update an organization")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"organizationName\": \"BULSTIK\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Organization not found\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@PathVariable Long id,
            @Valid @RequestBody OrganizationDto organizationDto) {
        Optional<OrganizationDto> updatedOrganization = organizationService.updateOrganization(id, organizationDto);
        return updatedOrganization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an organization
    @Operation(summary = "Delete an organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", description = "Organization not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Organization not found\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        boolean isDeleted = organizationService.deleteOrganization(id);
        return isDeleted ? ResponseEntity.ok("Organization deleted successfully") : ResponseEntity.notFound().build();
    }
}

package stis.contactapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stis.contactapi.dto.ContactOrganizationDto;
import stis.contactapi.service.ContactOrganizationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contact-organizations")
public class ContactOrganizationController {

    @Autowired
    private ContactOrganizationService contactOrganizationService;

    @Operation(summary = "Get all contact organizations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactOrganizationDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ContactOrganizationDto>> getAllContactOrganizations() {
        List<ContactOrganizationDto> contactOrganizations = contactOrganizationService.getAllContactOrganizations();
        return new ResponseEntity<>(contactOrganizations, HttpStatus.OK);
    }

    @Operation(summary = "Get a contact organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactOrganizationDto.class))),
            @ApiResponse(responseCode = "404", description = "Contact organization not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactOrganizationDto> getContactOrganizationById(@PathVariable Long id) {
        Optional<ContactOrganizationDto> contactOrganization = contactOrganizationService
                .getContactOrganizationById(id);
        return contactOrganization.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new contact organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contact organization created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactOrganizationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact not found.")
    })
    @PostMapping
    public ResponseEntity<ContactOrganizationDto> createContactOrganization(
            @Valid @RequestBody ContactOrganizationDto contactOrganizationDto) {
        ContactOrganizationDto createdContactOrganization = contactOrganizationService
                .createContactOrganization(contactOrganizationDto);
        return new ResponseEntity<>(createdContactOrganization, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing contact organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact organization updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactOrganizationDto.class))),
            @ApiResponse(responseCode = "404", description = "Contact organization not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContactOrganizationDto> updateContactOrganization(
            @PathVariable Long id, @Valid @RequestBody ContactOrganizationDto contactOrganizationDto) {
        Optional<ContactOrganizationDto> updatedContactOrganization = contactOrganizationService
                .updateContactOrganization(id, contactOrganizationDto);
        return updatedContactOrganization.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a contact organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contact organization deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact organization not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactOrganization(@PathVariable Long id) {
        boolean deleted = contactOrganizationService.deleteContactOrganization(id);
        return deleted ? ResponseEntity.noContent().build() : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

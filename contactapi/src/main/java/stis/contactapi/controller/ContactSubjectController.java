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

import stis.contactapi.dto.ContactSubjectDto;
import stis.contactapi.service.ContactSubjectService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contact-subjects")
public class ContactSubjectController {

    @Autowired
    private ContactSubjectService contactSubjectService;

    @Operation(summary = "Get all Contact Subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Contact Subject", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ContactSubjectDto.class)) })
    })
    @GetMapping
    public ResponseEntity<List<ContactSubjectDto>> getAllContactSubjects() {
        List<ContactSubjectDto> contactSubjects = contactSubjectService.getAllContactSubjects();
        return ResponseEntity.ok(contactSubjects);
    }

    @Operation(summary = "Get a Contact Subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Contact Subject", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ContactSubjectDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Contact Subject not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContactSubjectDto> getContactSubjectById(@PathVariable Long id) {
        Optional<ContactSubjectDto> contactSubject = contactSubjectService.getContactSubjectById(id);
        return contactSubject.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create a new Contact Subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contact Subject created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ContactSubjectDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact not found.")
    })
    @PostMapping
    public ResponseEntity<ContactSubjectDto> createContactSubject(
            @Valid @RequestBody ContactSubjectDto contactSubjectDto) {
        ContactSubjectDto createdContactSubject = contactSubjectService.createContactSubject(contactSubjectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContactSubject);
    }

    @Operation(summary = "Update an existing Contact Subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact Subject updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ContactSubjectDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Contact Subject not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContactSubjectDto> updateContactSubject(
            @PathVariable Long id,
            @Valid @RequestBody ContactSubjectDto contactSubjectDto) {
        Optional<ContactSubjectDto> updatedContactSubject = contactSubjectService.updateContactSubject(id,
                contactSubjectDto);
        return updatedContactSubject.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Delete a Contact Subject by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contact Subject deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact Subject not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactSubject(@PathVariable Long id) {
        if (contactSubjectService.deleteContactSubject(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

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

import stis.contactapi.dto.ContactDto;
import stis.contactapi.entity.ECType;
import stis.contactapi.entity.EJabatan;
import stis.contactapi.service.ContactService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contacts", description = "Endpoint ini digunakan untuk mengelola pembuatan, pengambilan, pembaruan, dan penghapusan informasi kontak mahasiswa dan dosen di Politeknik Statistika STIS.")
public class ContactController {

        @Autowired
        private ContactService contactService;

        // Get all contacts
        @Operation(summary = "Get all contacts")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "200", description = "List of all contacts")
        })
        @GetMapping
        public ResponseEntity<List<ContactDto>> getAllContacts() {
                List<ContactDto> contacts = contactService.getAllContacts();
                return ResponseEntity.ok(contacts);
        }

        // Search Contacts
        @Operation(summary = "Search contact by criteria")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contact found"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Contact not found\"}")))
        })
        @GetMapping("/search")
        public List<ContactDto> searchContacts(
                        @RequestParam(required = false) String fullName,
                        @RequestParam(required = false) EJabatan jabatan,
                        @RequestParam(required = false) String email,
                        @RequestParam(required = false) ECType contactType,
                        @RequestParam(required = false) String organizationName,
                        @RequestParam(required = false) String subjectName) {

                return contactService.searchContacts(fullName, jabatan, email, contactType, organizationName,
                                subjectName);
        }

        // Get contact by ID
        @Operation(summary = "Get contact by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contact found"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Contact not found\"}")))
        })
        @GetMapping("/{id}")
        public ResponseEntity<ContactDto> getContactById(@PathVariable Long id) {
                return contactService.getContactById(id)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        // Create a new contact
        @Operation(summary = "Create a new contact")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n"
                        +
                        "  \"fullName\": \"Dodi Firmansyah\",\n" +
                        "  \"phone\": \"081998915680\",\n" +
                        "  \"email\": \"222212572@stis.ac.id\",\n" +
                        "  \"contactType\": \"MAHASISWA or DOSEN\"\n" +
                        "}")))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Contact created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}")))
        })
        @PostMapping
        public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto) {
                ContactDto createdContact = contactService.createContact(contactDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
        }

        // Update an existing contact
        @Operation(summary = "Update an existing contact")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n"
                        +
                        "  \"fullName\": \"Dodi Firmansyah\",\n" +
                        "  \"phone\": \"081998915680\",\n" +
                        "  \"email\": \"222212572@stis.ac.id\",\n" +
                        "  \"contactType\": \"MAHASISWA or DOSEN\"\n" +
                        "}")))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contact updated successfully"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Contact not found\"}")))
        })
        @PutMapping("/{id}")
        public ResponseEntity<ContactDto> updateContact(@PathVariable Long id,
                        @Valid @RequestBody ContactDto contactDto) {
                Optional<ContactDto> updatedContact = contactService.updateContact(id, contactDto);
                return updatedContact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        // Delete a contact
        @Operation(summary = "Delete a contact")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contact deleted successfully"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Contact not found\"}")))
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteContact(@PathVariable Long id) {
                boolean isDeleted = contactService.deleteContact(id);
                return isDeleted ? ResponseEntity.ok("Contact deleted successfully")
                                : ResponseEntity.notFound().build();
        }
}

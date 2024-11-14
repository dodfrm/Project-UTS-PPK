package stis.contactapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stis.contactapi.dto.SubjectDto;
import stis.contactapi.service.SubjectService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subject", description = "endpoint ini digunakan untuk mengelola matakuliah yang ada di Politeknik Statistika STIS.")
public class SubjectController {

        @Autowired
        private SubjectService subjectService;

        @Operation(summary = "Get all subjects")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the subjects", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDto.class)) }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}")))
        })
        @GetMapping
        public ResponseEntity<List<SubjectDto>> getAllSubjects() {
                List<SubjectDto> subjects = subjectService.getAllSubjects();
                return ResponseEntity.ok(subjects);
        }

        @Operation(summary = "Get a subject by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the subject", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDto.class)) }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Subject not found\"}")))
        })
        @GetMapping("/{id}")
        public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id) {
                Optional<SubjectDto> subject = subjectService.getSubjectById(id);
                return subject.map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }

        @Operation(summary = "Create a new subject")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"subjectName\": \"Kalkulus\"}")))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Subject created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}")))
        })
        @PostMapping
        public ResponseEntity<SubjectDto> createSubject(@Valid @RequestBody SubjectDto subjectDto) {
                SubjectDto createdSubject = subjectService.createSubject(subjectDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
        }

        @Operation(summary = "Update an existing subject")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"subjectName\": \"Kalkulus\"}")))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Subject updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDto.class)) }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Subject not found\"}")))
        })
        @PutMapping("/{id}")
        public ResponseEntity<SubjectDto> updateSubject(
                        @PathVariable Long id,
                        @Valid @RequestBody SubjectDto subjectDto) {
                Optional<SubjectDto> updatedSubject = subjectService.updateSubject(id, subjectDto);
                return updatedSubject.map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }

        @Operation(summary = "Delete a subject by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Subject deleted", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Subject not found\"}")))
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
                if (subjectService.deleteSubject(id)) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
        }
}

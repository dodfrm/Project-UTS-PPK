package stis.contactapi.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import stis.contactapi.dto.UserDto;
import stis.contactapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Account")
public class AuthController {
        @Autowired
        AuthenticationManager authManager;
        @Autowired
        JwtUtil jwtUtil;
        @Autowired
        UserService userService;

        @Operation(summary = "Register a new user.")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to register with details such as name, email, and password. The request must contain valid values.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n"
                        +
                        "  \"name\": \"Dodi Firmansyah\",\n" +
                        "  \"email\": \"222212572@stis.ac.id\",\n" +
                        "  \"password\": \"utsppk2024\"\n" +
                        "}")))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User successfully registered", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Validation error details\"}")) }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Internal server error\"}")))
        })
        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody UserDto request) {
                UserDto user = userService.createUser(request);
                return ResponseEntity.ok().body(user);
        }

        @Operation(summary = "User login to get access token.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Email and access token", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)) }),
                        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Internal server error\"}"))) })

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
                try {
                        Authentication authentication = authManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(), request.getPassword())

                        );
                        String accessToken = jwtUtil.generateAccessToken(authentication);
                        AuthResponse response = new AuthResponse(request.getEmail(), accessToken);
                        return ResponseEntity.ok().body(response);
                } catch (BadCredentialsException ex) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
        }

        @Operation(summary = "Retrieve user profile by user ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User profile retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                        }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}")))
        })
        @GetMapping("/user-profile/{userId}")
        public ResponseEntity<UserDto> getUserProfile(@PathVariable Long userId) {
                UserDto user = userService.getUserById(userId);
                return ResponseEntity.ok(user);
        }

        @Operation(summary = "Update user profile information.")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to register with details such as name, email, and password. The request must contain valid values.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n"
                        +
                        "  \"name\": \"Dodi Firmansyah\",\n" +
                        "  \"email\": \"222212572@stis.ac.id\",\n" +
                        "  \"role\": \"ROLE_USER/ROLE_ADMIN\"\n" +
                        "}")))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User profile updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                        }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}"))),
                        @ApiResponse(responseCode = "400", description = "Invalid password data provided", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Invalid password data provided\"}")))
        })
        @PutMapping("/user-profile/{userId}")
        public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long userId, @RequestBody UserDto userDto) {
                UserDto updatedUser = userService.updateUserProfile(userId, userDto);
                return ResponseEntity.ok(updatedUser);
        }

        @Operation(summary = "Change user password.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Password changed successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Password changed successfully\"}"))
                        }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}"))),
                        @ApiResponse(responseCode = "400", description = "Invalid password data provided", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Invalid password data provided\"}")))
        })
        @PutMapping("/user/change-password/{userId}")
        public ResponseEntity<String> changePassword(@PathVariable Long userId,
                        @RequestBody ChangePasswordRequest passwordDto) {
                userService.changePassword(userId, passwordDto.getOldPassword(), passwordDto.getNewPassword());
                return ResponseEntity.ok("Password changed successfully");
        }

        @Operation(summary = "Delete a user account by user ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User deleted successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User deleted successfully\"}"))
                        }),
                        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Unauthorized\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}")))
        })
        @DeleteMapping("/user/{userId}")
        public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
                boolean isDeleted = userService.deleteUser(userId);
                if (isDeleted) {
                        return ResponseEntity.ok("User deleted successfully");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
}
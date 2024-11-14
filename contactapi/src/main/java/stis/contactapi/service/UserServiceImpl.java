package stis.contactapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import stis.contactapi.dto.UserDto;
import stis.contactapi.entity.ERole;
import stis.contactapi.entity.User;
import stis.contactapi.mapper.UserMapper;
import stis.contactapi.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUserProfile(Long userId, UserDto userDto) {
        // Dapatkan ID pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Temukan data user yang sedang login
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUserEmail == null) {
            throw new RuntimeException("User not found");
        }
        // Validasi akses: hanya pemilik akun atau admin yang bisa memperbarui profil
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals(ERole.ROLE_ADMIN)) {
            throw new RuntimeException("Unauthorized to update profile");
        }

        // Lakukan update profil jika valid
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        // Hanya admin yang diizinkan untuk mengubah peran pengguna
        if (currentUser.getRole().equals(ERole.ROLE_ADMIN)) {
            user.setRole(userDto.getRole());
        }
        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto changePassword(Long userId, String oldPassword, String newPassword) {
        // Dapatkan ID pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Temukan data user yang sedang login
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUserEmail == null) {
            throw new RuntimeException("User not found");
        }
        // Validasi akses: hanya pemilik akun atau admin yang bisa mengganti password
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals(ERole.ROLE_ADMIN)) {
            throw new RuntimeException("Unauthorized to change password");
        }

        // Temukan pengguna berdasarkan ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verifikasi password lama
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Ganti password dengan password baru
        user.setPassword(passwordEncoder.encode(newPassword));
        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        // Dapatkan ID pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Temukan data user yang sedang login
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUserEmail == null) {
            throw new RuntimeException("User not found");
        }
        // Validasi akses: hanya pemilik akun atau admin yang bisa mengganti password
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals(ERole.ROLE_ADMIN)) {
            throw new RuntimeException("Unauthorized to change password");
        }
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}

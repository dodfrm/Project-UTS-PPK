package stis.contactapi.service;

import stis.contactapi.dto.UserDto;

public interface UserService {
    public UserDto createUser(UserDto user);

    public UserDto getUserByEmail(String email);

    public UserDto getUserById(Long userId);

    UserDto updateUserProfile(Long userId, UserDto userDto);

    UserDto changePassword(Long userId, String oldPassword, String newPassword);

    boolean deleteUser(Long userId);
}
package ru.taras_buriachenko.NauJava;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.taras_buriachenko.NauJava.dto.RegisterRequestDto;
import ru.taras_buriachenko.NauJava.dto.UserResponseDto;
import ru.taras_buriachenko.NauJava.dto.UserUpdateDto;
import ru.taras_buriachenko.NauJava.entity.User;
import ru.taras_buriachenko.NauJava.repository.crud.UserRepository;
import ru.taras_buriachenko.NauJava.service.impl.UserServiceImpl;
import ru.taras_buriachenko.NauJava.type.UserRole;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setId(1L);
        user1.setLastSeen(now);
        user1.setOnline(true);
        user1.setRole(UserRole.USER);
        user1.setUsername("user1");
        user1.setPhoneNumber("+79001002030");
        user1.setPasswordHash("encoded_0000");
    }

    @Test
    void addUser_ShouldReturnSuccess() {
        RegisterRequestDto requestDto = new RegisterRequestDto(
                "new_user",
                "+79003002030",
                "password"
        );
        when(userRepository.existsByUsername("new_user")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(2L);
            return savedUser;
        });

        UserResponseDto result = userService.addUser(requestDto);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("new_user", result.username());
        assertTrue(result.isOnline());
        assertNotNull(result.lastSeen());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("new_user", capturedUser.getUsername());
        assertEquals("hashedPassword", capturedUser.getPasswordHash());
        assertEquals("+79003002030", capturedUser.getPhoneNumber());
        assertEquals(UserRole.USER, capturedUser.getRole());
        assertTrue(capturedUser.isOnline());
    }

    @Test
    void addUser_UsernameAlreadyExists_ThrowsException() {
        RegisterRequestDto requestDto = new RegisterRequestDto(
                "user1",
                "+79991112233",
                "raw_pass"
        );

        when(userRepository.existsByUsername("user1")).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> userService.addUser(requestDto)
        );

        assertEquals("User with username user1 has already exists", exception.getMessage());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_AllFieldsSuccess() {
        UserUpdateDto updateDto = new UserUpdateDto("updated_user", "new_raw_pass", "+79009998877");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(passwordEncoder.encode("new_raw_pass")).thenReturn("new_encoded_pass");
        when(userRepository.existsByUsername("updated_user")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        UserResponseDto result = userService.updateUser(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("updated_user", result.username());
        assertEquals("new_encoded_pass", user1.getPasswordHash());
        assertEquals("+79009998877", user1.getPhoneNumber());

        verify(passwordEncoder, times(1)).encode("new_raw_pass");
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void updateUser_SameUsernameSuccess() {
        UserUpdateDto updateDto = new UserUpdateDto("user1", null, "+79005554433");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.existsByUsername("user1")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        UserResponseDto result = userService.updateUser(1L, updateDto);

        assertNotNull(result);
        assertEquals("user1", result.username());
        assertEquals("+79005554433", user1.getPhoneNumber());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void updateUser_IgnoreNullAndBlankFields() {
        UserUpdateDto updateDto = new UserUpdateDto("   ", null, "");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        UserResponseDto result = userService.updateUser(1L, updateDto);

        assertNotNull(result);
        assertEquals("user1", result.username());
        assertEquals("encoded_0000", user1.getPasswordHash());
        assertEquals("+79001002030", user1.getPhoneNumber());

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).existsByUsername(any());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void updateUser_BlankPasswordAndNullUsername_Ignored() {
        UserUpdateDto updateDto = new UserUpdateDto(null, "   ", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        UserResponseDto result = userService.updateUser(1L, updateDto);

        assertNotNull(result);
        assertEquals("user1", result.username());
        assertEquals("encoded_0000", user1.getPasswordHash());
        assertEquals("+79001002030", user1.getPhoneNumber());

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).existsByUsername(any());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        Long nonExistentId = 999L;
        UserUpdateDto updateDto = new UserUpdateDto("name", "pass", "123");
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(nonExistentId, updateDto)
        );

        assertEquals("User with id = 999 not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_UsernameTakenByAnotherUser_ThrowsException() {
        UserUpdateDto updateDto = new UserUpdateDto("taken_username", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.existsByUsername("taken_username")).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> userService.updateUser(1L, updateDto)
        );

        assertEquals("User with username taken_username has already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void removeUser_Success() {
        when(userRepository.deleteUserById(1L)).thenReturn(1L);

        assertDoesNotThrow(() -> userService.removeUser(1L));

        verify(userRepository, times(1)).deleteUserById(1L);
    }

    @Test
    void removeUser_UserNotFound_ThrowsException() {
        Long nonExistentId = 999L;
        when(userRepository.deleteUserById(nonExistentId)).thenReturn(0L);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.removeUser(nonExistentId)
        );

        assertEquals("User with id = 999 not found", exception.getMessage());
        verify(userRepository, times(1)).deleteUserById(nonExistentId);
    }

    @Test
    void searchUserByUsername_Found() {
        when(userRepository.findByUsernameIgnoreCase("USER1")).thenReturn(List.of(user1));

        List<UserResponseDto> results = userService.searchUserByUsername("USER1");

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).id());
        assertEquals("user1", results.get(0).username());
        assertEquals(now, results.get(0).lastSeen());
        assertTrue(results.get(0).isOnline());

        verify(userRepository, times(1)).findByUsernameIgnoreCase("USER1");
    }

    @Test
    void searchUserByUsername_NotFound_ReturnsEmptyList() {
        when(userRepository.findByUsernameIgnoreCase("unknown")).thenReturn(Collections.emptyList());

        List<UserResponseDto> results = userService.searchUserByUsername("unknown");

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(userRepository, times(1)).findByUsernameIgnoreCase("unknown");
    }

    @Test
    void searchUserByPhoneNumber_Found() {
        String phone = "+79001002030";
        when(userRepository.findByPhoneNumber(phone)).thenReturn(List.of(user1));

        List<UserResponseDto> results = userService.searchUserByPhoneNumber(phone);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).id());
        assertEquals("user1", results.get(0).username());

        verify(userRepository, times(1)).findByPhoneNumber(phone);
    }

    @Test
    void searchUserByPhoneNumber_NotFound_ReturnsEmptyList() {
        String phone = "+70000000000";
        when(userRepository.findByPhoneNumber(phone)).thenReturn(Collections.emptyList());

        List<UserResponseDto> results = userService.searchUserByPhoneNumber(phone);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(userRepository, times(1)).findByPhoneNumber(phone);
    }
}

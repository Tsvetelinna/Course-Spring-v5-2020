package course.spring.services.impl;

import course.spring.dao.UserRepository;
import course.spring.exceptions.AlreadyExistingEntityException;
import course.spring.exceptions.InvalidEntityDataException;
import course.spring.exceptions.NonExistingEntityException;
import course.spring.models.users.Gender;
import course.spring.models.users.Role;
import course.spring.models.users.User;
import course.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static course.spring.models.users.AccountStatus.ACTIVE;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NonExistingEntityException(String.format("User with ID:%s does not exist.", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new InvalidEntityDataException("Invalid username or password."));
    }

    @Override
    public User addUser(User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            throw new AlreadyExistingEntityException("User with this username already exist");
        }

        if (user.getProfilePictureUrl() == null) {
            if (user.getGender() == Gender.FEMALE) {
                user.setProfilePictureUrl(User.getFemalePictureURL());
            } else if (user.getGender() == Gender.MALE) {
                user.setProfilePictureUrl(User.getMalePictureURL());
            }
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setId(null);
        user.setStatus(ACTIVE);
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.insert(user);
    }

    @Override
    public User updateUser(User user) {
        User userById = getUserById(user.getId());
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        if (user.getStatus() == null) {
            user.setStatus(userById.getStatus());
        }
        user.setRole(Role.USER);
        user.setPassword(userById.getPassword());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(String id) {
        User removed = getUserById(id);
        userRepository.deleteById(id);
        return removed;
    }
}

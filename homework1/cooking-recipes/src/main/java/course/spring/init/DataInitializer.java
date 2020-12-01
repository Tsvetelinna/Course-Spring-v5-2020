package course.spring.init;

import course.spring.models.users.Gender;
import course.spring.models.users.Role;
import course.spring.models.users.User;
import course.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static course.spring.models.users.Gender.FEMALE;
import static course.spring.models.users.Role.ADMIN;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    private static final User user = new User(
            "Default",
            "admin",
            "admin123-",
            FEMALE,
            ADMIN,
            null,
            "A am test admin user.");

    @Override
    public void run(String... args) throws Exception {
        userService.addUser(user);

    }
}

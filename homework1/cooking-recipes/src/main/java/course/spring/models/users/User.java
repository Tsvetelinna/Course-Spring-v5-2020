package course.spring.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Document(collection="users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid user ID")
    private String id;
    @NonNull
    @NotNull
    private String name;
    @NonNull
    @NotNull
    @Size(max=15)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NonNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*\\W)[\\w\\W]{8,}$", message = "Invalid password - must contains at least one digit and at least one non-digit and non-letter")
    private String password;
    @NonNull
    @NotNull
    private Gender gender;
    @NonNull
    @NotNull
    private Role role;
    @URL
    private String profilePictureUrl;
    @Size(max=512)
    private String description;
    @NonNull
    private AccountStatus status;
    @PastOrPresent
    private LocalDateTime createdAt = LocalDateTime.now();
    @PastOrPresent
    private LocalDateTime updatedAt = LocalDateTime.now();
    private boolean active = true;

    public User(@NonNull @NotNull String name,
                @NonNull @NotNull @Size(max = 15) String username,
                @NonNull @Size(min = 8) String password,
                @NonNull Gender gender,
                @NonNull Role role,
                @URL String profilePictureUrl,
                @Size(max = 512) String description) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.profilePictureUrl = profilePictureUrl;
        this.description = description;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return active;
    }

    public static String getFemalePictureURL() {
        return "https://cdn1.iconfinder.com/data/icons/website-internet/48/website_-_female_user-512.png";
    }

    public static String getMalePictureURL() {
        return "https://www.csahkh.com/wp-content/uploads/2020/03/avt.png";
    }
}

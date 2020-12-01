package course.spring.models;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection="recipes")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid recipe ID")
    private String id;
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid user ID")
    private String userId;
    @NonNull
    @NotNull
    @Size(max=80)
    private String title;
    @NonNull
    @NotNull
    @Size(max=256)
    private String shortDescription;
    @NonNull
    @NotNull
    private Integer cookingTimeInMinutes;
    @NonNull
    @NotNull
    private List<String> ingredients = new ArrayList<>();
    @NonNull
    @URL
    private String imageUrl;
    @Size(max=2048)
    private String longDescription;
    private List<@Pattern(regexp = "^[\\w\\s-]+$", message = "Invalid keyword - should contain only letters and digits") String> keywords = new ArrayList<>();
    @PastOrPresent
    private LocalDateTime createdAt = LocalDateTime.now();
    @PastOrPresent
    private LocalDateTime updatedAt = LocalDateTime.now();

}

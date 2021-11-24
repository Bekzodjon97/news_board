package uz.dev.newsboard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.dev.newsboard.entity.News;
import uz.dev.newsboard.entity.User;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.List;

@AllArgsConstructor
@Data
public class UserDto {
    @NotBlank(message = "Login not be empty")
    @Size(max = 50)
    private String login;

    @NotBlank(message = "Password not be empty")
    @Size(max = 50)
    private String password;

    @Email(message = "Email not")
    @NotBlank(message = "Email not be empty")
    private String email;

    @NotNull(message = "Age not be empty")
    @Min(value = 10)
    @Max(value = 150)
    private Integer age;



}

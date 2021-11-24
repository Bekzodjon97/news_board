package uz.dev.newsboard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class NewsDto {
    @NotBlank(message = "Title not be empty")
    @Size(max = 50)
    private String title;

    @NotBlank(message = "Text not be empty")
    @Size(max = 500)
    private String text;
}

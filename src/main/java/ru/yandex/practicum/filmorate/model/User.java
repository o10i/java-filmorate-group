package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

   private Long id;
   @NotBlank
   @Email
   private String email;
   @NotBlank
   private String login;
   private String name;
   @PastOrPresent
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private LocalDate birthday;
   private final Set<Long> friends = new HashSet<>();

}

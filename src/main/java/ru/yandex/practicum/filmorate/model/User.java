package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {

   Long id;
   @NotBlank
   @Email
   String email;
   @NotBlank
   String login;
   String name;
   @PastOrPresent
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   LocalDate birthday;
   final Set<Long> friends = new HashSet<>();

}

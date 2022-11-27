package ru.yandex.practicum.filmorate.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Create;
import ru.yandex.practicum.filmorate.model.groupInterfaces.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NotNull(groups = Update.class)
    Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Email
    String email;
    @NotBlank(groups = {Create.class, Update.class})
    String login;
    String name;
    @PastOrPresent(groups = {Create.class, Update.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("email", email);
        values.put("login", login);
        values.put("birthday", birthday);
        return values;
    }

}

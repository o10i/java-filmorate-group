package ru.yandex.practicum.filmorate.model.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.Marker;

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
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Email
    String email;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    String login;
    String name;
    @PastOrPresent(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
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

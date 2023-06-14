package rw.rca.springsecuritytemplate.Pojos.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotNull
    private String username;
    @Email
    private String email;
    @NotNull
    private String password;
    Set<String> role;
}

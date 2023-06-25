package rw.rca.springsecuritytemplate.Pojos.Request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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

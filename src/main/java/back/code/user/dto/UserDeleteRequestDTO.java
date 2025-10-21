package back.code.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteRequestDTO {
    private String userId;
    private String password;
}
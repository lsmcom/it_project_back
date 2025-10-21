package back.code.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPwResetRequestDTO {
    private String userId;
    private String newPassword;
}
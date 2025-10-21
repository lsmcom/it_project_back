package back.code.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPwCheckRequestDTO {
    private String userId;
    private String name;
    private String email;
}
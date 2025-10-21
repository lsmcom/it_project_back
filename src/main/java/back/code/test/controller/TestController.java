package back.code.test.controller;

import back.code.test.dto.ParamDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated  // 메서드 또는 파라메터 검증
public class TestController {

    @GetMapping(value="/test", consumes = MediaType.APPLICATION_JSON_VALUE,
                               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test(@RequestBody @Valid ParamDTO paramDTO){
        return ResponseEntity.ok(paramDTO);
    }

    @GetMapping(value="/test2")
    public ResponseEntity<?> test2( @Valid  ParamDTO paramDTO){
        return ResponseEntity.ok(paramDTO);
    }

    @GetMapping(value="/test3")
    public ResponseEntity<?> test3(@RequestParam("myName") @NotBlank String myName,
                                   @RequestParam("email") @NotBlank String email){
        JSONObject obj = new JSONObject();
        obj.put("myName", myName);
        return ResponseEntity.ok(obj.toString());

    }
}

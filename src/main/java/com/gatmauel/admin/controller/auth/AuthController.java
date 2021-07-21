package com.gatmauel.admin.controller.auth;

import com.gatmauel.admin.dto.admin.AdminDTO;
import com.gatmauel.admin.entity.admin.EmailAddressException;
import com.gatmauel.admin.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/@admin/auth")
@RestController
@Log4j2
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody AdminDTO dto) {
        try {
            return new ResponseEntity<>(authService.register(dto), HttpStatus.OK);
        } catch(EmailAddressException e){
            Map<String, Object> error = new HashMap<>();
            HttpStatus status;

            error.put("code", HttpStatus.CONFLICT.value());
            error.put("message", e.getMessage());

            status=HttpStatus.CONFLICT;

            return new ResponseEntity<>(error, status);
        } catch(Exception e) {
            Map<String, Object> error = new HashMap<>();
            HttpStatus status;

            error.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.put("message", e.getMessage());

            status = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(error, status);
        }
    }

    @PatchMapping("/modify/{id}")
    @PreAuthorize("#id==principal.getId()")
    public ResponseEntity<Map<String, Object>> modify(@PathVariable(value="id") Long id, @RequestBody AdminDTO dto){
        return new ResponseEntity<>(authService.modify(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("#id==principal.getId()")
    public ResponseEntity<Map<String, Long>> remove(@PathVariable(value="id") Long id){
        return new ResponseEntity<>(authService.remove(id), HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) throws Exception{
        log.debug(token);

        String email=authService.confirm(token);

        return "<div><br/><h2>"+email+"인증이 완료되었습니다.</h2></div>";
    }
}

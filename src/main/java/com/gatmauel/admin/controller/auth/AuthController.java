package com.gatmauel.admin.controller.auth;

import com.gatmauel.admin.dto.admin.AdminDTO;
import com.gatmauel.admin.entity.admin.EmailAddressException;
import com.gatmauel.admin.service.auth.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/@admin/auth")
@RestController
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
        try{
            return new ResponseEntity<>(authService.modify(id, dto), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            Map<String, Object> error = new HashMap<>();
            HttpStatus status;

            error.put("code", HttpStatus.BAD_REQUEST.value());
            error.put("message", e.getMessage());

            status = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(error, status);
        } catch (Exception e){
            Map<String, Object> error = new HashMap<>();
            HttpStatus status;

            error.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.put("message", e.getMessage());

            status = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(error, status);
        }
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("#id==principal.getId()")
    public ResponseEntity<Map<String, Long>> remove(@PathVariable(value="id") Long id){
        return new ResponseEntity<>(authService.remove(id), HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token){
        log.debug(token);

        try{
            String email=authService.confirm(token);

            String result="<div><br/><h2>"+email+"인증이 완료되었습니다.</h2></div>";

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(ExpiredJwtException e) {
            String result="<div><br/><h2>링크가 만료됐습니다.</h2></div>";

            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

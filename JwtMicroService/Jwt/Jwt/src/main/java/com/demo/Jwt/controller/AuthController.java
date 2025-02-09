package com.demo.Jwt.controller;


import com.demo.Jwt.dto.JwtRequest;
import com.demo.Jwt.dto.JwtResponse;
import com.demo.Jwt.service.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        String token = tokenProvider.createToken(request.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));

    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null){// || !authHeader.startsWith("Beares ")
            return ResponseEntity.badRequest().build();
        }
        String oldToken = authHeader.substring(7);
        String newToken = tokenProvider.refreshToken(oldToken);
        return ResponseEntity.ok(new JwtResponse(newToken));
    }

}

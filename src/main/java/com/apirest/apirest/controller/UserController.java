package com.apirest.apirest.controller;

import com.apirest.apirest.controller.request.CreateUserDTO;
import com.apirest.apirest.model.ERole;
import com.apirest.apirest.model.RoleEntity;
import com.apirest.apirest.model.UserEntity;
import com.apirest.apirest.repository.UserRepository;
import com.apirest.apirest.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World Not Secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO,
                                        @RequestHeader("Authorization") String jwtToken){

        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"El correo ya existe.\"}");
        }

        if (!Utils.validarPassword(createUserDTO.getPassword())) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"El formato de la contraseña no es válido.\"}");
        }

        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            String  token = jwtToken.substring(7); // Obtener el token sin el prefijo "Bearer "

            // Tu lógica para almacenar el token en el objeto UserEntity
            userEntity.setToken(token);
        }

        LocalDateTime fechaActual = LocalDateTime.now();

        userEntity.setCreated(fechaActual);
        userEntity.setModified(fechaActual);
        userEntity.setUuid(Utils.crearUUID());
        //return new ResponseEntity<String>("{\"mensaje\":\"Bien lo lograste!\"}", HttpStatus.OK);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el user con id".concat(id);
    }
    }
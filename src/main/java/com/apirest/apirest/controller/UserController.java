package com.apirest.apirest.controller;

import com.apirest.apirest.controller.request.CreateUserDTO;
import com.apirest.apirest.model.ERole;
import com.apirest.apirest.model.RoleEntity;
import com.apirest.apirest.model.UserEntity;
import com.apirest.apirest.repository.UserRepository;
import com.apirest.apirest.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
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
                                        BindingResult result,
                                        @RequestHeader("Authorization") String jwtToken
                                        ){

        UserEntity clienteNew = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

        }

        try{

//            if (userRepository.existsByEmail(createUserDTO.getEmail())) {
//                return ResponseEntity.badRequest().body("{\"mensaje\":\"El correo ya existe.\"}");
//            }
//
//            if (!Utils.validarPassword(createUserDTO.getPassword())) {
//                return ResponseEntity.badRequest().body("{\"mensaje\":\"El formato de la contraseña no es válido.\"}");
//            }

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

            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                String  token = jwtToken.substring(7); // Obtener el token sin el prefijo "Bearer "
                // Tu lógica para almacenar el token en el objeto UserEntity
                userEntity.setToken(token);
            }

            userEntity.setUuid(Utils.crearUUID());

            clienteNew = userRepository.save(userEntity);
        }catch(DataAccessException e){
            response.put("mensaje: ", "Error al guardar al cliente en bd");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje: ", "El cliente ha sido creado con éxito");
        response.put("usuario: ", clienteNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity update(@RequestBody CreateUserDTO createUserDTO, @PathVariable Long id){

        Optional<UserEntity> user =  userRepository.findById(id);

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .build();

        return userRepository.save(userEntity);
    }

    @DeleteMapping("/deleteUser/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        userRepository.deleteById(id);
    }

    }
package com.nosy.admin.nosyadmin.controller;

import com.nosy.admin.nosyadmin.dto.TokenCollectionDto;
import com.nosy.admin.nosyadmin.dto.UserDto;
import com.nosy.admin.nosyadmin.service.KeycloakService;
import com.nosy.admin.nosyadmin.service.UserService;
import com.nosy.admin.nosyadmin.utils.TokenCollectionMapper;
import com.nosy.admin.nosyadmin.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/api/v1/nosy")
public class AuthController {

  private UserService userService;
  private KeycloakService keycloakService;

  @Autowired
  public AuthController(
      UserService userService,  KeycloakService keycloakService) {
    this.userService = userService;
    this.keycloakService = keycloakService;
  }

  @GetMapping(path = "/auth/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    userService.logoutUser(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping(path = "/auth/status")
  public ResponseEntity<Boolean> isAuthenticated(@RequestBody TokenCollectionDto token)  {
    return new ResponseEntity<>(keycloakService.isAuthenticated(token.getAccessToken()), HttpStatus.OK);
  }

  @PostMapping(value = "/auth/token")
  public ResponseEntity<TokenCollectionDto> getToken(@RequestBody @Valid UserDto userdto)
      throws IOException {
    return new ResponseEntity<>(
            TokenCollectionMapper.INSTANCE.
                    toTokenCollectionDto(keycloakService.getTokens(UserMapper.INSTANCE.toUser(userdto))), HttpStatus.OK);
  }

  @PostMapping(value = "/users")
  public ResponseEntity<UserDto> newUser(@RequestBody @Valid UserDto userdto) {
    return new ResponseEntity<>(
        UserMapper.INSTANCE.toUserDto(userService.addUser(UserMapper.INSTANCE.toUser(userdto))), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/users")
  public ResponseEntity<String> deleteUsername(HttpServletRequest request) {
    userService.deleteUser(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "/users")
  public ResponseEntity<UserDto> getUserProfile(HttpServletRequest request) {
    return new ResponseEntity<>(UserMapper.INSTANCE.toUserDto(userService.getUserInfo(request)), HttpStatus.OK);
  }
}

package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.exceptions.PasswordIsNotValidException;
import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private KeycloakService keycloakService;

    private User user;
    String email="test@nosy.tech";
    @Before
    public void setUser(){

        user=new User();
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("Nosy");
        user.setPassword("dajsndjasn");
    }


    @Test
    public void testUser(){
        assertEquals("Nosy", user.getLastName());
        assertEquals("Test", user.getFirstName());
        String userString="User{email='test@nosy.tech', inputSystem=null, firstName='Test', lastName='Nosy'}";
        assertEquals(userString, user.toString());
    }

    @Test
    public void getUserInfo() {

        HttpServletRequest httpServletRequest=mock(HttpServletRequest.class);
        Principal principal=mock(Principal.class);
        when(httpServletRequest.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(email);
        when(keycloakService.getUserInfo(email)).thenReturn(user);
        assertEquals(email,userService.getUserInfo(httpServletRequest).getEmail());
    }

    @Test(expected = Test.None.class)
    public void deleteUser() {
        HttpServletRequest httpServletRequest=mock(HttpServletRequest.class);
        Principal principal=mock(Principal.class);
        when(httpServletRequest.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(email);
        userService.deleteUser(httpServletRequest);

    }

    @Test(expected = Test.None.class)
    public void logoutUser() {
        HttpServletRequest httpServletRequest=mock(HttpServletRequest.class);
        Principal principal=mock(Principal.class);
        when(httpServletRequest.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(email);
        userService.logoutUser(httpServletRequest);
    }

    @Test
    public void addUser() {
        doNothing().when(keycloakService).registerNewUser(user);
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        assertEquals(user.getEmail(),userService.addUser(user).getEmail());
    }






    @Test(expected = PasswordIsNotValidException.class)
    public void addUserWithInvalidPassword() {
        User userWithInvalidPassword=new User();
        user.setEmail("test@nosy.tech");
        user.setPassword("");
        assertEquals(user.getEmail(),userService.addUser(userWithInvalidPassword).getEmail());
    }
    @Test(expected = PasswordIsNotValidException.class)
    public void addUserWithInvalidNullPassword() {
        User userWithInvalidPassword=new User();
        user.setEmail("test@nosy.tech");
        assertEquals(user.getEmail(),userService.addUser(userWithInvalidPassword).getEmail());
    }
    @Test(expected = PasswordIsNotValidException.class)
    public void addUserWithInvalidLength5Password() {
        User userWithInvalidPassword=new User();
        user.setEmail("test@nosy.tech");
        user.setPassword("12345");

        assertEquals(user.getEmail(),userService.addUser(userWithInvalidPassword).getEmail());
    }
}

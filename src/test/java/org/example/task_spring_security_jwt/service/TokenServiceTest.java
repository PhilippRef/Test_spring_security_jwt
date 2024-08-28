package org.example.task_spring_security_jwt.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private TokenService tokenService;

    private final UserDetails userDetails =
            Mockito.mock(UserDetails.class);

    private final String jwtSecret = "984hg493gh0439rthr0429uruj2309yh937glksjgore84u8tsoghdfjnvzxnvlc763fe87t3f89723gf";

    private final long tokenExpiration = 3600000;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        tokenService.setJwtSecret(jwtSecret);
        tokenService.setTokenExpiration(tokenExpiration);
    }

    @Test
    @DisplayName("test generateToken and return token when user details are valid")
    void testGenerateToken() {
        String username = "testUser";

        when(userDetails.getUsername()).thenReturn(username);

        String token = tokenService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.startsWith("ey"));

        Claims claims = tokenService.extractAllClaims(token);

        assertEquals(username, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());

        Date expirationDate = claims.getExpiration();
        Date currentDate = new Date();

        assertTrue(expirationDate.after(currentDate));
        assertFalse(claims.getExpiration().before(new Date()));
        assertFalse(claims.getIssuedAt().after(new Date()));

        verify(userDetails, Mockito.times(1)).getAuthorities();
    }

    @Test
    @DisplayName("test get user name when user details are valid")
    void getUserNameWhenUserIsValid() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = tokenService.generateToken(userDetails);
        String userName = tokenService.getUserName(token);

        assertEquals("testUser", userName);

        verify(userDetails, Mockito.times(1)).getUsername();
        verify(userDetails, Mockito.times(1)).getAuthorities();
    }

    @Test
    @DisplayName("test extractAllClaims and return claims when user details are valid")
    void testExtractAllClaims() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = tokenService.generateToken(userDetails);
        Claims claims = tokenService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
        assertTrue(claims.containsKey("role"));

        verify(userDetails, Mockito.times(1)).getAuthorities();
        verify(userDetails, Mockito.times(1)).getUsername();
    }

    @Test
    @DisplayName("test getSigningKey")
    public void testGetSigningKey() {
        Key signingKey = tokenService.getSigningKey();

        assertNotNull(signingKey);
    }

}
package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordEncodingTests {

    static final String PASSWORD = "password";

    @Test
    void testBCrypt(){
        var bCrypt = new BCryptPasswordEncoder();
        System.out.println(bCrypt.encode("guru"));
        System.out.println(bCrypt.encode(PASSWORD));
        var encodedPassword = bCrypt.encode(PASSWORD);
        assertThat(bCrypt.matches(PASSWORD, encodedPassword)).isTrue();
    }

    @Test
    void testSha256() {
        var sha = new StandardPasswordEncoder();
        System.out.println(sha.encode(PASSWORD));
        System.out.println(sha.encode(PASSWORD));
        var encodedPassword = sha.encode(PASSWORD);
        assertThat(sha.matches(PASSWORD, encodedPassword)).isTrue();
    }

    @Test
    void testLdap() {
        var ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        var encodedPassword = ldap.encode(PASSWORD);

        assertThat(ldap.matches(PASSWORD, encodedPassword)).isTrue();
    }


    @Test
    void testNoOp() {
        var noOp = NoOpPasswordEncoder.getInstance();

        System.out.println(noOp.encode(PASSWORD));

    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes(StandardCharsets.UTF_8)));
        var salted = PASSWORD + "ThisIsMySALTVALUE";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes(StandardCharsets.UTF_8)));
    }
}



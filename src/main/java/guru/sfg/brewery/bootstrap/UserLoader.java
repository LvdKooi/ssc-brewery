package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserLoader implements CommandLineRunner {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {

        List<Authority> savedAuthorities = getDefaultAuthorities().stream().map(auth -> authorityRepository.findByRole(auth.getRole()).orElse(authorityRepository.save(auth))).collect(Collectors.toList());


        getDefaultUsers(savedAuthorities).forEach(user -> userRepository.findByUsername(user.getUsername()).orElse(userRepository.save(user)));

        log.debug("Users loaded: {}", userRepository.findAll().size());
    }

    private String encodePassword(String password) {
        return encoder.encode(password);
    }


    private List<User> getDefaultUsers(List<Authority> savedAuthorities) {

        Authority adminRole = savedAuthorities.stream().filter(a -> a.getRole().equals("ROLE_ADMIN")).findFirst().orElse(null);
        Authority userRole = savedAuthorities.stream().filter(a -> a.getRole().equals("ROLE_USER")).findFirst().orElse(null);
        Authority customerRole = savedAuthorities.stream().filter(a -> a.getRole().equals("ROLE_CUSTOMER")).findFirst().orElse(null);

        User spring = User.builder().username("spring").password(encodePassword("guru")).authority(adminRole).build();
        User user = User.builder().username("user").password(encodePassword("password")).authority(userRole).build();
        User scott = User.builder().username("scott").password(encodePassword("tiger")).authority(customerRole).build();

        return List.of(spring, user, scott);

    }

    private List<Authority> getDefaultAuthorities() {

        Authority admin = Authority.builder()
                .role("ROLE_ADMIN").build();

        Authority user = Authority.builder()
                .role("ROLE_USER").build();

        Authority customer = Authority.builder()
                .role("ROLE_CUSTOMER").build();

        return List.of(admin, user, customer);

    }

}

package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    private BeerRepository beerRepository;


    @Test
    void findBeersNoUser() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findSpecificBeerNoUser() throws Exception {

        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findSpecificBeerUpcNoUser() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeersCustomerRole() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerCustomerRole() throws Exception {

        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerUpcCustomerRole() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void findBeersAdminRole() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerAdminRole() throws Exception {

        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerUpcAdminRole() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }


    @Test
    void findBeersUserRole() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerUserRole() throws Exception {

        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerUpcUserRole() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036").with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }
}

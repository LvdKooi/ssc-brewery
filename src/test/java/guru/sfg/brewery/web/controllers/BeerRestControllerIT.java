package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class BeerRestControllerIT extends BaseIT {


    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeer() throws Exception {
        mockMvc.perform(get("/api/v1/beer/3804f2b4-e9f5-43e9-a9b5-2f65ab8dc226"))
                .andExpect(status().isOk());
    }

    @Test
    void findSpecificBeerUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }


    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/3804f2b4-e9f5-43e9-a9b5-2f65ab8dc226")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerRequestParams() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/3804f2b4-e9f5-43e9-a9b5-2f65ab8dc226")
                        .param("user", "spring")
                        .param("password", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadCred() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/3804f2b4-e9f5-43e9-a9b5-2f65ab8dc226")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "WRONG!"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/3804f2b4-e9f5-43e9-a9b5-2f65ab8dc226")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/3804f2b4-e9f5-43e9-a9b5-2f65ab8dc226"))
                .andExpect(status().isUnauthorized());
    }
}

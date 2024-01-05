package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.controllers;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.AdoptPostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class HomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdoptPostService adoptPostService;

    @Test
    public void getHomePageContainsModelData() throws Exception {

        when(adoptPostService.parseLessThanAge(any())).thenReturn(Integer.MAX_VALUE);
        when(adoptPostService.parsePuppyBreed(any())).thenReturn(null);

        mockMvc.perform(get("/home"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attribute("pageSize", AdoptPostService.PAGE_SIZE))
                .andExpect(model().attributeExists("numPages"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("baseUrl", "/home?page=1"))
                .andExpect(model().attributeExists("breadValues"))
                .andExpect(model().attribute("selectedBreed", "any"))
                .andExpect(status().isOk());

    }

}

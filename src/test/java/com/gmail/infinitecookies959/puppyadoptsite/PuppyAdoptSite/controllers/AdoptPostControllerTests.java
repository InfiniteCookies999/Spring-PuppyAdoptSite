package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.controllers;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.AdoptPostService;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.WebUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdoptPostsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AdoptPostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdoptPostService adoptPostService;

    @MockBean
    private WebUserService webUserService;

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

    @Test
    public void publishPostRedirectsSuccessfulNewPost() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile(
          "puppyImage",
          "image.png",
          MediaType.IMAGE_PNG_VALUE,
          "contents".getBytes()
        );

        when(webUserService.getUser(any())).thenReturn(Optional.of(new WebUser()));
        doNothing().when(adoptPostService).createNewPost(any(), any());

        mockMvc.perform(multipart("/new-post")
                        .file(mockFile)
                        .param("puppyName", "Paco")
                        .param("puppyAge", "4")
                        .param("description", "This is the description")
                        .param("breed", "POODLE"))
                .andExpect(redirectedUrl("/successful-new-post"))
                .andExpect(status().isFound());

    }

    @Test
    public void publishPostWithBadFileHasModelErrors() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile(
                "puppyImage",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "contents".getBytes()
        );

        when(webUserService.getUser(any())).thenReturn(Optional.of(new WebUser()));
        doNothing().when(adoptPostService).createNewPost(any(), any());

        mockMvc.perform(multipart("/new-post")
                        .file(mockFile)
                        .param("puppyName", "")
                        .param("puppyAge", "blah")
                        .param("description", "")
                        .param("breed", "invalid"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("postBody", "puppyName"))
                .andExpect(model().attributeHasFieldErrors("postBody", "puppyAge"))
                .andExpect(model().attributeHasFieldErrors("postBody", "description"))
                .andExpect(model().attributeHasFieldErrors("postBody", "breed"))
                .andExpect(model().attributeHasFieldErrors("postBody", "puppyImage"))
                .andExpect(status().isOk());

    }

}

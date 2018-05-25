package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.model.SpecialAuthority;
import ml.echelon133.service.ISpecialAuthorityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class SpecialAuthorityControllerTest {

    private MockMvc mvc;

    @Mock
    private ISpecialAuthorityService authorityService;

    @InjectMocks
    private SpecialAuthorityController authorityController;

    private JacksonTester<List<SpecialAuthority>> jsonSpecialAuthority;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(authorityController).build();
    }

    @Test
    public void emptyJsonObjectIsReturnedWhenThereIsNoSpecialAuthorities() throws Exception {
        // Given
        given(authorityService.getAll()).willReturn(new ArrayList<>());

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/json/special-authorities")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void validJsonObjectIsReturnedWhenThereAreSpecialAuthorities() throws Exception {
        // Prepare authorities
        SpecialAuthority auth1 = new SpecialAuthority("user1", "ROLE_ADMIN");
        SpecialAuthority auth2 = new SpecialAuthority("user2", "ROLE_ADMIN");
        List<SpecialAuthority> authorities = Arrays.asList(auth1, auth2);

        // Prepare expected json object
        JsonContent<List<SpecialAuthority>> jsonAuthorityContent = jsonSpecialAuthority.write(authorities);

        // Given
        given(authorityService.getAll()).willReturn(authorities);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/json/special-authorities")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(jsonAuthorityContent.getJson());
    }
}

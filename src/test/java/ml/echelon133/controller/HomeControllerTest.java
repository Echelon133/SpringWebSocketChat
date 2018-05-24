package ml.echelon133.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private HomeController homeController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void homePageReturnsExpectedContent() throws Exception {
        // When
        MockHttpServletResponse response = mvc.perform(
                get("/")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString().contains("<title>SpringWebSocketChat</title>"));
        assertThat(response.getContentAsString().contains("<h2>All rooms</h2>"));
    }
}

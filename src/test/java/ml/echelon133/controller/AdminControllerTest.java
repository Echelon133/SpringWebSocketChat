package ml.echelon133.controller;

import ml.echelon133.service.IRoomService;
import ml.echelon133.service.ISpecialAuthorityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    private MockMvc mvc;

    @Mock
    private IRoomService roomService;

    @Mock
    private ISpecialAuthorityService authorityService;

    @InjectMocks
    private AdminController adminController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void adminPanelRendersCorrectly() throws Exception {
        // When
        MockHttpServletResponse response = mvc.perform(
                get("/admin")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString().contains("<title>Admin Panel</title>"));
    }
}

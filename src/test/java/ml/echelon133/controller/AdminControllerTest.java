package ml.echelon133.controller;

import ml.echelon133.model.form.RoomForm;
import ml.echelon133.service.IRoomService;
import ml.echelon133.service.ISpecialAuthorityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest
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

    @Test
    public void adminRoomsPanelRendersCorrectly() throws Exception {
        // When
        MockHttpServletResponse response = mvc.perform(
                get("/admin/rooms")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString().contains("<title>Rooms Panel</title>"));
        assertThat(response.getContentAsString().contains("<form action=\"#\""));
    }

    @Test
    public void adminRoomsPanelRoomFormValidationRejectsEmptyName() throws Exception {
        mvc.perform(post("/admin/rooms")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("roomName", ""))
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attributeHasFieldErrorCode("roomForm", "roomName", "Length"))
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attributeErrorCount("roomForm", 1));
    }

    @Test
    public void adminRoomsPanelRoomFormValidationAcceptsValidNames() throws Exception {
        // Checks boundary lengths (3 and 40)
        List<String> names = Arrays.asList("aaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        for (String name : names) {
            mvc.perform(post("/admin/rooms")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("roomName", name))
                    .andExpect(
                            MockMvcResultMatchers.model().hasNoErrors());
        }
    }

    @Test
    public void adminRoomsPanelRoomFormValidationRejectsTooLongNames() throws Exception {
        List<String> names = Arrays.asList("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        for (String name : names) {
            mvc.perform(post("/admin/rooms")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("roomName", ""))
                    .andExpect(
                            MockMvcResultMatchers.model()
                                    .attributeHasFieldErrorCode("roomForm", "roomName", "Length"))
                    .andExpect(
                            MockMvcResultMatchers.model()
                                    .attributeErrorCount("roomForm", 1));
        }
    }

    @Test
    public void adminAuthoritiesPanelRendersCorrectly() throws Exception {
        // When
        MockHttpServletResponse response = mvc.perform(
                get("/admin/authorities")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString().contains("<title>Authorities Panel</title>"));
        assertThat(response.getContentAsString().contains("<form action=\"#\""));
    }

    @Test
    public void adminAuthoritiesPanelSpecialAuthoritiesFormValidationRejectsEmptyUsername() throws Exception {
        mvc.perform(post("/admin/authorities")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "")
                .param("authority", "ROLE_ADMIN"))
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attributeHasFieldErrorCode("specialAuthorityForm", "username", "NotEmpty"))
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attributeErrorCount("specialAuthorityForm", 1));
    }

    @Test
    public void adminAuthoritiesPanelSpecialAuthoritiesFormValidationRejectsInvalidAuthority() throws Exception {
        mvc.perform(post("/admin/authorities")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("authority", "ASDF"))
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attributeHasFieldErrorCode("specialAuthorityForm", "authority", "ValidAuthority"))
                .andExpect(
                        MockMvcResultMatchers.model()
                                .attributeErrorCount("specialAuthorityForm", 1));
    }

    @Test
    public void adminAuthoritiesPanelSpecialAuthoritiesFormValidationAcceptsValidForm() throws Exception {
        mvc.perform(post("/admin/authorities")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user")
                .param("authority", "ROLE_ADMIN"))
                .andExpect(
                        MockMvcResultMatchers.model().hasNoErrors());
    }

    @Test
    public void adminDeleteRoomWithIdReturnsValidResponseIfRoomWasNotDeleted() throws Exception {
        // Expected Json
        String expectedJson = "{\"deleted\":false}";

        // Given
        given(roomService.deleteById("abcd")).willReturn(false);

        // When
        MockHttpServletResponse response = mvc.perform(delete("/admin/rooms/abcd")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void adminDeleteRoomWithIdReturnsValidResponseIfRoomWasDeleted() throws Exception {
        // Expected Json
        String expectedJson = "{\"deleted\":true}";

        // Given
        given(roomService.deleteById("abcd")).willReturn(true);

        // When
        MockHttpServletResponse response = mvc.perform(delete("/admin/rooms/abcd")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }
}

package ml.echelon133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.echelon133.model.Room;
import ml.echelon133.model.exception.RoomNotFoundException;
import ml.echelon133.service.IRoomService;
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

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class RoomControllerTest {

    private MockMvc mvc;

    @Mock
    private IRoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private JacksonTester<List<Room>> jsonRooms;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    public void emptyJsonObjectIsReturnedWhenThereIsNoRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();

        // Given
        given(roomService.getAllRooms()).willReturn(rooms);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/json/rooms")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void validJsonObjectIsReturnedWhenListOfRoomsNotEmpty() throws Exception {
        // Prepare the list of rooms
        Room room1 = new Room("test1");
        room1.setId("aaa");
        Room room2 = new Room("test2");
        room2.setId("bbb");
        List<Room> rooms = Arrays.asList(room1, room2);

        // Prepare the expected JSON
        JsonContent<List<Room>> roomsJsonContent = jsonRooms.write(rooms);

        // Given
        given(roomService.getAllRooms()).willReturn(rooms);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/json/rooms")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(roomsJsonContent.getJson());
    }

    @Test
    public void validRoomTitleIsRenderedWhenVisitingRoom() throws Exception {
        List<String> testRoomNames = Arrays.asList("test", "test room", "Test  room", "test_room", "test-room", "test.room");

        for (String roomName : testRoomNames) {
            String roomUrl = "/rooms/" + roomName;
            String expectedTitle = "<title>Room: " + roomName + "</title>";
            String expectedHeader = "<h1>Room: " + roomName + "</h1>";

            // When
            MockHttpServletResponse response = mvc.perform(get(roomUrl)).andReturn().getResponse();

            // Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString().contains(expectedTitle));
            assertThat(response.getContentAsString().contains(expectedHeader));
        }
    }

    @Test
    public void roomControllerRedirectsTo404IfRoomDoesNotExist() throws Exception {
        // Given
        given(roomService.getByName("test")).willReturn(null);

        // When
        MockHttpServletResponse response = mvc.perform(get("/rooms/test")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString().contains("<title>Room Not Found</title>"));
    }
}

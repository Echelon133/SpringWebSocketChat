package ml.echelon133.model.form;

import org.hibernate.validator.constraints.Length;

public class RoomForm {

    @Length(min = 3, max = 40)
    private String roomName;

    public RoomForm() {}
    public RoomForm(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}

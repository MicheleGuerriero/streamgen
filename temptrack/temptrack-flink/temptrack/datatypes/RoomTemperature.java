package temptrack.datatypes;

import java.io.Serializable;

import com.datastax.driver.mapping.annotations.Table;

@Table(name = "RoomTemperature", keyspace = "TempTrack_ks")
public class RoomTemperature implements Serializable{

    private String roomId;

    private Long temperature;
 
    public RoomTemperature() {

    }

    public RoomTemperature(String roomId, Long temperature) {
        this.roomId = roomId;
        this.temperature = temperature;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }
 
    @Override
    public String toString() {
        return "(" + this.roomId + "," + this.temperature + ")";
    }

}

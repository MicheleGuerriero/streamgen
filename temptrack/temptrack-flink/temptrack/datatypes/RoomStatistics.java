package temptrack.datatypes;

import java.io.Serializable;

public class RoomStatistics implements Serializable{

    private String roomId;

    private Long avgTemp;

    private Long maxTemp;

    private Long timestamp;
 
    public RoomStatistics() {

    }

    public RoomStatistics(String roomId, Long avgTemp, Long maxTemp, Long timestamp) {
        this.roomId = roomId;
        this.avgTemp = avgTemp;
        this.maxTemp = maxTemp;
        this.timestamp = timestamp;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Long avgTemp) {
        this.avgTemp = avgTemp;
    }

    public Long getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Long maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
 
    @Override
    public String toString() {
        return "(" + this.roomId + "," + this.avgTemp + "," + this.maxTemp + "," + this.timestamp + ")";
    }

}

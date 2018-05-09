package temptrack.datatypes;

import java.io.Serializable;

public class Room10MinAheadTempPrediction implements Serializable{

    private String roomId;

    private Long avgPrediction;
 
    public Room10MinAheadTempPrediction() {

    }

    public Room10MinAheadTempPrediction(String roomId, Long avgPrediction) {
        this.roomId = roomId;
        this.avgPrediction = avgPrediction;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getAvgPrediction() {
        return avgPrediction;
    }

    public void setAvgPrediction(Long avgPrediction) {
        this.avgPrediction = avgPrediction;
    }
 
    @Override
    public String toString() {
        return "(" + this.roomId + "," + this.avgPrediction + ")";
    }

}

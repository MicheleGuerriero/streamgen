package advertisinganalysis.datatypes;

import java.io.Serializable;
import java.lang.reflect.Field;

public class AdEvent implements Serializable{

    private String userId;

    private String adId;

    private String pageId;

    private String adType;

    private String eventType;

    private Long eventTime;

    private String ipAddress;

	private String tupleId;

	private String streamId;

	public String getTupleId() {
		return this.tupleId;
	}
	
	public void setTupleId(String tupleId) {
		this.tupleId = tupleId;
	}

	public String getStreamId() {
		return this.streamId;
	}
	
	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
 
    public AdEvent() {

    }

    public AdEvent(String userId, String adId, String pageId, String adType, String eventType, Long eventTime, String ipAddress) {
        this.userId = userId;
        this.adId = adId;
        this.pageId = pageId;
        this.adType = adType;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.ipAddress = ipAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    	
        sb.append("@" + this.eventTime);
        sb.append(" " + this.streamId+ " (");
		sb.append(this.userId + "," + this.adId + "," + this.pageId + "," + this.adType + "," + this.eventType + "," + this.eventTime + "," + this.ipAddress );
        sb.append(")");
        
        return sb.toString();
    }

	@Override
	public int hashCode() {
		return this.tupleId.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		Field t;
		try {
			t = other.getClass().getDeclaredField("tupleId");
			t.setAccessible(true);
			return this.tupleId.equals((String) t.get(other));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return false;

	}

}

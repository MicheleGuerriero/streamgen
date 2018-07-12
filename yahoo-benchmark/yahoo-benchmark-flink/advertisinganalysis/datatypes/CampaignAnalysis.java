package advertisinganalysis.datatypes;

import java.io.Serializable;
import java.lang.reflect.Field;

public class CampaignAnalysis implements Serializable{

    private String campaignId;

    private Integer viewAdEventCount;

    private Long eventTime;

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
 
    public CampaignAnalysis() {

    }

    public CampaignAnalysis(String campaignId, Integer viewAdEventCount, Long eventTime) {
        this.campaignId = campaignId;
        this.viewAdEventCount = viewAdEventCount;
        this.eventTime = eventTime;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getViewAdEventCount() {
        return viewAdEventCount;
    }

    public void setViewAdEventCount(Integer viewAdEventCount) {
        this.viewAdEventCount = viewAdEventCount;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    	
        sb.append("@" + this.eventTime);
        sb.append(" " + this.streamId+ " (");
		sb.append(this.campaignId + "," + this.viewAdEventCount + "," + this.eventTime );
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

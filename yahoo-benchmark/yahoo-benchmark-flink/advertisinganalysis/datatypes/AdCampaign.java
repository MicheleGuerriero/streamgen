package advertisinganalysis.datatypes;

import java.io.Serializable;
import java.lang.reflect.Field;

public class AdCampaign implements Serializable{

    private String adId;

    private String campaignId;

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
 
    public AdCampaign() {

    }

    public AdCampaign(String adId, String campaignId, Long eventTime) {
        this.adId = adId;
        this.campaignId = campaignId;
        this.eventTime = eventTime;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
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
		sb.append(this.adId + "," + this.campaignId + "," + this.eventTime );
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

package advertisinganalysis.functions;

import advertisinganalysis.datatypes.AdEvent;
import advertisinganalysis.datatypes.Ad;
import advertisinganalysis.datatypes.AdCampaign;
import advertisinganalysis.datatypes.CampaignAnalysis;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import java.util.*;

public class ExtractAd extends RichMapFunction<AdEvent, Ad> {

	@Override 
	public void open(Configuration conf) {
		
	}
	
	@Override
	public Ad map(AdEvent tuple) throws Exception {
		Ad toReturn = new Ad();

toReturn.setAdId(tuple.getAdId());
toReturn.setEventTime(tuple.getEventTime());

return toReturn;
	}
	
}


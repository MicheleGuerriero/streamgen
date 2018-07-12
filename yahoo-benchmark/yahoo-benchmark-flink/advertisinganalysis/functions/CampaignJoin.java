package advertisinganalysis.functions;

import advertisinganalysis.datatypes.AdEvent;
import advertisinganalysis.datatypes.Ad;
import advertisinganalysis.datatypes.AdCampaign;
import advertisinganalysis.datatypes.CampaignAnalysis;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import java.util.*;

public class CampaignJoin extends RichCoFlatMapFunction<String, Ad, AdCampaign> {

			List<AdCampaign> adsToCampaigns;
	
	@Override 
	public void open(Configuration conf) {
			adsToCampaigns = new ArrayList<AdCampaign>();
	}
	
	@Override
	public void flatMap1(String tuple, Collector<AdCampaign> out) throws Exception {
		AdCampaign toAdd = new AdCampaign();

String[] fields = tuple.split(",");

toAdd.setCampaignId(fields[0]);
toAdd.setAdId(fields[1]);
toAdd.setEventTime(-1L);

this.adsToCampaigns.add(toAdd);
	}

	@Override
	public void flatMap2(Ad tuple, Collector<AdCampaign> out) throws Exception {
				AdCampaign output = null;

		for (AdCampaign a : this.adsToCampaigns) {
			if (a.getAdId().equals(tuple.getAdId())) {
				output.setAdId(a.getAdId());
				output.setCampaignId(a.getCampaignId());
				output.setEventTime(tuple.getEventTime());
			}
		}

		out.collect(output);
	}
	
}


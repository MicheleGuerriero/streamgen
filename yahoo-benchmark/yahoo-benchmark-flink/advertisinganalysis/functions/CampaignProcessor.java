package advertisinganalysis.functions;

import advertisinganalysis.datatypes.AdEvent;
import advertisinganalysis.datatypes.Ad;
import advertisinganalysis.datatypes.AdCampaign;
import advertisinganalysis.datatypes.CampaignAnalysis;

import java.util.*;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class CampaignProcessor implements WindowFunction<AdCampaign, CampaignAnalysis, Tuple, TimeWindow> {
	
	@Override
	public void apply(Tuple key, TimeWindow window, Iterable<AdCampaign> windowContentIterator, Collector<CampaignAnalysis> out) throws Exception {
		
		List<AdCampaign> windowContent = new  ArrayList<AdCampaign>();
		for(AdCampaign x: windowContentIterator){
			windowContent.add(x);
		}
		
		int count = 0;

for(AdCampaign ad: windowContent){
	count++;
}

CampaignAnalysis output = new CampaignAnalysis();
output.setCampaignId(key.getField(0));
output.setViewAdEventCount(count);
output.setEventTime(window.getEnd());

out.collect(output);
	}


}

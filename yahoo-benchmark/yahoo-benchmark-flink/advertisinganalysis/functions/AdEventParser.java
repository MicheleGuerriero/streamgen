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

public class AdEventParser extends RichMapFunction<String, AdEvent> {

	@Override 
	public void open(Configuration conf) {
		
	}
	
	@Override
	public AdEvent map(String tuple) throws Exception {
		            String[] fields = tuple.split(",");
            return new AdEvent(
                    fields[0],
                    fields[1],
                    fields[2],
                    fields[3],
                    fields[4],
                    Long.parseLong(fields[5]),
                    fields[6]
                    );
	}
	
}


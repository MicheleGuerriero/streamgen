package temptrack.functions;

import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class TemperatureParser extends RichMapFunction<String, RoomTemperature> {

	@Override 
	public void open(Configuration conf) {
		
	}
	
	@Override
	public RoomTemperature map(String tuple) throws Exception {
		            String[] fields = tuple.split(",");
            return new RoomTemperature(
                    fields[0],
                    Long.parseLong(fields[1])
                    );
	}
	
}


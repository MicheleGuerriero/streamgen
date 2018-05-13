package temptrack.functions;

import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;

import org.apache.spark.api.java.function.Function;


public class TemperatureParser implements Function<String, RoomTemperature>{

	private static final long serialVersionUID = 1L;

	@Override
	public RoomTemperature call(String tuple) throws Exception {
		            String[] fields = tuple.split(",");
            return new RoomTemperature(
                    fields[0],
                    Long.parseLong(fields[1])
                    );
	}

}


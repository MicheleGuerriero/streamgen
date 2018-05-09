package temptrack.functions;

import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;

import java.util.*;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class ComputeRoomStatistics implements WindowFunction<RoomTemperature, RoomStatistics, Tuple, TimeWindow> {
	
	@Override
	public void apply(Tuple key, TimeWindow window, Iterable<RoomTemperature> windowContentIterator, Collector<RoomStatistics> out) throws Exception {
		
		List<RoomTemperature> windowContent = new  ArrayList<RoomTemperature>();
		for(RoomTemperature x: windowContentIterator){
			windowContent.add(x);
		}
		
		Long avgTemp = new Long(0);
Long maxTemp =  new Long(-9999);

for(RoomTemperature r: windowContent){
	avgTemp = avgTemp + r.getTemperature();
	if(maxTemp < r.getTemperature()){
		maxTemp = r.getTemperature();
	}
}

avgTemp = avgTemp/windowContent.size();

out.collect(new RoomStatistics(key.getField(0), avgTemp, maxTemp, window.getEnd()));
	}


}

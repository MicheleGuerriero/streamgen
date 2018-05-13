package temptrack.functions;

import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;

import org.apache.spark.api.java.function.FlatMapFunction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import scala.Tuple2;

public class ComputeRoomStatistics implements FlatMapFunction<Tuple2<String, List<Tuple2<RoomTemperature, Long>>>,RoomStatistics>{

private static final long serialVersionUID = 1L;

@Override
public Iterator<RoomStatistics> call(Tuple2<String, List<Tuple2<RoomTemperature, Long>>> t) throws Exception {


	List<RoomTemperature> windowContent = new  ArrayList<RoomTemperature>();
	
	for(Tuple2<RoomTemperature, Long> x: t._2) {
		windowContent.add(x._1);
	}

	String key = t._1;
	List<RoomStatistics> out= new ArrayList<RoomStatistics>();

	Window window = new Window();
	
	window.setStart(t._2.get(0)._2);
	window.setEnd(t._2.get(t._2.size())._2);

	
	Long avgTemp = new Long(0);
Long maxTemp =  new Long(-9999);

for(RoomTemperature r: windowContent){
	avgTemp = avgTemp + r.getTemperature();
	if(maxTemp < r.getTemperature()){
		maxTemp = r.getTemperature();
	}
}

avgTemp = avgTemp/windowContent.size();

out.add(new RoomStatistics(key, avgTemp, maxTemp, window.getEnd()));        
	
	return out.iterator();
}

private class Window {
	private long startTime;
	private long endTime;
	
	public long getEnd() {
		return this.endTime;
	}
	
	public long getStart() {
		return this.startTime;
	}
	
	public void setStart(long startTime) {
		this.startTime = startTime;
	}
	
	public void setEnd(long endTime) {
		this.endTime = endTime;
	}	
	
}

}

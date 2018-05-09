package temptrack.application;

import temptrack.functions.TemperatureParser;
import temptrack.functions.ComputeRoomStatistics;
import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;
import temptrack.functions.MonitorCriticalPrediction;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import org.apache.flink.streaming.connectors.kafka.*;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

import org.apache.flink.streaming.connectors.cassandra.CassandraSink;
import com.datastax.driver.mapping.Mapper;


public class TempTrack {

    public static void main(String[] args) throws Exception {

       final StreamExecutionEnvironment env = StreamExecutionEnvironment
               .getExecutionEnvironment();
       
       //uncomment the below if you want to set the default parallelism for the project.
       //env.setParallelism(1);

				DataStream<String> rawTemperatures = env.socketTextStream("localhost", 9999);
			DataStream<RoomTemperature> parsedTemperatures = rawTemperatures
				.map(new TemperatureParser())
				.setParallelism(2);
		
			DataStream<RoomStatistics> roomStatistics = parsedTemperatures
		      	.keyBy("roomId")
		        .timeWindow(Time.minutes(2))
		        .apply(new ComputeRoomStatistics())
				;
		
			DataStream<RoomStatistics> criticalTemperatures = roomStatistics
		      	.keyBy("roomId")
				.filter((RoomStatistics tuple) -> true)
				;
		
			FlinkKafkaProducer010<String> ReportCriticalTemperature_producer = new FlinkKafkaProducer010<String>(
			        "localhost:9092",            
			        "ReportCriticalTemperature",                 
			        new SimpleStringSchema()); 
		
			 criticalTemperatures.map((RoomStatistics x) -> x.toString()).addSink(ReportCriticalTemperature_producer);
			roomStatistics
				.writeAsText("/home/user/room-statistics.csv")
				.setParallelism(3);
			DataStream<RoomTemperature> cleanedData = parsedTemperatures
		      	.keyBy("roomId")
				.filter((RoomTemperature tuple) -> tuple.getTemperature() < 9999 & 
		tuple.getTemperature() > -9999 & 
		tuple.getTemperature() != null  &
		tuple.getRoomId() !=  null)
				.setParallelism(3);
		
			CassandraSink.addSink(cleanedData)
		    	.setHost("localhost")
		    	.setMapperOptions(() -> new Mapper.Option[]{Mapper.Option.saveNullFields(true)})
		    	.build();
			DataStream<Room10MinAheadTempPrediction> avgTempPredictions = roomStatistics
		      	.keyBy("roomId")
		        .timeWindow(Time.minutes(10))
		        .apply(new MonitorCriticalPrediction())
				;
		
			FlinkKafkaProducer010<String> ReportCriticalPrediction_producer = new FlinkKafkaProducer010<String>(
			        "localhost:9092",            
			        "ReportCriticalPrediction",                 
			        new SimpleStringSchema()); 
		
			 avgTempPredictions.map((Room10MinAheadTempPrediction x) -> x.toString()).addSink(ReportCriticalPrediction_producer);

       JobExecutionResult result = env.execute();
       System.out.println("EXECUTION TIME: " + result.getNetRuntime(TimeUnit.SECONDS));

    }
}

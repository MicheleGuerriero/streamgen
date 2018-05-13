package temptrack.application;

import temptrack.functions.TemperatureParser;
import temptrack.functions.ComputeRoomStatistics;
import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;
import temptrack.functions.MonitorCriticalPrediction;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import scala.Tuple2;
import org.apache.spark.*;
import org.apache.spark.api.java.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.api.java.function.*;
import java.util.*;
import org.apache.spark.api.java.Optional;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;


import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;



public class TempTrack {

    public static void main(String[] args) throws Exception {

	   Logger.getLogger("org").setLevel(Level.OFF);
	   Logger.getLogger("akka").setLevel(Level.OFF);
	
	   SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("TempTrack");
	   JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));



			   JavaReceiverInputDStream<String> rawTemperatures = jssc.socketTextStream("localhost", 9999,
						StorageLevels.MEMORY_AND_DISK_SER);
			   JavaDStream<RoomTemperature> parsedTemperatures = rawTemperatures
				  	.repartition(2)
					.map(new TemperatureParser());
				JavaDStream<RoomStatistics> roomStatistics = parsedTemperatures.map(x-> new Tuple2<>(x, System.currentTimeMillis()))
					.mapToPair((x -> new Tuple2<>(x._1.getRoomId(), Arrays.asList(x))))
					.reduceByKeyAndWindow(new Function2<List<Tuple2<RoomTemperature, Long>>, List<Tuple2<RoomTemperature, Long>>, List<Tuple2<RoomTemperature, Long>>>() {
						@Override 
						public List<Tuple2<RoomTemperature, Long>> call(List<Tuple2<RoomTemperature, Long>> v1, List<Tuple2<RoomTemperature, Long>> v2) throws Exception {
							Set<Tuple2<RoomTemperature, Long>> set = new HashSet<Tuple2<RoomTemperature, Long>>();
		
							set.addAll(v1);
							set.addAll(v2);
		
							return new ArrayList<Tuple2<RoomTemperature, Long>>(set);
						}
					}
					, Durations.minutes(2),
					Durations.minutes(2)
		)
					.flatMap(new ComputeRoomStatistics());
		
		
			   JavaDStream<RoomStatistics> criticalTemperatures = roomStatistics
				  	.repartition(1)
					.filter((RoomStatistics tuple) -> tuple.getMaxTemp() > 65);
		
			   Map<String, Object> ReportCriticalTemperature_kafkaParams = new HashMap<>();
			   ReportCriticalTemperature_kafkaParams.put("bootstrap.servers", "localhost:9092");
			   ReportCriticalTemperature_kafkaParams.put("key.deserializer", StringDeserializer.class);
			   ReportCriticalTemperature_kafkaParams.put("value.deserializer", StringDeserializer.class);
			   ReportCriticalTemperature_kafkaParams.put("group.id", "ReportCriticalTemperature_groupId");
			   ReportCriticalTemperature_kafkaParams.put("auto.offset.reset", "latest");
			   ReportCriticalTemperature_kafkaParams.put("enable.auto.commit", false);
		
		       criticalTemperatures.foreachRDD(rdd ->
		       rdd.foreachPartition(partition -> {
		
		    	   while(partition.hasNext()) {
		                 KafkaProducer producer = new KafkaProducer<String, String>(ReportCriticalTemperature_kafkaParams);
		                 ProducerRecord<String, String> message=new ProducerRecord<String, String>("ReportCriticalTemperature",null,partition.next().toString());
		                 producer.send(message);	    		   
		    	   }
		       }
		       ));
			   JavaDStream<RoomTemperature> cleanedData = parsedTemperatures
				  	.repartition(3)
					.filter((RoomTemperature tuple) -> tuple.getTemperature() < 9999 & 
		tuple.getTemperature() > -9999 & 
		tuple.getTemperature() != null  &
		tuple.getRoomId() !=  null);
		       cleanedData.foreachRDD(rdd ->
		       		javaFunctions(rdd).writerBuilder("cleanedData_keyspace", "cleanedData", mapToRow(RoomTemperature.class)).saveToCassandra());  
				JavaDStream<Room10MinAheadTempPrediction> avgTempPredictions = roomStatistics.map(x-> new Tuple2<>(x, System.currentTimeMillis()))
					.mapToPair((x -> new Tuple2<>(x._1.getRoomId(), Arrays.asList(x))))
					.reduceByKeyAndWindow(new Function2<List<Tuple2<RoomStatistics, Long>>, List<Tuple2<RoomStatistics, Long>>, List<Tuple2<RoomStatistics, Long>>>() {
						@Override 
						public List<Tuple2<RoomStatistics, Long>> call(List<Tuple2<RoomStatistics, Long>> v1, List<Tuple2<RoomStatistics, Long>> v2) throws Exception {
							Set<Tuple2<RoomStatistics, Long>> set = new HashSet<Tuple2<RoomStatistics, Long>>();
		
							set.addAll(v1);
							set.addAll(v2);
		
							return new ArrayList<Tuple2<RoomStatistics, Long>>(set);
						}
					}
					, Durations.minutes(10),
					Durations.minutes(10)
		)
					.flatMap(new MonitorCriticalPrediction());
		
		
		
			   Map<String, Object> ReportCriticalPrediction_kafkaParams = new HashMap<>();
			   ReportCriticalPrediction_kafkaParams.put("bootstrap.servers", "localhost:9092");
			   ReportCriticalPrediction_kafkaParams.put("key.deserializer", StringDeserializer.class);
			   ReportCriticalPrediction_kafkaParams.put("value.deserializer", StringDeserializer.class);
			   ReportCriticalPrediction_kafkaParams.put("group.id", "ReportCriticalPrediction_groupId");
			   ReportCriticalPrediction_kafkaParams.put("auto.offset.reset", "latest");
			   ReportCriticalPrediction_kafkaParams.put("enable.auto.commit", false);
		
		       avgTempPredictions.foreachRDD(rdd ->
		       rdd.foreachPartition(partition -> {
		
		    	   while(partition.hasNext()) {
		                 KafkaProducer producer = new KafkaProducer<String, String>(ReportCriticalPrediction_kafkaParams);
		                 ProducerRecord<String, String> message=new ProducerRecord<String, String>("ReportCriticalPrediction",null,partition.next().toString());
		                 producer.send(message);	    		   
		    	   }
		       }
		       ));

		jssc.start(); // Start the computation
		jssc.awaitTermination(); // Wait for the computation to terminate
    }
}

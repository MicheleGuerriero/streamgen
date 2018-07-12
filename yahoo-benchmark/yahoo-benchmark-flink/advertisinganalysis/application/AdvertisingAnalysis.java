package advertisinganalysis.application;

import advertisinganalysis.functions.AdEventParser;
import advertisinganalysis.functions.ExtractAd;
import advertisinganalysis.functions.CampaignJoin;
import advertisinganalysis.functions.CampaignProcessor;
import advertisinganalysis.datatypes.AdEvent;
import advertisinganalysis.datatypes.Ad;
import advertisinganalysis.datatypes.AdCampaign;
import advertisinganalysis.datatypes.CampaignAnalysis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import org.apache.flink.streaming.connectors.kafka.*;
import org.apache.flink.api.common.serialization.SimpleStringSchema;



public class AdvertisingAnalysis {

    public static void main(String[] args) throws Exception {

       final StreamExecutionEnvironment env = StreamExecutionEnvironment
               .getExecutionEnvironment();
       
       //uncomment the below if you want to set the default parallelism for the project.
       //env.setParallelism(1);

				Properties MessageStream_kafkaParams = new Properties();
				MessageStream_kafkaParams.setProperty("bootstrap.servers", ":9092");
				MessageStream_kafkaParams.setProperty("group.id", "MessageStream_groupId");
				DataStream<String> rawAdEvents = env
					.addSource(new FlinkKafkaConsumer010<>("MessageStream", new SimpleStringSchema(), MessageStream_kafkaParams));
				DataStream<String> adsToCampaign = env.readTextFile("/home/utente/eclipse-workspace/streamgen/yahoo-benchmark/input.txt");
					;
		
			DataStream<AdEvent> adEvents = rawAdEvents
				.map(new AdEventParser())
				;
		
			DataStream<AdEvent> viewAdEvents = adEvents
				.filter((AdEvent tuple) -> tuple.getAdType().equals("view"))
				;
		
			DataStream<Ad> viewedAds = viewAdEvents
				.map(new ExtractAd())
				;
		
			DataStream<AdCampaign> adsPerChampaign = adsToCampaign
				.broadcast()
				.connect(
				viewedAds
				)
				.flatMap(new CampaignJoin())
				;
		
			DataStream<CampaignAnalysis> results = adsPerChampaign
		      	.keyBy("campaignId")
		        .timeWindow(Time.seconds(10))
		        .apply(new CampaignProcessor())
				;
		
			FlinkKafkaProducer010<String> ChampaignSink_producer = new FlinkKafkaProducer010<String>(
			        "localhost:9092",            
			        "ChampaignSink",                 
			        new SimpleStringSchema()); 
		
			 results.map((CampaignAnalysis x) -> x.toString()).addSink(ChampaignSink_producer);

       JobExecutionResult result = env.execute();
       System.out.println("EXECUTION TIME: " + result.getNetRuntime(TimeUnit.SECONDS));

    }
}

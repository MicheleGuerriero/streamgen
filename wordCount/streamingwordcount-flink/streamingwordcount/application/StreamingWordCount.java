package streamingwordcount.application;

import streamingwordcount.datatypes.WordCount;
import streamingwordcount.datatypes.WordToken;
import streamingwordcount.functions.LineSplitter;
import streamingwordcount.functions.WordCounter;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class StreamingWordCount {

    public static void main(String[] args) throws Exception {

       final StreamExecutionEnvironment env = StreamExecutionEnvironment
               .getExecutionEnvironment();
       
       //uncomment the below if you want to set the default parallelism for the project.
       //env.setParallelism(1);

				DataStream<String> text = env.socketTextStream("localhost", 9999);
			DataStream<WordToken> tokens = text
				.flatMap(new LineSplitter())
				.setParallelism(4);
		
			DataStream<WordCount> counts = tokens
		      	.keyBy("word")
		        .timeWindow(Time.seconds(3))
		        .apply(new WordCounter())
				.setParallelism(4);
		
			counts
				.keyBy("word")
				.writeAsText("/home/utente/word-count-output.txt")
				.setParallelism(1);

       JobExecutionResult result = env.execute();
       System.out.println("EXECUTION TIME: " + result.getNetRuntime(TimeUnit.SECONDS));

    }
}

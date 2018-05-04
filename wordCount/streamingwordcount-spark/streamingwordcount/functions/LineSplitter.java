package streamingwordcount.functions;

import streamingwordcount.datatypes.WordCount;
import streamingwordcount.datatypes.WordToken;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class LineSplitter extends RichFlatMapFunction<String, WordToken> {

	@Override 
	public void open(Configuration conf) {
		
	}
	
	@Override
	public void flatMap(String tuple, Collector<WordToken> out) throws Exception {
		String [] words = tuple.split(" ");

for(String word: words){
out.collect(new WordToken(word,1));
}
	}
	
}


package streamingwordcount.functions;

import streamingwordcount.datatypes.WordCount;
import streamingwordcount.datatypes.WordToken;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class WordCounter implements WindowFunction<WordToken, WordCount, Tuple, TimeWindow> {
	
	@Override
	public void apply(Tuple key, TimeWindow window, Iterable<WordToken> windowContent, Collector<WordCount> out) throws Exception {
		int count = 0;

for(WordToken token: windowContent){
	count++;
}

out.collect(new WordCount(key.getField(0),count,window.getEnd()));
	}


}

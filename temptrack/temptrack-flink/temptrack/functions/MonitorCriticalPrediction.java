package temptrack.functions;

import temptrack.datatypes.RoomTemperature;
import temptrack.datatypes.RoomStatistics;
import temptrack.datatypes.Room10MinAheadTempPrediction;

import java.util.*;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class MonitorCriticalPrediction implements WindowFunction<RoomStatistics, Room10MinAheadTempPrediction, Tuple, TimeWindow> {
	
	@Override
	public void apply(Tuple key, TimeWindow window, Iterable<RoomStatistics> windowContentIterator, Collector<Room10MinAheadTempPrediction> out) throws Exception {
		
		List<RoomStatistics> windowContent = new  ArrayList<RoomStatistics>();
		for(RoomStatistics x: windowContentIterator){
			windowContent.add(x);
		}
		
				int[] x = new int[windowContent.size()];
		long[] y = new long[windowContent.size()];

		int j = 0;
		
		for(RoomStatistics rs: windowContent) {
			y[j] = rs.getAvgTemp();
			x[j] = j + 1;
			j = j + 1;
		}
		
	    int N;
	    double alpha, beta;
	    double R2;
	    double svar, svar0, svar1;
		
		if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        N = x.length;

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < N; i++) sumx  += x[i];
        for (int i = 0; i < N; i++) sumx2 += x[i]*x[i];
        for (int i = 0; i < N; i++) sumy  += y[i];
        double xbar = sumx / N;
        double ybar = sumy / N;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < N; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        beta  = xybar / xxbar;
        alpha = ybar - beta * xbar;

        // more statistical analysis
        double rss = 0.0;      // residual sum of squares
        double ssr = 0.0;      // regression sum of squares
        for (int i = 0; i < N; i++) {
            double fit = beta*x[i] + alpha;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = N-2;
        R2    = ssr / yybar;
        svar  = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar/N + xbar*xbar*svar1;
        
        out.collect(new Room10MinAheadTempPrediction(key.getField(0), new Double(beta*(windowContent.size()+5) + alpha).longValue()));
		
	}


}

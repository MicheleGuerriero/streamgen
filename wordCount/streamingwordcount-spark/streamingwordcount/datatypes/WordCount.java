package streamingwordcount.datatypes;

public class WordCount {

    private String word;

    private Integer count;

    private Long timestamp;
 
    public WordCount() {

    }

    public WordCount(String word, Integer count, Long timestamp) {
        this.word = word;
        this.count = count;
        this.timestamp = timestamp;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
 
    @Override
    public String toString() {
        return "(" + this.word + "," + this.count + "," + this.timestamp + ")";
    }

}

package streamingwordcount.datatypes;

public class WordToken {

    private String word;

    private Integer token;
 
    public WordToken() {

    }

    public WordToken(String word, Integer token) {
        this.word = word;
        this.token = token;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }
 
    @Override
    public String toString() {
        return "(" + this.word + "," + this.token + ")";
    }

}

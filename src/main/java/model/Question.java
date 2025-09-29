package model;

public class Question {
    private String questionText;
    private String[] options;
    private int correctIndex;
    private int timeLimitSeconds;

    public Question(String questionText, String[] options, int correctIndex, int timeLimitSeconds) {
        this.questionText = questionText;
        this.options = options;
        this.correctIndex = correctIndex;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public int getTimeLimitSeconds() { return timeLimitSeconds; }
}
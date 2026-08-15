package it.uniroma2.dicii.ispw.sostudy.model;

public class Choice {
    private int id;
    private String content;

    public Choice(int id, String content) {
        this.id = id;
        this.content = content;
    }
    public Choice(String content) {this.content = content; }

    public String getContent() {return this.content;}
    public void setContent(String content) {this.content = content;}
    public int getChoiceID() {return this.id;}
}

package jp.co.faithcreates.meowziq.model;

public class ServerStatus {
    private final String text;
    private final String song;

    public ServerStatus(String text, String song) {
        this.text = text;
        this.song = song;
    }

    public String getText() {
        return text;
    }

    public String getSong() {
        return song;
    }
}

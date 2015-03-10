package jp.co.faithcreates.party_play.model;

public class Artist {
    public String id;
    public String content;

    public Artist(String id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}

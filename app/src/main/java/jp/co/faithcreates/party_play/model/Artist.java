package jp.co.faithcreates.party_play.model;

public class Artist implements Comparable<Artist> {
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

    @Override
    public int compareTo(Artist another) {
        return this.content.compareTo(another.content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (content != null ? !content.equals(artist.content) : artist.content != null)
            return false;
        if (id != null ? !id.equals(artist.id) : artist.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}

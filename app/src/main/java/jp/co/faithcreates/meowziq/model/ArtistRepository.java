package jp.co.faithcreates.meowziq.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ArtistRepository {

    private static List<Artist> artistList = new ArrayList<>();

    private static Map<String, Artist> artistMap = new HashMap<>();

    private static void addItem(Artist item) {
        artistList.add(item);
        artistMap.put(item.id, item);
    }

    public static List<Artist> getArtistList() {
        return artistList;
    }

    public static void loadFromSongList(List<Song> songList) {
        artistList.clear();
        artistMap.clear();
        Set<Artist> artistSet = new TreeSet<>();
        for (Song song : songList) {
            Artist artist = new Artist("id", song.getArtist());
            artistSet.add(artist);
        }
        for (Artist artist : artistSet) {
            addItem(artist);
        }
    }

}

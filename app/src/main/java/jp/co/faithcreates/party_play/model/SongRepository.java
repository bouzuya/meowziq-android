package jp.co.faithcreates.party_play.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongRepository {

    public static List<Song> mSongList = new ArrayList<Song>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Song> mSongMap = new HashMap<String, Song>();

    private static void addItem(Song item) {
        mSongList.add(item);
        mSongMap.put(item.id, item);
    }

    public static void loadFromContentResolver(ContentResolver resolver) {
        mSongList.clear();
        mSongMap.clear();

        boolean useInternal = false;
        Uri uri = useInternal
                ? MediaStore.Audio.Media.INTERNAL_CONTENT_URI
                : MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Song song = new Song(id, title);
            song.setArtist(artist);
            song.setPath(path);
            song.setTitle(title);
            addItem(song);
        }

        // TODO: try catch
        cursor.close();

        ArtistRepository.loadFromSongList(mSongList);
    }

}

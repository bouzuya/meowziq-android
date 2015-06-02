package jp.co.faithcreates.meowziq.model;

import java.util.List;

public class ServerSongRepository {

    public static String baseUrl;

    public static void loadFromServer(final OnLoadListener listener) {
        new ServerSongRequest(baseUrl) {
            @Override
            protected void onPostExecute(List<Song> songs) {
                listener.onLoad(songs);
            }
        }.execute();
    }

    public interface OnLoadListener {
        void onLoad(List<Song> songs);
    }
}

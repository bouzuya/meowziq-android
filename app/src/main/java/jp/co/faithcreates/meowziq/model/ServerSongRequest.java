package jp.co.faithcreates.meowziq.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerSongRequest extends AsyncTask<Void, Void, List<Song>> {
    public static final String TAG = "meowziq";

    private final String baseUrl;

    public ServerSongRequest(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    protected List<Song> doInBackground(Void... voids) {
        String url = baseUrl + "/songs";

        Log.d(TAG, "GET " + url);
        HttpGet httpGet = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            Log.d(TAG, httpResponse.getStatusLine().toString());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            httpResponse.getEntity().writeTo(outputStream);
            String jsonString = outputStream.toString();
            Log.d(TAG, jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            List<Song> songList = buildSongList(jsonArray);

            for (Song i : songList) {
                Log.d(TAG, i.toString());
            }

            return songList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Song> buildSongList(JSONArray jsonArray) throws JSONException {
        List<Song> songList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Song song = buildSong(jsonObject);
            songList.add(song);
        }
        return songList;
    }

    private Song buildSong(JSONObject jsonObject) throws JSONException {
        String s = jsonObject.getString("artist") + " - " + jsonObject.getString("title");
        Song song = new Song(jsonObject.getString("id"), s);
        song.setArtist(jsonObject.getString("artist"));
        song.setTitle(jsonObject.getString("title"));
        return song;
    }
}

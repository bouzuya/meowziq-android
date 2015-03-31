package jp.co.faithcreates.party_play.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class SongRequest extends AsyncTask<Song, Void, String> {
    private final String baseUrl;

    public SongRequest(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    protected String doInBackground(Song... songs) {

        String url = baseUrl + "/songs";

        Log.d("party-play", "request " + url);
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF-8"));

        Song song = songs[0];
        String path = song.getPath();
        byte[] binary = null;
        try {
            File file = new File(path);
            FileInputStream input = new FileInputStream(file);
            binary = new byte[input.available()];
            input.read(binary);
            input.close();
        } catch (IOException e) {
            Log.d("party-play", e.getMessage());
            e.printStackTrace();
        }
        builder.addBinaryBody("file", binary, ContentType.APPLICATION_OCTET_STREAM, path);
        builder.addTextBody("title", song.getTitle());
        builder.addTextBody("artist", song.getArtist());

        httpPost.setEntity(builder.build());

        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = client.execute(httpPost);
            Log.d("party-play", httpResponse.getStatusLine().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

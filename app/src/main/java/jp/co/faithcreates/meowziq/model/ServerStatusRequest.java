package jp.co.faithcreates.meowziq.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerStatusRequest extends AsyncTask<Void, Void, ServerStatus> {
    public static final String TAG = "meowziq";

    private final String baseUrl;

    public ServerStatusRequest(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    protected ServerStatus doInBackground(Void... voids) {
        String url = baseUrl + "/status";
        Log.d(TAG, "GET " + url);
        String fetchedString = fetch(url);
        return buildServerStatus(fetchedString);
    }

    private String fetch(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            Log.d(TAG, httpResponse.getStatusLine().toString());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            httpResponse.getEntity().writeTo(outputStream);
            String jsonString = outputStream.toString();
            Log.d(TAG, jsonString);
            return jsonString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerStatus buildServerStatus(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String text = jsonObject.getString("text");
            JSONObject songJsonObject = jsonObject.getJSONObject("song");
            String artist = songJsonObject.getString("artist");
            String title = songJsonObject.getString("title");
            return new ServerStatus(text, artist + " - " + title);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

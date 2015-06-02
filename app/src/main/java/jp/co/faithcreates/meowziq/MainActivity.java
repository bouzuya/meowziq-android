package jp.co.faithcreates.meowziq;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import jp.co.faithcreates.meowziq.model.Artist;
import jp.co.faithcreates.meowziq.model.ServerSongRepository;
import jp.co.faithcreates.meowziq.model.ServerStatusRepository;
import jp.co.faithcreates.meowziq.model.Song;
import jp.co.faithcreates.meowziq.model.SongRepository;
import jp.co.faithcreates.meowziq.model.SongRequest;
import jp.co.faithcreates.meowziq.ui.ArtistFragment;
import jp.co.faithcreates.meowziq.ui.HomeFragment;
import jp.co.faithcreates.meowziq.ui.ServerSongFragment;
import jp.co.faithcreates.meowziq.ui.SettingsActivity;
import jp.co.faithcreates.meowziq.ui.SongFragment;

public class MainActivity extends ActionBarActivity implements SongFragment.OnFragmentInteractionListener, ArtistFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener {

    private static final String TAG = "meowziq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        transaction.add(R.id.main_layout, homeFragment);
        transaction.commit();
    }

    private void request(final Song song) {
        Log.d(TAG, "request");
        Log.d(TAG, song.getPath());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        String baseUrl = prefs.getString("server_url", null);

        if (baseUrl == null) {
            Log.d(TAG, "url is empty");
            return;
        }

        final Activity activity = this;
        Toast.makeText(this, song.getTitle() + "を転送しています。", Toast.LENGTH_LONG).show();
        final SongRequest task = new SongRequest(baseUrl) {
            @Override
            protected void onPostExecute(String result) {
                String message = song.getTitle() + "の転送を完了しました。";
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        };
        task.execute(song);
    }

    private void setServerUrl() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        String baseUrl = prefs.getString("server_url", null);

        if (baseUrl == null) {
            Log.d(TAG, "url is empty");
            return;
        }

        ServerSongRepository.baseUrl = baseUrl;
        ServerStatusRepository.baseUrl = baseUrl;
    }

    private void reload() {
        SongRepository.loadFromContentResolver(getContentResolver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_reload:
                reload();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Artist artist) {
        Log.d(TAG, "selected artist :" + artist.toString());

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SongFragment songFragment = new SongFragment();
        Bundle bundle = new Bundle();
        bundle.putString("artistName", artist.content);
        songFragment.setArguments(bundle);

        transaction.replace(R.id.main_layout, songFragment);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(final Song song) {
        Log.d(TAG, "selected song : " + song.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("転送しますか？")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request(song);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(String name) {
        if (name.equals("local")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    ArtistFragment artistFragment = new ArtistFragment();
                    transaction.replace(R.id.main_layout, artistFragment);
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();

                    reload(); // FIXME:
                }
            });
        } else if (name.equals("remote")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setServerUrl(); // FIXME:

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    ServerSongFragment serverSongFragment = new ServerSongFragment();
                    transaction.replace(R.id.main_layout, serverSongFragment);
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                }
            });
        } else {
            Log.e(TAG, name + " is unkown button");
        }
    }
}

package jp.co.faithcreates.party_play;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import jp.co.faithcreates.party_play.model.Artist;
import jp.co.faithcreates.party_play.model.Song;
import jp.co.faithcreates.party_play.model.SongRepository;
import jp.co.faithcreates.party_play.model.SongRequest;
import jp.co.faithcreates.party_play.ui.ArtistFragment;
import jp.co.faithcreates.party_play.ui.SettingsActivity;
import jp.co.faithcreates.party_play.ui.SongFragment;

public class MainActivity extends ActionBarActivity implements SongFragment.OnFragmentInteractionListener, ArtistFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ArtistFragment artistFragment = new ArtistFragment();
        transaction.add(R.id.main_layout, artistFragment);
        transaction.commit();

        reload();
    }

    private void request(Song song) {
        Log.d("party-play", "request");
        Log.d("party-play", song.getPath());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        String baseUrl = prefs.getString("server_url", null);

        if (baseUrl == null) {
            Log.d("party-play", "url is empty");
            return;
        }

        SongRequest task = new SongRequest(baseUrl);
        task.execute(song);
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
        Log.d("party-play", "selected artist :" + artist.toString());

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
    public void onFragmentInteraction(Song song) {
        Log.d("party-play", "selected song : " + song.toString());
        request(song);
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
}

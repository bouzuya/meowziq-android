package jp.co.faithcreates.meowziq.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.faithcreates.meowziq.R;
import jp.co.faithcreates.meowziq.model.ServerSongRepository;
import jp.co.faithcreates.meowziq.model.ServerStatus;
import jp.co.faithcreates.meowziq.model.ServerStatusRepository;
import jp.co.faithcreates.meowziq.model.Song;

public class ServerSongFragment extends Fragment {
    private AbsListView mListView;
    private TextView mStatusTextView;
    private TextView mCurrentSongTextView;

    private ArrayAdapter<Song> mAdapter;

    public static ServerSongFragment newInstance() {
        return new ServerSongFragment();
    }

    public ServerSongFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item2,
                android.R.id.text1,
                new ArrayList<Song>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_song_list, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mStatusTextView = (TextView) view.findViewById(R.id.statusText);
        mCurrentSongTextView = (TextView) view.findViewById(R.id.currentSong);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ServerSongRepository.loadFromServer(new ServerSongRepository.OnLoadListener() {
            @Override
            public void onLoad(List<Song> songs) {
                mAdapter.addAll(songs);
            }
        });

        ServerStatusRepository.loadFromServer(new ServerStatusRepository.OnLoadListener() {
            @Override
            public void onLoad(ServerStatus status) {
                mStatusTextView.setText(status.getText());
                mCurrentSongTextView.setText(status.getSong());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

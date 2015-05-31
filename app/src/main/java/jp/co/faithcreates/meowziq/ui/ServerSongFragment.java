package jp.co.faithcreates.meowziq.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.co.faithcreates.meowziq.R;
import jp.co.faithcreates.meowziq.model.ServerSongRepository;
import jp.co.faithcreates.meowziq.model.Song;

public class ServerSongFragment extends Fragment {
    private AbsListView mListView;

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
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

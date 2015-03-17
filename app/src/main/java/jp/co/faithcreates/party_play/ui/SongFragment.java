package jp.co.faithcreates.party_play.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.co.faithcreates.party_play.R;
import jp.co.faithcreates.party_play.model.Song;
import jp.co.faithcreates.party_play.model.SongRepository;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SongFragment extends ListFragment {
    private static final String ARTIST_NAME = "artistName";
    private String mArtistName;
    // TODO:
    private List<Song> mSongList;
    private OnFragmentInteractionListener mListener;

    public static SongFragment newInstance(String artistName) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mArtistName = getArguments().getString(ARTIST_NAME);
        }

        mSongList = new ArrayList<>();
        for (Song song : SongRepository.mSongList) {
            if (song.getArtist().equals(mArtistName)) {
                mSongList.add(song);
            }
        }
        setListAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.list_item2, android.R.id.text1, mSongList));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(mSongList.get(position));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Song song);
    }

}

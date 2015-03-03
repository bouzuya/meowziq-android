package jp.co.faithcreates.party_play;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class SongAdapter extends ArrayAdapter<Song> {
    private LayoutInflater inflater;
    private int resource;

    public SongAdapter(Context context, int resource) {
        super(context, resource);

        this.inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(resource, null);
        }

        Song item = this.getItem(position);
        ((TextView) view.findViewById(R.id.artist)).setText(item.getArtist());
        ((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
        return view;
    }
}

package com.goblin.qrhunter.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.goblin.qrhunter.Player;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;

import java.util.ArrayList;

public class playerSearchArrayAdapter extends ArrayAdapter<Player> {

    public playerSearchArrayAdapter(@NonNull Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1) 'convertView' object is a way to recycle old views inside the ListView -> Just increases performance of ListView.
        // If 'convertView' holds nothing, then 'content.xml' is inflated and assigned to 'view'.
        // Otherwise, we reuse 'convertView' as the 'view'.
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_player_search_list_content, parent, false);
        } else {
            view = convertView;
        }

        // Get the position of the qrcode item in list.
        Player player = getItem(position);

        // Get textViews for all attributes of "visit" object.
        TextView test = view.findViewById(R.id.player_test);

        // Sets the username. DON'T FORGET TO SET WHATEVER ELSE IS NEEDED LATER ON.
        test.setText(player.getUsername());

        return view;
    }
}

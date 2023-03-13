package com.goblin.qrhunter.ui.summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;

import java.util.ArrayList;

public class QRcodesArrayAdapter extends ArrayAdapter<QRCode> {

    private ArrayList<QRCode> codes;

    public QRcodesArrayAdapter(@NonNull Context context, ArrayList<QRCode> QRcodes) {
        super(context, 0, QRcodes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1) 'convertView' object is a way to recycle old views inside the ListView -> Just increases performance of ListView.
        // If 'convertView' holds nothing, then 'content.xml' is inflated and assigned to 'view'.
        // Otherwise, we reuse 'convertView' as the 'view'.
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_qr_list_item, parent, false);
        } else {
            view = convertView;
        }
        // Get the position of the qrcode item in list.
        QRCode qrcode = getItem(position);
        // Get textViews for all attributes of "visit" object.
        TextView qrcodeName = view.findViewById(R.id.qr_list_item_title);
        // Sets the name for each textView to the actual name of the attribute in the list.
        qrcodeName.setText(qrcode.getHash());
        return view;
    }
}

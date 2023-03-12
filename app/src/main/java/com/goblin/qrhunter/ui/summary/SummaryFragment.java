/**
 * The summary view is implemented using the Fragment class in Android, which provides a modular way to
 * display a section of the user interface. The view is created by inflating a layout file using the
 * LayoutInflater and binding the views to data using the FragmentSummaryBinding class.
 */
package com.goblin.qrhunter.ui.summary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentSummaryBinding;
import com.goblin.qrhunter.domain.GetPlayerQRCodes;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * The SummaryFragment class is responsible for displaying a summary of information in the application.
 * It uses the SummaryViewModel class to manage the data and logic behind the summary view.
 */
public class SummaryFragment extends Fragment {
    private ArrayList<QRCode> dataList;
    private ListView qrcodeList;
    private QRcodesArrayAdapter qrAdapter;

    private SummaryViewModel mViewModel;

    private FragmentSummaryBinding binding;

    /**

     Called to have the fragment instantiate its user interface view.

     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.

     @param container If non-null, this is the parent view that the fragment's UI should be attached to.

     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.

     @return The fragment's view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textDashboard;

        String username = mViewModel.getUsername().getValue();

        // TODO error is here
        MediatorLiveData<ArrayList<QRCode>> pc = new GetPlayerQRCodes(username).get();
        // Set up listview, datalist (holds the QR codes), and adapter.

//        QRCode testCode = new QRCode("I am a test");

        if (pc.getValue() != null) {
            Log.d("POST", pc.getValue().get(0).getHash());
            dataList = pc.getValue();
        }
        else {
            dataList = new ArrayList<>();
        }
        dataList = new ArrayList<>();
        qrcodeList = binding.getRoot().findViewById(R.id.qrcode_list);
        qrAdapter = new QRcodesArrayAdapter(getContext(), dataList);


        // Test: add a QR code with random string.
//        QRCode testCode = new QRCode("I am a test");
//        dataList.add(testCode);
        qrcodeList.setAdapter(qrAdapter);

        return root;
    }

    /**

     Called when the view previously created by onCreateView has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
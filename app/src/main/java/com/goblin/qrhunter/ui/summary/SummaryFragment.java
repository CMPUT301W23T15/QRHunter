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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.QRCode;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentSummaryBinding;
import com.goblin.qrhunter.domain.GetPlayerQRCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    FirebaseFirestore db;


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

        qrcodeList = binding.getRoot().findViewById(R.id.qrcode_list);
        TextView txtView = binding.textDashboard;

        String username = "User414613761";
        // getting username
        mViewModel.getUsername().observe(getViewLifecycleOwner(), txtView::setText);

        // creating lists and adapter
        dataList = new ArrayList<>();

        // setting adapter
        qrAdapter = new QRcodesArrayAdapter(getContext(), dataList);
        qrcodeList.setAdapter(qrAdapter);

        // updating files
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .whereEqualTo("playerId", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                dataList.add(doc.toObject(QRCode.class));
                                Log.d("GOOD", doc.getId() + " => " + doc.getData());
                            }
                        }
                        else {
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                        }
                        qrAdapter.notifyDataSetChanged();
                        Log.d("SIZE!", String.valueOf(dataList.size()));
                    }
                });
        return binding.getRoot();
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
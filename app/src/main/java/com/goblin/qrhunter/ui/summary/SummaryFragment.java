/**
 * The summary view is implemented using the Fragment class in Android, which provides a modular way to
 * display a section of the user interface. The view is created by inflating a layout file using the
 * LayoutInflater and binding the views to data using the FragmentSummaryBinding class.
 */
package com.goblin.qrhunter.ui.summary;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.goblin.qrhunter.Post;
import com.goblin.qrhunter.R;
import com.goblin.qrhunter.databinding.FragmentSummaryBinding;
import com.goblin.qrhunter.ui.listutil.QRRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * The SummaryFragment class is responsible for displaying a summary of information in the application.
 * It uses the SummaryViewModel class to manage the data and logic behind the summary view.
 */
public class SummaryFragment extends Fragment {
    private SummaryViewModel mViewModel;
    private FragmentSummaryBinding binding;
    private QRRecyclerAdapter qrAdapter;
    FirebaseFirestore db;
    String TAG = "SummaryFragment";


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

        // set username.
        mViewModel.getUsername().observe(getViewLifecycleOwner(), binding.textDashboard::setText);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.qrListView.setLayoutManager(llm);
        // create a qr list
        qrAdapter = new QRRecyclerAdapter();
        binding.qrListView.setAdapter(qrAdapter);

        // listen for changes and update list
        mViewModel.getUserPosts().observe(getViewLifecycleOwner(), qrAdapter::setData);
        mViewModel.getUserPosts().observe(getViewLifecycleOwner(), posts -> {
            for (Post p: posts
                 ) {
                Log.d(TAG, "onCreateView: updating: " + p.getId() );
            }

        });
        registerForContextMenu(binding.qrListView);


        return binding.getRoot();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.long_press_menu, menu);
    }

    /**
     * Brings up the context menu that can edit / delete the QR code. ONLY for summary fragment, as you don't want to
     * delete / edit other user's QR codes.
     * @param item The recyclerView item that was selected (long-pressed).
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Needs to get info about the listView. Store it into menuInfo.
        switch (item.getItemId()){
            // When the optionEdit is selected...
            case R.id.edit_QR_code:
                Toast.makeText(getContext(), "Edit option selected", Toast.LENGTH_SHORT).show();
                // Brings user to fragment to edit the tagged geo-location.
                return true;

            case R.id.delete_QR_code:
                Toast.makeText(getContext(), "Delete option selected", Toast.LENGTH_SHORT).show();
                // 1) Remove item from list // Don't think this is required either.
                // 2) Update firestore.
                // 3) Update adapter // Actually don't need this because of line 67 already listening for changes.

                return true;

            default:
                return super.onContextItemSelected(item);
        }
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
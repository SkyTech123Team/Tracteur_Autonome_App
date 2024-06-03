package ali.abdou.arauth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass that provides options to navigate to
 * either dimension input or image upload fragments.
 *@version 1.0
 *@author AIT ALI MHAMED SAADIA
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * This method is called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        // Initialize buttons from the layout
        Button btnDims = rootView.findViewById(R.id.dimensions);
        Button btnImg = rootView.findViewById(R.id.img);

        // Set up click listener for the dimensions button
        btnDims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DimensFragment to enter dimensions
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DimensFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Set up click listener for the image upload button
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadImageFragment to upload an image
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UploadImageFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}

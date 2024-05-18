package ali.abdou.arauth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ali.abdou.arauth.DimFragment;
import ali.abdou.arauth.R;
import ali.abdou.arauth.UploadImageFragment;

/**
 * A simple {@link Fragment} subclass.
 * Provides options to navigate to dimension input or image upload fragments.
 */
public class InfoBFragment extends Fragment {

    public InfoBFragment() {
        // Required empty public constructor
    }

/**
 * This method is called to have the fragment instantiate its user interface view.
 *
 * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
 * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
 * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
 * @return Return the View for the fragment's UI, or null.
 ***/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        Button btnDims = rootView.findViewById(R.id.dim);
        Button btnImg = rootView.findViewById(R.id.img);

        btnDims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers DimFragment pour entrer les dimensions
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DimFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers UploadImageFragment pour envoyer une image
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UploadImageFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
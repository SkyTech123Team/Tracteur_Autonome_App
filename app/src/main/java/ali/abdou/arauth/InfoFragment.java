package ali.abdou.arauth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass that provides options to navigate to
 * either dimension input or image upload fragments.
 *@version 1.0
 *@author SAFRANI Fatima ezzahra
 */
public class InfoFragment extends Fragment {
    //Button de dimensions
    private Button btnDims;
    //Button d'envoie d'image
    private Button btnImg;
    //Button pour commancer soit par image ou dimensions
    private Button btnPlay;
    //description pour lancer l'envoi
    private TextView startDesc;
    private OkHttpClient client = new OkHttpClient();
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
        btnDims = rootView.findViewById(R.id.dimensions);
        btnImg = rootView.findViewById(R.id.img);
        btnPlay = rootView.findViewById(R.id.play);
        startDesc=rootView.findViewById(R.id.sd);

        btnDims.setVisibility(View.GONE);
        btnImg.setVisibility(View.GONE);



        // Set up click listener for the dimensions button
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDims.setVisibility(View.VISIBLE);
                btnImg.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
                startDesc.setVisibility(View.GONE);
            }
        });



        // Si user choisit envoie des dimensions a l aide des inputs de valeurs on va le redireger vers un autre fragment
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



        // Si user choisit envoie des dimensions a l aide de l'image on va le redireger vers un autre fragment
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

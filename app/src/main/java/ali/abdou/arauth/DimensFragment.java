package ali.abdou.arauth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DimensFragment extends Fragment {
    private static final String RASPBERRY_PI_URL_PARAMS = "http://192.168.137.165:5000/sendInfo";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DimensFragment() {
        // Constructeur public vide requis
    }

    public static DimensFragment newInstance(String param1, String param2) {
        DimensFragment fragment = new DimensFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void sendHttpRequest(String urlString, int l, int h) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    // Définir les paramètres dans le corps de la requête
                    String postData = "l=" + l + "&h=" + h;
                    urlConnection.setDoOutput(true);
                    urlConnection.getOutputStream().write(postData.getBytes());
                    int responseCode = urlConnection.getResponseCode();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer les erreurs de connexion ici
                }
            }
        }).start();
    }

    private void executeFunctionOnRaspberryPiPostCoordonnees(int l, int h) {
        sendHttpRequest(RASPBERRY_PI_URL_PARAMS, l, h);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_controle, container, false);




        return rootView;
    }
}

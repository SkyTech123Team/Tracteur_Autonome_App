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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fragment pour envoyer les dimensions (longueur et hauteur) à un Raspberry Pi via une requête HTTP POST.
 */
public class DimFragment extends Fragment {
    private static final String RASPBERRY_PI_URL_PARAMS = "http://192.168.137.165:5000/sendInfo";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DimFragment() {
        // Constructeur public vide requis
    }

    /**
     * Utilise cette méthode pour créer une nouvelle instance de ce fragment en utilisant
     * les paramètres fournis.
     *
     * @param param1 Paramètre 1.
     * @param param2 Paramètre 2.
     * @return Une nouvelle instance de fragment DimFragment.
     */
    public static DimFragment newInstance(String param1, String param2) {
        DimFragment fragment = new DimFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Envoie une requête HTTP POST à l'URL spécifiée avec les dimensions fournies.
     *
     * @param urlString L'URL vers laquelle envoyer la requête HTTP.
     * @param l         La longueur à envoyer.
     * @param h         La hauteur à envoyer.
     */
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

    /**
     * Envoie les dimensions (longueur et hauteur) au Raspberry Pi via une requête HTTP POST.
     *
     * @param l La longueur à envoyer.
     * @param h La hauteur à envoyer.
     */
    private void executeFunctionOnRaspberryPiPostCoordonnees(int l, int h) {
        sendHttpRequest(RASPBERRY_PI_URL_PARAMS, l, h);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Gonfler le layout pour ce fragment
        View rootView = inflater.inflate(R.layout.fragment_controle, container, false);

        Button ok = rootView.findViewById(R.id.btnValid);
        TextView lTextView = rootView.findViewById(R.id.L);
        TextView hTextView = rootView.findViewById(R.id.H);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenir les valeurs des TextViews et les convertir en entiers
                int l = Integer.parseInt(lTextView.getText().toString());
                int h = Integer.parseInt(hTextView.getText().toString());
                executeFunctionOnRaspberryPiPostCoordonnees(l, h);
            }
        });

        return rootView;
    }
}

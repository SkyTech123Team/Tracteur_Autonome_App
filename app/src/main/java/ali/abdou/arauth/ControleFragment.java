package ali.abdou.arauth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fragment pour contrôler le mouvement d'un Raspberry Pi via une interface web.
 *@version 1.0
 *@author AIT ALI MHAMED SAADIA
 */
public class ControleFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WebView webPage;
    private FirebaseFirestore db;

    // Définir les constantes pour les URL de Firebase
    private static final String RASPBERRY_PI_URL_BASE = "http://"; // Préfixe de l'URL
    private String ipAddress = ""; // Stocker l'adresse IP récupérée de Firebase
    private static final String PORT = ":5000"; // Port utilisé
    private static final String MOVE_RIGHT = "/move_right";
    private static final String MOVE_LEFT = "/move_left";
    private static final String MOVE_FORWARD = "/move_forward";
    private static final String MOVE_BACKWARD = "/move_backward";
    private static final String STOP = "/stop";

    public ControleFragment() {
        // Required empty public constructor
    }

    /**
     * Utilise cette méthode pour créer une nouvelle instance de ce fragment en utilisant
     * les paramètres fournis.
     *
     * @param param1 Paramètre 1.
     * @param param2 Paramètre 2.
     * @return Une nouvelle instance de fragment ControleFragment.
     */
    public static ControleFragment newInstance(String param1, String param2) {
        ControleFragment fragment = new ControleFragment();
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
            // Utiliser les arguments si nécessaire
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gonfler le layout pour ce fragment
        View rootView = inflater.inflate(R.layout.fragment_controle, container, false);

        // Initialise Firestore
        db = FirebaseFirestore.getInstance();

        // Trouver les boutons par leur ID
        Button right = rootView.findViewById(R.id.btnRight);
        Button left = rootView.findViewById(R.id.btnLeft);
        Button forward = rootView.findViewById(R.id.btnForward);
        Button backward = rootView.findViewById(R.id.btnReverse);
        Button stop = rootView.findViewById(R.id.btnStop);

        // Trouver la WebView par son ID
        webPage = rootView.findViewById(R.id.myWebView2);

        // Charger la WebView avec l'adresse IP récupérée de Firebase
        db.collection("adresseIP").document("1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ipAddress = document.getString("adresse");
                        loadWebView(ipAddress);
                        setupButtonListeners();
                    }
                }
            }
        });

        return rootView;
    }

    /**
     * Configure les listeners des boutons pour envoyer les commandes au Raspberry Pi.
     */
    private void setupButtonListeners() {
        Button right = getView().findViewById(R.id.btnRight);
        Button left = getView().findViewById(R.id.btnLeft);
        Button forward = getView().findViewById(R.id.btnForward);
        Button backward = getView().findViewById(R.id.btnReverse);
        Button stop = getView().findViewById(R.id.btnStop);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFunctionOnRaspberryPi(RASPBERRY_PI_URL_BASE + ipAddress + PORT + MOVE_RIGHT);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFunctionOnRaspberryPi(RASPBERRY_PI_URL_BASE + ipAddress + PORT + MOVE_LEFT);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFunctionOnRaspberryPi(RASPBERRY_PI_URL_BASE + ipAddress + PORT + MOVE_FORWARD);
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFunctionOnRaspberryPi(RASPBERRY_PI_URL_BASE + ipAddress + PORT + MOVE_BACKWARD);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFunctionOnRaspberryPi(RASPBERRY_PI_URL_BASE + ipAddress + PORT + STOP);
            }
        });
    }

    /**
     * Charge la WebView avec l'adresse IP spécifiée.
     *
     * @param ipAddress L'adresse IP à charger dans la WebView.
     */
    private void loadWebView(String ipAddress) {
        String url = "http://" + ipAddress + ":5000/video_feed";
        webPage.loadUrl(url);
        webPage.getSettings().setJavaScriptEnabled(true);

        webPage.getSettings().setLoadWithOverviewMode(true);
        webPage.getSettings().setUseWideViewPort(true);
        webPage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webPage.setScrollbarFadingEnabled(true);
    }

    /**
     * Envoie une requête HTTP GET à l'URL spécifiée.
     *
     * @param urlString L'URL vers laquelle envoyer la requête HTTP.
     */
    private void executeFunctionOnRaspberryPi(String urlString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    int responseCode = urlConnection.getResponseCode();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer les erreurs de connexion ici
                }
            }
        }).start();
    }
}

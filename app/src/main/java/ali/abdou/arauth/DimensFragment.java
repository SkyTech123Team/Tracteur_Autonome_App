package ali.abdou.arauth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * DimensFragment est un fragment qui permet a l'utilisateur de saisir et d'envoyer
 * des dimensions (largeur et hauteur) a un serveur Raspberry Pi.
 *
 * @author SAFRANI Fatima ezzahra
 * @version 1.2
 */
public class DimensFragment extends Fragment {

    /** Cle JSON pour la largeur. */
    private static final String JSON_KEY_WIDTH = "width";

    /** Cle JSON pour la hauteur. */
    private static final String JSON_KEY_HEIGHT = "height";

    /** Champ de texte pour entrer la largeur. */
    EditText editTextWidth;

    /** Champ de texte pour entrer la hauteur. */
    EditText editTextHeight;

    /** L'instance de FirebaseFirestore. */
    private FirebaseFirestore db;

    /** L'adresse IP recuperee de Firebase. */
    private String ipAddress = "";

    /** L'URL du serveur Raspberry Pi pour envoyer les parametres. */
    private static final String RASPBERRY_PI_URL_PARAMS = "http://"; // Prefixe de l'URL
    private static final String PORT = ":5000/sendInfo";

    /**
     * Constructeur vide requis pour l'initialisation du fragment.
     */
    public DimensFragment() {
    }

    /**
     * Cree et retourne la vue associee au fragment.
     *
     * @param inflater Utilise pour gonfler la vue du fragment.
     * @param container Vue parent dans laquelle la vue du fragment est inseree.
     * @param savedInstanceState Si non-null, ce fragment est en train d'etre reconstruit a partir d'un etat sauvegarde precedemment.
     * @return La vue du fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dimens, container, false);
    }

    /**
     * Appele apres que la vue du fragment ait ete creee.
     *
     * @param view La vue renvoyee par {@link #onCreateView}.
     * @param savedInstanceState Si non-null, ce fragment est en train d'etre reconstruit a partir d'un etat sauvegarde precedemment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextWidth = view.findViewById(R.id.editTextWidth);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        Button buttonOK = view.findViewById(R.id.buttonOK);

        // Initialise Firestore
        db = FirebaseFirestore.getInstance();

        // Charger l'adresse IP a partir de Firebase
        db.collection("adresseIP").document("1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ipAddress = document.getString("adresse");
                    }
                }
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            /**
             * Methode appelee lorsqu'on clique sur le bouton "OK".
             *
             * @param v La vue qui a ete cliquee.
             */
            @Override
            public void onClick(View v) {
                String width = editTextWidth.getText().toString();
                String height = editTextHeight.getText().toString();

                if (!width.isEmpty() && !height.isEmpty()) {
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put(JSON_KEY_WIDTH, width);
                        jsonBody.put(JSON_KEY_HEIGHT, height);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Erreur lors de la creation de la requete", Toast.LENGTH_SHORT).show();
                        return; // Arreter le traitement si une exception se produit
                    }

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, jsonBody.toString());
                    Request request = new Request.Builder()
                            .url(RASPBERRY_PI_URL_PARAMS + ipAddress + PORT)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        /**
                         * Methode appelee lorsque la requete echoue.
                         *
                         * @param call L'appel qui a echoue.
                         * @param e L'exception qui a cause l'echec.
                         */
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("HTTP Request", "Erreur: " + e.getMessage());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "echec de l'envoi des donnees", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        /**
                         * Methode appelee lorsque la requete reussit.
                         *
                         * @param call L'appel qui a reussi.
                         * @param response La reponse du serveur.
                         * @throws IOException Si une erreur d'entree/sortie survient.
                         */
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                Log.d("HTTP Request", "Reponse: " + responseBody);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Donnees envoyees avec succes", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.e("HTTP Request", "Erreur: " + response.code() + " " + response.message());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Erreur lors de l'envoi des donnees", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

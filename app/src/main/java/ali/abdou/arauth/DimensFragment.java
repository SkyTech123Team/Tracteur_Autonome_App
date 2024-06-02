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

 * DimensFragment est un fragment qui permet à l'utilisateur de saisir et d'envoyer
 * des dimensions (largeur et hauteur) à un serveur Raspberry Pi.
 *
 * @author SAFRANI Fatima ezzahra
 * @version 1.2
 */
public class DimensFragment extends Fragment {

    /** L'URL du serveur Raspberry Pi pour envoyer les parametres. */
    private static final String RASPBERRY_PI_URL_PARAMS = "http://192.168.137.29:5000/sendInfo";

    /** Cle JSON pour la largeur. */
    private static final String JSON_KEY_WIDTH = "width";

    /** Cle JSON pour la hauteur. */
    private static final String JSON_KEY_HEIGHT = "height";

    /** Champ de texte pour entrer la largeur. */
    EditText editTextWidth;

    /** Champ de texte pour entrer la hauteur. */
    EditText editTextHeight;

    /**
     Constructeur vide requis pour l'initialisation du fragment.
     */
    public DimensFragment() {
    }
    /**

     Cree et retourne la vue associee au fragment.
     @param inflater Utilise pour gonfler la vue du fragment.
     @param container Vue parent dans laquelle la vue du fragment est inseree.
     @param savedInstanceState Si non-null, ce fragment est en train d'être reconstruit à partir d'un etat sauvegarde precedemment.
     @return La vue du fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dimens, container, false);
    }
    /**

     Appele apres que la vue du fragment ait ete creee.

     @param view La vue renvoyee par {@link #onCreateView}.

     @param savedInstanceState Si non-null, ce fragment est en train d'être reconstruit à partir d'un etat sauvegarde precedemment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextWidth = view.findViewById(R.id.editTextWidth);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        Button buttonOK = view.findViewById(R.id.buttonOK);

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
                        Toast.makeText(getActivity(), "Erreur lors de la creation de la requête", Toast.LENGTH_SHORT).show();
                        return; // Arrêter le traitement si une exception se produit
                    }

                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, jsonBody.toString());
                    Request request = new Request.Builder()
                            .url(RASPBERRY_PI_URL_PARAMS)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        /**
                         * Methode appelee lorsque la requête echoue.
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
                         * Methode appelee lorsque la requête reussit.
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

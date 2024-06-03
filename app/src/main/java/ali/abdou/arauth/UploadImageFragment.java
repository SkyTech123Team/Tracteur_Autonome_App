package ali.abdou.arauth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * UploadImageFragment est un fragment permettant de sélectionner une image,
 * de l'afficher et de l'envoyer à un serveur Raspberry Pi en utilisant une requête HTTP POST.
 *
 * @version 1.2
 * @author SAFRANI Fatima ezzahra
 */
public class UploadImageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSIONS = 123;
    private static final String RASPBERRY_PI_URL_PREFIX = "http://";
    private static final String PORT = ":5000/uploadImage";

    private ImageView imageView;
    private Button uploadButton;
    private Button okButton;

    private Bitmap selectedImageBitmap;

    private FirebaseFirestore db;
    private String ipAddress = "";

    /**
     * Constructeur public vide requis par Fragment.
     */
    public UploadImageFragment() {
        // Constructeur public vide requis par Fragment
    }

    /**
     * Crée et retourne la vue associée au fragment.
     *
     * @param inflater Utilisé pour gonfler la vue du fragment.
     * @param container Vue parent dans laquelle la vue du fragment est insérée.
     * @param savedInstanceState Si non-null, ce fragment est en train d'être reconstruit à partir d'un état sauvegardé précédemment.
     * @return La vue du fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_image, container, false);

        imageView = rootView.findViewById(R.id.imageView);
        uploadButton = rootView.findViewById(R.id.btnSelectImage);
        okButton = rootView.findViewById(R.id.btnUploadImage);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageBitmap != null) {
                    String base64Image = bitmapToBase64(selectedImageBitmap);
                    sendImageToRaspberryPi(base64Image);
                } else {
                    Toast.makeText(getContext(), "Veuillez d'abord sélectionner une image.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            }, REQUEST_PERMISSIONS);
        }

        // Initialise Firestore
        db = FirebaseFirestore.getInstance();

        // Charger l'adresse IP à partir de Firebase
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

        return rootView;
    }

    /**
     * Gère le résultat de la demande de permissions.
     *
     * @param requestCode Le code de demande de permissions.
     * @param permissions Les permissions demandées.
     * @param grantResults Les résultats de la demande de permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                Toast.makeText(getContext(), "Permissions are required to use this app", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Ouvre le sélecteur d'image pour permettre à l'utilisateur de choisir une image.
     */
    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    /**
     * Gère le résultat de l'activité de sélection d'image.
     *
     * @param requestCode Le code de demande.
     * @param resultCode Le code de résultat.
     * @param data Les données retournées par l'activité.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                imageView.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (resultCode == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Sélection d'image annulée.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Convertit un Bitmap en chaîne de caractères encodée en base64.
     *
     * @param bitmap Le Bitmap à convertir.
     * @return La chaîne de caractères encodée en base64.
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Envoie l'image encodée en base64 au serveur Raspberry Pi.
     *
     * @param base64Image L'image encodée en base64.
     */
    private void sendImageToRaspberryPi(String base64Image) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{ \"image\": \"" + base64Image + "\" }";

        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(RASPBERRY_PI_URL_PREFIX + ipAddress + PORT)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Image envoyée avec succès.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Erreur lors de l'envoi de l'image.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Erreur lors de l'envoi de l'image.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

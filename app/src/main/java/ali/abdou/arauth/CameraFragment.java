package ali.abdou.arauth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Fragment pour afficher le flux video de la camera via un WebView.
 *@version 1.0
 *@author AIT ALI MHAMED SAADIA
 */
public class CameraFragment extends Fragment {

    private ProgressBar superProgressBar;
    private ImageView superImageView;
    private WebView superWebView;

    private FirebaseFirestore db;

    /**
     * Crée et renvoie la vue hiérarchique associée au fragment.
     *
     * @param inflater           L'objet LayoutInflater qui peut être utilisé pour gonfler
     *                           n'importe quelle vue dans le fragment.
     * @param container          Si non-null, c'est le parent auquel la vue GUI de ce fragment
     *                           est attachée.
     * @param savedInstanceState Si non-null, ce fragment est reconstruit à partir d'un état
     *                           précédemment enregistré.
     * @return La vue pour l'interface utilisateur du fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        superProgressBar = rootView.findViewById(R.id.myProgressBar);
        superImageView = rootView.findViewById(R.id.myImageView);
        superWebView = rootView.findViewById(R.id.myWebView);

        superProgressBar.setMax(100);

        db = FirebaseFirestore.getInstance();

        // Récupère l'adresse IP depuis la base de données Firebase
        db.collection("adresseIP").document("1").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String adresseIP = document.getString("adresse");
                                if (adresseIP != null) {
                                    loadWebView(adresseIP);
                                }
                            }
                        }
                    }
                });

        return rootView;
    }

    /**
     * Charge le WebView avec l'URL de l'adresse IP spécifiée.
     *
     * @param adresseIP L'adresse IP à charger dans le WebView.
     */
    private void loadWebView(String adresseIP) {
        String url = "http://" + adresseIP + ":5000/video_feed";
        superWebView.loadUrl(url);
        superWebView.getSettings().setJavaScriptEnabled(true);

        superWebView.getSettings().setLoadWithOverviewMode(true);
        superWebView.getSettings().setUseWideViewPort(true);
        superWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        superWebView.setScrollbarFadingEnabled(true);

        superWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                superProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                superProgressBar.setVisibility(View.GONE);
            }
        });

        superWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getActivity().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImageView.setImageBitmap(icon);
            }
        });
    }
}

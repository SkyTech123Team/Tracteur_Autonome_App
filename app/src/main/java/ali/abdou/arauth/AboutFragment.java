package ali.abdou.arauth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Un simple {@link Fragment} sous-classe.
 * Utilisez la methode {@link AboutFragment#newInstance} pour
 * creer une instance de ce fragment.
 * @version 1.0
 * @author AIT ALI MHAMED SAADIA
 */
public class AboutFragment extends Fragment {

    // TODO: Renommez les arguments des paramètres, choisissez des noms qui correspondent
    // aux paramètres d'initialisation du fragment, par exemple ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Renommez et changez les types de paramètres
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Constructeur public vide requis
    }

    /**
     * Utilisez cette méthode de fabrique pour créer une nouvelle instance de
     * ce fragment en utilisant les paramètres fournis.
     *
     * @param param1 Paramètre 1.
     * @param param2 Paramètre 2.
     * @return Une nouvelle instance du fragment AboutFragment.
     */
    // TODO: Renommez et changez les types et le nombre de paramètres
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gonfler le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}

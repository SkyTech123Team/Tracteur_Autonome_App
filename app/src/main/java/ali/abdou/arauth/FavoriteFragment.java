package ali.abdou.arauth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Un simple {@link Fragment} sous-classe.
 * Utilisez la methode {@link FavoriteFragment#newInstance} pour
 * creer une instance de ce fragment.
 *@version 1.0
 *@author AIT ALI MHAMED SAADIA
 */
public class FavoriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FavoriteFragment() {
        // Constructeur public vide requis
    }

    /**
     * Utilisez cette méthode de fabrique pour créer une nouvelle instance de
     * ce fragment en utilisant les paramètres fournis.
     *
     * @param param1 Paramètre 1.
     * @param param2 Paramètre 2.
     * @return Une nouvelle instance du fragment FavoriteFragment.
     */
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Gonfler le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }
}

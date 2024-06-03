package ali.abdou.arauth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Un simple {@link Fragment} sous-classe representant la page d'accueil.
 *@version 1.0
 *@author SAFRANI Fatima ezzahra
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Constructeur public vide requis
    }

    /**
     * Méthode appelée pour initialiser la vue du fragment.
     *
     * @param inflater Le LayoutInflater utilisé pour gonfler les vues dans le fragment.
     * @param container Le conteneur parent auquel la vue du fragment est attachée.
     * @param savedInstanceState Si non-null, ce fragment est recréé à partir d'un état précédent.
     * @return La vue gonflée pour ce fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Gonfler le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

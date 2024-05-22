package fm.app.Activity.ui.equipos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fm.app.R;
import fm.app.databinding.FragmentModificarBinding;

public class ModificarFragment extends Fragment {
    private FragmentModificarBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modificar, container, false);
    }
}
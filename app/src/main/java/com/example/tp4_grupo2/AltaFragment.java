package com.example.tp4_grupo2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.tp4_grupo2.conexion.DataCategorias;

public class AltaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public AltaFragment() {
        // Required empty public constructor
    }

    public static AltaFragment newInstance(String param1, String param2) {
        AltaFragment fragment = new AltaFragment();
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
        View view = inflater.inflate(R.layout.fragment_alta, container, false);
        Spinner spinnerCategoria = view.findViewById(R.id.spinnerCategoria);

        DataCategorias dataCategorias = new DataCategorias(spinnerCategoria, getContext());
        dataCategorias.fetchData();

        return view;
    }
}
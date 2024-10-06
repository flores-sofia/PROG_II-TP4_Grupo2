package com.example.tp4_grupo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp4_grupo2.Entidades.Articulo;
import com.example.tp4_grupo2.Entidades.Categoria;
import com.example.tp4_grupo2.conexion.DataArticulo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModificacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificacionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText txtIdArticulo,txtNombreArticulo,txtStockArticulo,txtCategoriaArticulo;
    private Button btnBuscar;
    private Spinner spinnerCategoria;
    private Button btnModificar;


    public ModificacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModificacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModificacionFragment newInstance(String param1, String param2) {
        ModificacionFragment fragment = new ModificacionFragment();
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
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_modificacion, container, false);
        txtIdArticulo=view.findViewById(R.id.editTextId);
        txtNombreArticulo=view.findViewById(R.id.editTextNombreProducto);
        txtStockArticulo=view.findViewById(R.id.editTextStock);
        btnBuscar= view.findViewById(R.id.buttonBuscar);
        spinnerCategoria=view.findViewById(R.id.spinnerCategoria);

        btnModificar = view.findViewById(R.id.buttonModificar);
        spinnerCategoria.setVisibility(View.GONE);

        DataArticulo dataArticulo=new DataArticulo(txtNombreArticulo,txtStockArticulo,spinnerCategoria,getContext());


        btnBuscar.setOnClickListener(v -> {
            String idArticulo=txtIdArticulo.getText().toString();
            if(idArticulo.trim().isEmpty()){
                Toast.makeText(getContext(), "Debe ingresar un ID", Toast.LENGTH_SHORT).show();
            }else{
                int id=Integer.parseInt(idArticulo);
                dataArticulo.obtenerArticulo(getContext(),id);

                spinnerCategoria.setVisibility(View.VISIBLE);
//                txtIdArticulo.setText("");

            }
        });

        btnModificar.setOnClickListener(v -> {

            String idArticulo = txtIdArticulo.getText().toString();
            String nombre = txtNombreArticulo.getText().toString();
            String stock = txtStockArticulo.getText().toString();

            // Obtener el objeto Categoria seleccionado
            Categoria categoriaSeleccionada = (Categoria) spinnerCategoria.getSelectedItem();
            int idCategoria = (categoriaSeleccionada != null) ? categoriaSeleccionada.getId() : -1;

            if (validarDatos(nombre, stock, idCategoria)) {
                dataArticulo.modificarArticulo(getContext(), Integer.parseInt(idArticulo),
                        nombre, Integer.parseInt(stock), idCategoria);

                txtStockArticulo.setText("");
                txtNombreArticulo.setText("");
                txtIdArticulo.setText("");
                spinnerCategoria.setVisibility(View.GONE);
            }

        });

        return view;
    }

    private boolean validarDatos(String nombre, String stock, int idCategoria) {

        // Validar Nombre
        if (nombre.isEmpty() || contieneNumeros(nombre)) {
            Toast.makeText(getContext(), "El nombre del producto no puede estar vacío y no debe contener números", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar Stock
        if (stock.isEmpty() || !esNumero(stock) || Integer.parseInt(stock) <= 0) {
            Toast.makeText(getContext(), "El stock debe ser un número entero positivo", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Si todas las validaciones pasan
        return true;
    }

    private boolean esNumero(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean contieneNumeros(String str) {
        return str.matches(".*\\d.*"); // Verifica si hay algún dígito en el string
    }
}
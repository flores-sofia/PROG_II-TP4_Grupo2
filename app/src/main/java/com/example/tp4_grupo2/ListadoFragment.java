package com.example.tp4_grupo2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tp4_grupo2.Entidades.Articulo;
import com.example.tp4_grupo2.Entidades.Categoria;
import com.example.tp4_grupo2.conexion.DataArticulo;
import com.example.tp4_grupo2.conexion.DataCategorias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListadoFragment extends Fragment {

    private Map<Integer, Categoria> categorias;
    private LinearLayout contenedorArticulos;
    private Button botonActualizar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);
        categorias = cargarCategorias(); // Método que obtiene las categorías de alguna fuente
        contenedorArticulos = view.findViewById(R.id.contenedorArticulos);
        botonActualizar = view.findViewById(R.id.botonActualizar); // Inicializar el botón


        // Cargar la lista de artículos al iniciar
        obtenerListaArticulosDesdeDB();

        // Configurar el clic del botón de actualizar
        botonActualizar.setOnClickListener(v -> obtenerListaArticulosDesdeDB());

        return view;
    }

    private Map<Integer, Categoria> cargarCategorias() {
        Map<Integer, Categoria> mapCategorias = new HashMap<>();
        DataCategorias dataCategorias = new DataCategorias();
        dataCategorias.obtenerCategoriasParaListado(new DataCategorias.Callback() {

            @Override
            public void onSuccess(ArrayList<Categoria> listaCategorias) {
                for (Categoria cat : listaCategorias) {
                    mapCategorias.put(cat.getId(), cat);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Error al obtener los artículos: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        return mapCategorias;
    }



    // Método para obtener el nombre de la categoría
    private String obtenerNombreCategoria(int idCategoria) {
        Categoria categoria = categorias.get(idCategoria);
        return categoria != null ? categoria.getDescripcion() : "Desconocida";
    }

    // Método para obtener los artículos desde la base de datos
    private void obtenerListaArticulosDesdeDB() {
        contenedorArticulos.removeAllViews(); // Limpiar el contenedor antes de agregar los artículos
        DataArticulo dataArticulo = new DataArticulo();
        dataArticulo.obtenerArticulosParaListado(new DataArticulo.Callback() {

            @Override
            public void onSuccess(ArrayList<Articulo> listaArticulos) {
                for (Articulo articulo : listaArticulos) {
                    String nombreCategoria = obtenerNombreCategoria(articulo.getIdCategoria());

                    TextView textView = new TextView(getContext());
                    textView.setText("ID: " + articulo.getId() + "\nArtículo: " + articulo.getNombre() + "\nCategoría: " + nombreCategoria + "\nStock: " + articulo.getStock());

                    textView.setTextSize(18);
                    textView.setPadding(20, 20, 20, 20); // Ajustar padding
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
                    textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(10, 10, 10, 10);
                    textView.setLayoutParams(params);

                    contenedorArticulos.addView(textView);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Error al obtener los artículos: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

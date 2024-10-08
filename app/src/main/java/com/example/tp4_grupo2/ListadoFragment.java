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
import androidx.fragment.app.Fragment;

import com.example.tp4_grupo2.Entidades.Articulo;
import com.example.tp4_grupo2.Entidades.Categoria;
import com.example.tp4_grupo2.conexion.DataArticulo;

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
        contenedorArticulos = view.findViewById(R.id.contenedorArticulos);
        botonActualizar = view.findViewById(R.id.botonActualizar); // Inicializar el botón
        categorias = cargarCategorias(); // Método que obtiene las categorías de alguna fuente

        // Cargar la lista de artículos al iniciar
        obtenerListaArticulosDesdeDB();

        // Configurar el clic del botón de actualizar
        botonActualizar.setOnClickListener(v -> obtenerListaArticulosDesdeDB());

        return view;
    }

    // Método para cargar las categorías en un HashMap (puede ser de la base de datos o hardcodeado)
    private Map<Integer, Categoria> cargarCategorias() {
        Map<Integer, Categoria> mapCategorias = new HashMap<>();
        mapCategorias.put(1, new Categoria(1, "Electrónica"));
        mapCategorias.put(2, new Categoria(2, "Hogar"));
        mapCategorias.put(3, new Categoria(3, "Deportes"));
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
                    textView.setText("Artículo: " + articulo.getNombre() + "\nCategoría: " + nombreCategoria + "\nStock: " + articulo.getStock());
                    textView.setTextSize(18);
                    textView.setPadding(0, 20, 0, 20);
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);

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

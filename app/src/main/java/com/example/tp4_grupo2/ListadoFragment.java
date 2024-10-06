package com.example.tp4_grupo2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tp4_grupo2.Entidades.Articulo;
import com.example.tp4_grupo2.Entidades.Categoria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListadoFragment extends Fragment {

    private Map<Integer, Categoria> categorias;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);

        LinearLayout contenedorArticulos = view.findViewById(R.id.contenedorArticulos);

        cargarCategorias();

        List<Articulo> listaArticulos = obtenerListaArticulos();

        for (Articulo articulo : listaArticulos) {
            String nombreCategoria = obtenerNombreCategoria(articulo.getIdCategoria());

            TextView textView = new TextView(getContext());
            textView.setText("Artículo: " + articulo.getNombre() + "\nCategoría: " + nombreCategoria + "\nStock: " + articulo.getStock());
            textView.setTextSize(18);
            textView.setPadding(0, 20, 0, 20);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);

            contenedorArticulos.addView(textView);
        }

        return view;
    }

    private List<Articulo> obtenerListaArticulos() {
        List<Articulo> articulos = new ArrayList<>();
        articulos.add(new Articulo(1, "Laptop Dell XPS 13", 10, 1));
        articulos.add(new Articulo(2, "Monitor Samsung 24\"", 5, 2));
        articulos.add(new Articulo(3, "Teclado Mecánico Corsair", 8, 1));
        articulos.add(new Articulo(4, "Silla Gamer DXRacer", 3, 3));
        return articulos;
    }

    private void cargarCategorias() {
        categorias = new HashMap<>();
        categorias.put(1, new Categoria(1, "Electrónica"));
        categorias.put(2, new Categoria(2, "Accesorios"));
        categorias.put(3, new Categoria(3, "Muebles"));
    }

    private String obtenerNombreCategoria(int idCategoria) {
        Categoria categoria = categorias.get(idCategoria);
        return categoria != null ? categoria.getDescripcion() : "Categoría desconocida";
    }
}

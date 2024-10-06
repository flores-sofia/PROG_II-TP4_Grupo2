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

import com.example.tp4_grupo2.Entidades.Categoria;
import com.example.tp4_grupo2.conexion.DataArticulo;
import com.example.tp4_grupo2.conexion.DataCategorias;

public class AltaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alta, container, false);

        // Referencias a los campos de entrada
        EditText editTextID = view.findViewById(R.id.editTextID);
        EditText editTextNombreProducto = view.findViewById(R.id.editTextNombreProducto);
        EditText editTextStock = view.findViewById(R.id.editTextStock);
        Spinner spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        Button btnAgregar = view.findViewById(R.id.btnAgregar);

        // Cargar categorías en el Spinner
        DataCategorias dataCategorias = new DataCategorias(spinnerCategoria, getContext());
        dataCategorias.fetchData();

        // Acción al hacer clic en el botón AGREGAR
        btnAgregar.setOnClickListener(v -> {
            String idArticulo = editTextID.getText().toString();
            String nombre = editTextNombreProducto.getText().toString();
            String stock = editTextStock.getText().toString();

            // Obtener el objeto Categoria seleccionado
            Categoria categoriaSeleccionada = (Categoria) spinnerCategoria.getSelectedItem();
            int idCategoria = (categoriaSeleccionada != null) ? categoriaSeleccionada.getId() : -1;

            // Validar los datos antes de intentar guardar
            if (!validarDatos(idArticulo, nombre, stock, idCategoria)) {
                return; // Si la validación falla, no continuar
            }

            // Validar que el ID no exista en la base de datos
            if (DataArticulo.existeArticulo(getContext(), Integer.parseInt(idArticulo))) {
                Toast.makeText(getContext(), "El ID ya existe en la base de datos", Toast.LENGTH_SHORT).show();
                return; // Si el ID ya existe, no continuar
            }

            // Guardar el artículo en la base de datos
            DataArticulo.guardarArticulo(getContext(), Integer.parseInt(idArticulo), nombre, Integer.parseInt(stock), idCategoria);

            // Limpiar los campos después de agregar
            editTextID.setText("");
            editTextNombreProducto.setText("");
            editTextStock.setText("");
            spinnerCategoria.setSelection(0); // Restablecer el Spinner a la primera posición
        });

        return view;
    }

    private boolean validarDatos(String idArticulo, String nombre, String stock, int idCategoria) {
        // Validar ID
        if (idArticulo.isEmpty() || !esNumero(idArticulo)) {
            Toast.makeText(getContext(), "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            return false;
        }

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

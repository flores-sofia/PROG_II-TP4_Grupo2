package com.example.tp4_grupo2.conexion;

import android.content.Context;
import android.widget.Spinner;

import com.example.tp4_grupo2.Entidades.Categoria;
import com.example.tp4_grupo2.adapter.CategoriaAdapter;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataCategorias{
    private Spinner spinnerCategoria;
    private Context context;

    private static String result;
    private static ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>();

    public DataCategorias(Spinner sp, Context ct)
    {
        spinnerCategoria = sp;
        context = ct;
    }

    public void fetchData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ArrayList<Categoria> listaCategorias = new ArrayList<>();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM categoria");

                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setDescripcion(rs.getString("descripcion"));
                    listaCategorias.add(categoria);
                }
                rs.close();
                st.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                CategoriaAdapter adapter = new CategoriaAdapter(context, listaCategorias);
                spinnerCategoria.setAdapter(adapter);
            });
        });
    }

    public void cargarDataYSeleccionar(int posicionSeleccion) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ArrayList<Categoria> listaCategorias = new ArrayList<>();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM categoria");

                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setDescripcion(rs.getString("descripcion"));
                    listaCategorias.add(categoria);
                }
                rs.close();
                st.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                CategoriaAdapter adapter = new CategoriaAdapter(context, listaCategorias);
                spinnerCategoria.setAdapter(adapter);
                spinnerCategoria.setSelection(posicionSeleccion);
            });
        });
    }
}

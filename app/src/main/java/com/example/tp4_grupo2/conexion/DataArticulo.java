package com.example.tp4_grupo2.conexion;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tp4_grupo2.Entidades.Articulo;
import com.example.tp4_grupo2.Entidades.Categoria;
import com.example.tp4_grupo2.ModificacionFragment;
import com.example.tp4_grupo2.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataArticulo {

    public static String nombreTabla="articulo";
    public static String columnaId="id";
    public static String columnaNombre="nombre";
    public static String columnaStock="stock";
    public static String columnaCategoria="idCategoria";

    //Para Modificar
    private static EditText txtNombreArticulo;
    private static EditText txtStockArticulo;
    private static Spinner spinnerCategoria;
    private static Articulo articuloModificar=new Articulo();
    private Context context;

    //Constructor para actualizar controles Modificar


    public DataArticulo(EditText etNombre,EditText etStock,Spinner sp, Context ct)
    {
        txtNombreArticulo=etNombre;
        txtStockArticulo=etStock;
        spinnerCategoria=sp;
        context = ct;
    }


    // Método para guardar un artículo

    public static void guardarArticulo(Context context, int id, String nombre, int stock, int idCategoria) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String sql = "INSERT INTO articulo (id, nombre, stock, idCategoria) VALUES (?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);
                statement.setString(2, nombre);
                statement.setInt(3, stock);
                statement.setInt(4, idCategoria);
                int rowsInserted = statement.executeUpdate();

                // Actualiza la UI en el hilo principal
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    if (rowsInserted > 0) {
                        Toast.makeText(context, "Artículo guardado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al guardar el artículo", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (SQLException e) {
                // Manejo específico para el error de duplicado
                if (e.getErrorCode() == 1062) { // Código de error para duplicados
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "El ID ya existe en la base de datos", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    e.printStackTrace();
                    // Actualiza la UI en el hilo principal para otros errores
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Error al guardar el artículo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    public static void modificarArticulo(Context context, int id, String nombre, int stock, int idCategoria) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String sql = "UPDATE articulo SET nombre = ?, stock = ?, idCategoria = ? WHERE id = ?";
            try (Connection connection = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, nombre);
                statement.setInt(2, stock);
                statement.setInt(3, idCategoria);
                statement.setInt(4, id);

                int rowsUpdated = statement.executeUpdate();

                // Actualiza la UI en el hilo principal
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    if (rowsUpdated > 0) {
                        Toast.makeText(context, "Artículo modificado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al modificar el artículo", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (SQLException e) {

                Toast.makeText(context, "Algo malio sal jeje", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean existeArticulo(Context context, int id) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) FROM articulo WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                existe = resultSet.getInt(1) > 0;
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }



    public void obtenerArticulo(Context context,int id) {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM " + nombreTabla + " WHERE " + columnaId + "=" + id);

                    int c=0;
                    while(rs.next()){
                        articuloModificar.setId(rs.getInt(columnaId));
                        articuloModificar.setNombre(rs.getString(columnaNombre));
                        articuloModificar.setStock(rs.getInt(columnaStock));
                        articuloModificar.setIdCategoria(rs.getInt(columnaCategoria));
                        System.out.println(articuloModificar.toString());
                        c++;
                    }

                    rs.close();
                    st.close();
                    con.close();

                    // Actualiza la UI en el hilo principal para errores
                    if(c==0){
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            Toast.makeText(context, "No existe Articulo con el ID ingresado", Toast.LENGTH_SHORT).show();
                            txtNombreArticulo.setText("");
                            txtStockArticulo.setText("");
                            txtNombreArticulo.setEnabled(false);
                            txtStockArticulo.setEnabled(false);
                            spinnerCategoria.setEnabled(false);
                        });
                    }else {
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {

                            String stock= String.valueOf(articuloModificar.getStock());
                            txtNombreArticulo.setText(articuloModificar.getNombre());
                            txtStockArticulo.setText(stock);

                            // Cargar categorías en el Spinner
                            DataCategorias dataCategorias = new DataCategorias(spinnerCategoria,context);
                            dataCategorias.cargarDataYSeleccionar(articuloModificar.getIdCategoria()-1);


                            txtNombreArticulo.setEnabled(true);
                            txtStockArticulo.setEnabled(true);
                            spinnerCategoria.setEnabled(true);

                        });


                    }




                } catch (Exception e) {
                    e.printStackTrace();
                    // Actualiza la UI en el hilo principal para errores
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Error al obtener el artículo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        txtNombreArticulo.setText("");
                        txtStockArticulo.setText("");
                        txtNombreArticulo.setEnabled(false);
                        txtStockArticulo.setEnabled(false);
                        spinnerCategoria.setEnabled(false);
                    });
                }
            });

        return;
    }


}

package com.example.tp4_grupo2.conexion;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataDB {
    // Datos de conexión
    public static String urlMySQL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10734361";
    public static String user = "sql10734361";
    public static String pass = "IAzwgfvyXN";

    // Método para guardar un artículo
    public static void guardarArticulo(Context context, int id, String nombre, int stock, int idCategoria) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String sql = "INSERT INTO articulo (id, nombre, stock, idCategoria) VALUES (?, ?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(urlMySQL, user, pass);
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

    public static boolean existeArticulo(Context context, int id) {
        boolean existe = false;
        try {
            Connection connection = DriverManager.getConnection(urlMySQL, user, pass);
            String sql = "SELECT COUNT(*) FROM articulo WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
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
}

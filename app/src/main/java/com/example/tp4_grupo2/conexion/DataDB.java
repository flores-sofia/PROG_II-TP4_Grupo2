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


}

package com.example.sqlconnection;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class MainActivity extends AppCompatActivity {
    private TextView mytextView;
    private Button  mybutton;
    private static String sqlServerIp = "192.168.1.26";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String sqlDatabaseName = "Ogrenci";
    private static String sqlUserName = "sa";
    private static String sqlUserPassword = "123";
    private static String connectionUrl = "jdbc:jtds:sqlserver://"+sqlServerIp+":"+port+"/"+sqlDatabaseName;
    private Connection connectionStatus = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mytextView = findViewById(R.id.textView);
        mybutton = (Button)findViewById(R.id.sqlButton);
        mybutton.setOnClickListener(this::sqlGetData);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connectionStatus = DriverManager.getConnection(connectionUrl, sqlUserName,sqlUserPassword);
            //DriverManager.setLoginTimeout(100000);
            mytextView.setText("CONNECTION SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            mytextView.setText("CLASS ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            mytextView.setText("CONNECTION FAILURE");
        }
    }
    public void sqlGetData(View view){
        String line= new String(new char[32]).replace("\0", "_");
        String sqlData="Registered Names in the Database\n"+line+"\n";
        if (connectionStatus!=null){
            Statement statement = null;
            try {
                statement = connectionStatus.createStatement();
                ResultSet resultSet = statement.executeQuery("Select * from Ogrenciler;");
                while (resultSet.next()){
                    sqlData+=resultSet.getString(2)+' '+resultSet.getString(3)+'\n';
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            mytextView.setText("Connection is null");
        }
        mytextView.setText(sqlData);
    }
}
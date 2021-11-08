package com.example.pogodejszyn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String Extra = "com.example.application.example.Extra";
    Button button;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        //zabezpiecznie przed brakiem nazwy
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ConnectivityManager connectivityManager = (ConnectivityManager)
                    getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
               //pobieranie informacji o sieci
               NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
               //sprawdzanie statusu
               if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){

                   Toast toast = Toast.makeText(getApplicationContext(), "Połączenie stracone", Toast.LENGTH_LONG);
                   toast.show();
               }
               else {
               String miasto = editText.getText().toString();
               savedata(miasto);
               if(TextUtils.isEmpty(miasto)){
                   editText.setError("zle miasto");
                   return;
               }
               Intent druga = new Intent(getApplicationContext(),MainActivity2.class);
               druga.putExtra(Extra,miasto);
               startActivity(druga);

           }
           }
       });
        loaddata();
    }
    private void savedata (String miasto)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("miasto", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("miasto", miasto);
        editor.apply();

    }
    private void loaddata (){
        SharedPreferences sharedPreferences = getSharedPreferences("miasto", MODE_PRIVATE);
        String dane = sharedPreferences.getString("miasto", "");
        editText.setText(dane);
    }


}

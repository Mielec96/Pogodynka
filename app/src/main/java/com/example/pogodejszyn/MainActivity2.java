package com.example.pogodejszyn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity2 extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String Extra = "com.example.application.example.Extra";
    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7;
    ImageView obrazek;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler mHandler ;

    public static String miasto;

    //zapis/przesył/odczyt
    class pogoda extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream dane = connection.getInputStream();
                InputStreamReader daner = new InputStreamReader(dane);
                int data = daner.read();
                String content = "";
                char ch;
                while(data != -1){
                ch = (char) data;
                content = content + ch;
                data = daner.read();
                }
                return content;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        this.handlerRefreshData();

        this.mHandler = new Handler();
        mHandler.post(m_Runnable);
    }
    public void handlerRefreshData(){

       swipeRefreshLayout = findViewById(R.id.refresh);
       swipeRefreshLayout.setOnRefreshListener(this);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        txt5 = findViewById(R.id.txt5);
        txt6 = findViewById(R.id.txt6);
        txt7 = findViewById(R.id.txt7);
        obrazek = findViewById(R.id.obrazek);
         //pobieranie wpisy z mainactivity 1
         Intent druga = getIntent();
         miasto = druga.getStringExtra(MainActivity.Extra);


        String content;
        pogoda pogoda2 = new pogoda();
        try {
            content = pogoda2.execute("http://api.openweathermap.org/data/2.5/weather?q=" + miasto + "&units=metric&APPID=749561a315b14523a8f5f1ef95e45864").get();
            //Zabezpiecznie przed wpisaniem nieprawdiłowej wartosci
            if(content == null)
            {
                Intent pierwsza = new Intent(getApplicationContext(),MainActivity.class);
                Toast toast = Toast.makeText(getApplicationContext(), "Wpisz poprawne miasto", Toast.LENGTH_LONG);
                toast.show();
                startActivity(pierwsza);
                return;
                }
            Log.i("sprawdzam", content);
            //biblioteka json spisanie wartosci z serwera
            JSONObject json = new JSONObject(content);
            String pdata = json.getString("weather");
            String nazwa = json.getString("name");
            String temp = json.getString("main");
            String czas = json.getString("dt");
            String strefaczasowa = json.getString("timezone");

            JSONObject tempe = new JSONObject(temp);
            JSONObject cis = new JSONObject(temp);
            JSONObject wilg = new JSONObject(temp);
            JSONObject tmin = new JSONObject(temp);
            JSONObject tmax = new JSONObject(temp);

            String temperatura = tempe.getString("temp");
            String cisnienie = cis.getString("pressure");
            String wilgotnosc = wilg.getString("humidity");
            String temperaturamin = tmin.getString("temp_min");
            String temperaturamax = tmax.getString("temp_max");

            //zmiena temp bez przecinka
            double tbp = Double.parseDouble(temperatura);
            double tbpr = Math.round(tbp);
            NumberFormat formatt = new DecimalFormat("0.#");

            //czas
            Long czasodczyt = Long.parseLong(czas);
            Long strefaczasowaodczyt = Long.parseLong(strefaczasowa);
            Long czasprawidlowy = ((czasodczyt + strefaczasowaodczyt)*1000);
            Date format  = new java.util.Date(czasprawidlowy);
            String godzina = new SimpleDateFormat("HH:mm").format(format);

            JSONArray array = new JSONArray(pdata);
            String ikony = "";
            for(int i = 0; i < array.length();i++){
                JSONObject ikona = array.getJSONObject(i);
                ikony = ikona.getString("icon");
            }

            if (ikony.equals("01d")) {
                obrazek.setImageResource(R.drawable.d01);
            }
            else if (ikony.equals("01n")) {
                obrazek.setImageResource(R.drawable.n01);
            }
            else if (ikony.equals("02d")) {
                obrazek.setImageResource(R.drawable.d02);
            }
            else if (ikony.equals("02n")) {
                obrazek.setImageResource(R.drawable.n02);
            }
            else if (ikony.equals("03d")) {
                obrazek.setImageResource(R.drawable.d03);
            }
            else if (ikony.equals("03n")) {
                obrazek.setImageResource(R.drawable.n03);
            }
            else if (ikony.equals("04d")) {
                obrazek.setImageResource(R.drawable.d05);
            }
            else if (ikony.equals("04n")) {
                obrazek.setImageResource(R.drawable.n05);
            }
            else if (ikony.equals("09d")) {
                obrazek.setImageResource(R.drawable.d05);
            }
            else if (ikony.equals("09n")) {
                obrazek.setImageResource(R.drawable.n05);
            }
            else if (ikony.equals("10d")) {
                obrazek.setImageResource(R.drawable.d06);
            }
            else if (ikony.equals("10n")) {
                obrazek.setImageResource(R.drawable.n06);
            }
            else if (ikony.equals("11d")) {
                obrazek.setImageResource(R.drawable.d07);
            }
            else if (ikony.equals("11n")) {
                obrazek.setImageResource(R.drawable.n07);
            }
            else if (ikony.equals("13d")) {
                obrazek.setImageResource(R.drawable.d08);
            }
            else if (ikony.equals("13n")) {
                obrazek.setImageResource(R.drawable.n08);
            }
            else if (ikony.equals("50d")) {
                obrazek.setImageResource(R.drawable.d09);
            }
            else if (ikony.equals("50n")) {
                obrazek.setImageResource(R.drawable.n09);
            }
            else {
                obrazek.setImageResource(R.drawable.d01);
            }
            //wyniki
            txt6.setText("" + nazwa);
            txt2.setText("Temperatura : " + formatt.format(tbpr) + "°C");
            txt3.setText("Cisnienie : " + cisnienie + "hPa");
            txt4.setText("Wilgotnosc : " + wilgotnosc + "%");
            txt5.setText("Temp_min : " + temperaturamin + " °C");
            txt1.setText("Tem_max : " + temperaturamax + " °C");
            txt7.setText("Godzina : " + godzina);

            miasto = txt6.getText().toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRefresh(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Intent to = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(to);
            Toast toast = Toast.makeText(getApplicationContext(), "Utracono połączenie", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        else{
            handlerRefreshData();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
                Intent to = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(to);
                Toast toast = Toast.makeText(getApplicationContext(), "Utracono połączenie", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
            else{
                handlerRefreshData();
                mHandler.postDelayed(this, 20000);
            }
        }
    };
}

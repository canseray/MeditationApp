package com.example.meditationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    public boolean internetVarMi (final Context context){
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(internetVarMi(this)){
            Thread splashThread;
            splashThread = new Thread(){
                @Override public void run(){
                    try{
                        synchronized (this){
                            wait(2000);
                        }
                    } catch (InterruptedException ex){

                    }
                    finally {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }
            };
            splashThread.start();
        }
        else{
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Baglanti hatasi");
            alert.setMessage("Lutfen internet baglantinizi kontrol edin");
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "Tamam",    //mesaj sonrası button olusturduk
                    new DialogInterface.OnClickListener() {                    //butonu tıklayınca ne olucak
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pid = android.os.Process.myUid();
                            android.os.Process.killProcess(pid);
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }
    }
}

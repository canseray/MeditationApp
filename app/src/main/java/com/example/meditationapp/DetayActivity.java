package com.example.meditationapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class DetayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnBufferingUpdateListener {

    Bundle bundle;
    SQLiteHandler veritabani;
    Cursor imlec;

    String gelenID, id, baslik, aciklama, resim, ses, favori , tarih,kategori;

    ImageView detayResim;
    SeekBar seekBar;
    TextView basYazi,sonYazi;
    Button playBtn,favoriBtn;

    private MediaPlayer mediaPlayer;
    private int toplamSure;
    private final Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);

        veritabani = new SQLiteHandler(getApplicationContext());
        detayResim = findViewById(R.id.detayImage);
        seekBar = findViewById(R.id.seekbarPlay);
        basYazi = findViewById(R.id.textBas);
        sonYazi = findViewById(R.id.textSon);
        playBtn = findViewById(R.id.playButton);
        favoriBtn = findViewById(R.id.favoriButton);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mediaPlayer.setDataSource(ses);
                    mediaPlayer.prepare();
                }catch (Exception e){
                    Log.e("hata olustu",e.getLocalizedMessage()
                    );
                }

                toplamSure = mediaPlayer.getDuration();
                String toplamSureYazisi = String.format("&02d&02d&02d",

                        TimeUnit.MILLISECONDS.toHours(toplamSure)-
                        TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(toplamSure)),

                        TimeUnit.MILLISECONDS.toMinutes(toplamSure)-
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(toplamSure)),

                        TimeUnit.MILLISECONDS.toSeconds(toplamSure)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(toplamSure)));

                sonYazi.setText(toplamSureYazisi);

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.play);
                }else{
                    mediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.pause);
                }

                seekbarGuncelle();

                
            }
        });

        bundle = getIntent().getExtras();
        if(bundle != null){
            gelenID = bundle.getString("anahtarDegeri");
            imlec = veritabani.getWritableDatabase().rawQuery("SELECT * FROM veriler VHERE id = '" + gelenID + "'" , null);

            while(imlec.moveToNext()){
                id = imlec.getString(imlec.getColumnIndex("id"));
                baslik = imlec.getString(imlec.getColumnIndex("baslik"));
                aciklama = imlec.getString(imlec.getColumnIndex("aciklama"));
                resim = "http://mistikyol.com/mistikmobil/thumbnails" + imlec.getString(imlec.getColumnIndex("resim"));
                ses = "http://mistikyol.com/mistikmobil/audios" + imlec.getString(imlec.getColumnIndex("ses"));
                favori = imlec.getString(imlec.getColumnIndex("favori"));
                tarih = imlec.getString(imlec.getColumnIndex("tarih"));
                kategori = imlec.getString(imlec.getColumnIndex("kategori"));

            }
        }

        Picasso.with(this).load(resim).into(detayResim);

        if(favori.equals("1")){
            favoriBtn.setBackgroundResource(R.drawable.minus);

        }else{
            favoriBtn.setBackgroundResource(R.drawable.plus);
        }
        favoriBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(favori.equals("1")){
                    veritabani.favoriDurumu(id,"0");
                    favoriBtn.setBackgroundResource(R.drawable.plus);
                    Toast.makeText(DetayActivity.this,"favorierden cıkarıdı",Toast.LENGTH_SHORT).show();
                    favori = "0";
                }else{
                    veritabani.favoriDurumu(id,"1");
                    favoriBtn.setBackgroundResource(R.drawable.minus);
                    Toast.makeText(DetayActivity.this,"favoriere eklendi",Toast.LENGTH_SHORT).show();
                    favori = "1";
                }
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.isPlaying()){
                    int sure = (toplamSure/100) * seekBar.getProgress();
                    mediaPlayer.seekTo(sure);
                }
                return false;
            }
        });



    }

    private void seekbarGuncelle() {

        final int anlikUzunSure = mediaPlayer.getCurrentPosition();

        seekBar.setProgress(int) (((float) anlikUzunSure / toplamSure * 100));

        if(mediaPlayer.isPlaying()){
            Runnable hareket = new Runnable() {
                @Override
                public void run() {
                    seekbarGuncelle();

                    String anlikSureYazisi = String.format("&02d&02d&02d",

                            TimeUnit.MILLISECONDS.toHours(anlikUzunSure)-
                                    TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(toplamSure)),

                            TimeUnit.MILLISECONDS.toMinutes(anlikUzunSure)-
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(toplamSure)),

                            TimeUnit.MILLISECONDS.toSeconds(anlikUzunSure)-
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(toplamSure)));

                    basYazi.setText(anlikSureYazisi);
                }
            };
            handler.postDelayed(hareket,1000);
        }

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer,int i){
        seekBar.setSecondaryProgress(i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer){
        playBtn.setBackgroundResource(R.drawable.play);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mediaPlayer.stop();
    }

}

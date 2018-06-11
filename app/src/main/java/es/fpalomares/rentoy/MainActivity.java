package es.fpalomares.rentoy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "rentoyPrefsFile";
    Button nos_1, nos_3, vos_1, vos_3, reset, delete, play_envio;
    Integer score_nos = 0, score_vos = 0;
    Activity thisActivity = this;
    List<String> stack_list = new ArrayList<String>();
    Gson gson = new Gson();
    Random random = new Random();
    MediaPlayer mp;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        restoreData();

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("destroyed", false);
        editor.apply();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String jsonText = prefs.getString("stack_list", null);
        if (jsonText != null && !jsonText.isEmpty()) {
            stack_list = new ArrayList<String>(Arrays.asList(gson.fromJson(jsonText, String[].class)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("score_nos", score_nos);
        editor.putInt("score_vos", score_vos);


        String jsonText = gson.toJson(stack_list);
        editor.putString("stack_list", jsonText);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("destroyed", true);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        nos_1 = (Button) findViewById(R.id.button);
        nos_3 = (Button) findViewById(R.id.button2);
        vos_1 = (Button) findViewById(R.id.button3);
        vos_3 = (Button) findViewById(R.id.button4);

        reset = (Button) findViewById(R.id.button5);
        delete = (Button) findViewById(R.id.button6);

        play_envio = (Button) findViewById(R.id.button7);

        mp = MediaPlayer.create(thisActivity, R.raw.rufinos);

        initAlertDialog();

        setButtonListeners();

    }

    public void initAlertDialog() {

        builder = new AlertDialog.Builder(thisActivity,R.style.CustomDialogTheme);
        builder.setTitle("Elige un sonido");

        // voices lists
        String[] voices = {"Los Rufinos huelen peste", "¡Envío!", "¡Revío!", "¡¡nueve!!", "¡¡¡El rentoy entero!!!", "Gallina"};
        builder.setItems(voices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // rufinos
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mp = MediaPlayer.create(thisActivity, R.raw.rufinos);
                        mp.start();
                        break;
                    case 1: // envío
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mp = MediaPlayer.create(thisActivity, R.raw.envio);
                        mp.start();
                        break;
                    case 2: // revío
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mp = MediaPlayer.create(thisActivity, R.raw.revio);
                        mp.start();
                        break;
                    case 3: // nueve
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mp = MediaPlayer.create(thisActivity, R.raw.nueve);
                        mp.start();
                        break;
                    case 4: // el rentoy entero
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mp = MediaPlayer.create(thisActivity, R.raw.entero);
                        mp.start();
                        break;
                    case 5: // gallina
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mp = MediaPlayer.create(thisActivity, R.raw.clucking);
                        mp.start();
                        break;
                }
            }
        });

    }

    public void setButtonListeners() {


        nos_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // add 1 point to nos
                if (score_vos < 24 && score_nos < 24 ) {
                    score_nos++;
                    ImageView chino = (ImageView) getImage("nos",score_nos);
                    chino.setVisibility(View.VISIBLE);

                    if (score_nos == 24) {
                        Toast.makeText(
                                MainActivity.this,
                                "¡GANA Nos!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                    stack_list.add("nos-"+Integer.toString(random.nextInt(1000)));

                } else if (score_vos >= 24) {
                    Toast.makeText(
                            MainActivity.this,
                            "¡Ha ganado Vos!",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "¡Ha ganado Nos!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        nos_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // add 3 points to nos
                nos_1.performClick();
                nos_1.performClick();
                nos_1.performClick();
            }
        });

        vos_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // add 1 point to vos
                if (score_nos < 24 && score_vos < 24) {
                    score_vos++;
                    ImageView chino = (ImageView) getImage("vos",score_vos);
                    chino.setVisibility(View.VISIBLE);

                    if (score_vos == 24) {
                        Toast.makeText(
                                MainActivity.this,
                                "¡GANA Vos!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    stack_list.add("vos-"+Integer.toString(random.nextInt(1000)));

                } else if (score_nos >= 24) {
                    Toast.makeText(
                            MainActivity.this,
                            "¡Ha ganado Nos!",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "¡Ha ganado Vos!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        vos_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                vos_1.performClick();
                vos_1.performClick();
                vos_1.performClick();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (score_nos > 0 || score_vos > 0) {

                    new AlertDialog.Builder(thisActivity,R.style.CustomDialogTheme)
                            .setTitle("Atención")
                            .setMessage("¿Seguro que quieres resetear la puntuación?")
                            .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // reset score of NOS
                                    Integer i = 1;
                                    score_nos = 0;
                                    while (i <= 24) {
                                        ImageView chino = (ImageView) getImage("nos", i);
                                        chino.setVisibility(View.INVISIBLE);
                                        i++;
                                    }
                                    // reset score of VOS
                                    i = 1;
                                    score_vos = 0;
                                    while (i <= 24) {
                                        ImageView chino = (ImageView) getImage("vos", i);
                                        chino.setVisibility(View.INVISIBLE);
                                        i++;
                                    }

                                    stack_list = new ArrayList<String>();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (!stack_list.isEmpty()) {

                    Integer last_pos = stack_list.size() - 1;
                    String delete_team_plus_random = stack_list.get(last_pos);
                    String[] parts = delete_team_plus_random.split("-");
                    String delete_team = parts[0];

                    stack_list.remove(delete_team_plus_random);

                    if ( delete_team.equals("nos")) {
                        ImageView chino = (ImageView) getImage("nos",score_nos);
                        chino.setVisibility(View.INVISIBLE);
                        score_nos--;
                    } else {
                        ImageView chino = (ImageView) getImage("vos",score_vos);
                        chino.setVisibility(View.INVISIBLE);
                        score_vos--;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No hay más movimientos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        play_envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void restoreData() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        score_nos = prefs.getInt("score_nos", 0);
        score_vos = prefs.getInt("score_vos", 0);

        Integer i;
        if ( score_nos > 0 ) {
            i = score_nos;
            while (i > 0) {
                ImageView chino = (ImageView) getImage("nos",i);
                chino.setVisibility(View.VISIBLE);
                i--;
            }
        }

        if ( score_vos > 0 ) {
            i = score_vos;
            while (i > 0) {
                ImageView chino = (ImageView) getImage("vos",i);
                chino.setVisibility(View.VISIBLE);
                i--;
            }
        }
    }
    
    public ImageView getImage(String type, Integer score) {

        ImageView chino;

        if (type.equals("nos")) {
            
            switch (score) {
                case 1:
                    chino = findViewById(R.id.nos1); 
                    break;

                case 2:
                    chino = findViewById(R.id.nos2);
                    break;

                case 3:
                    chino = findViewById(R.id.nos3);
                    break;
                case 4:
                    chino = findViewById(R.id.nos4);
                    break;

                case 5:
                    chino = findViewById(R.id.nos5);
                    break;

                case 6:
                    chino = findViewById(R.id.nos6);
                    break;

                case 7:
                    chino = findViewById(R.id.nos7);
                    break;

                case 8:
                    chino = findViewById(R.id.nos8);
                    break;

                case 9:
                    chino = findViewById(R.id.nos9);
                    break;

                case 10:
                    chino = findViewById(R.id.nos10);
                    break;

                case 11:
                    chino = findViewById(R.id.nos11);
                    break;

                case 12:
                    chino = findViewById(R.id.nos12);
                    break;

                case 13:
                    chino = findViewById(R.id.nos13);
                    break;

                case 14:
                    chino = findViewById(R.id.nos14);
                    break;

                case 15:
                    chino = findViewById(R.id.nos15);
                    break;
                    
                case 16:
                    chino = findViewById(R.id.nos16);
                    break;

                case 17:
                    chino = findViewById(R.id.nos17);
                    break;

                case 18:
                    chino = findViewById(R.id.nos18);
                    break;

                case 19:
                    chino = findViewById(R.id.nos19);
                    break;

                case 20:
                    chino = findViewById(R.id.nos20);
                    break;

                case 21:
                    chino = findViewById(R.id.nos21);
                    break;

                case 22:
                    chino = findViewById(R.id.nos22);
                    break;

                case 23:
                    chino = findViewById(R.id.nos23);
                    break;

                case 24:
                    chino = findViewById(R.id.nos24);
                    break;

                default:
                    chino = findViewById(R.id.nos1);
                    break;
            }
        } else {

            switch (score) {
                case 1:
                    chino = findViewById(R.id.vos1);
                    break;

                case 2:
                    chino = findViewById(R.id.vos2);
                    break;

                case 3:
                    chino = findViewById(R.id.vos3);
                    break;
                case 4:
                    chino = findViewById(R.id.vos4);
                    break;

                case 5:
                    chino = findViewById(R.id.vos5);
                    break;

                case 6:
                    chino = findViewById(R.id.vos6);
                    break;

                case 7:
                    chino = findViewById(R.id.vos7);
                    break;

                case 8:
                    chino = findViewById(R.id.vos8);
                    break;

                case 9:
                    chino = findViewById(R.id.vos9);
                    break;

                case 10:
                    chino = findViewById(R.id.vos10);
                    break;

                case 11:
                    chino = findViewById(R.id.vos11);
                    break;

                case 12:
                    chino = findViewById(R.id.vos12);
                    break;

                case 13:
                    chino = findViewById(R.id.vos13);
                    break;

                case 14:
                    chino = findViewById(R.id.vos14);
                    break;

                case 15:
                    chino = findViewById(R.id.vos15);
                    break;

                case 16:
                    chino = findViewById(R.id.vos16);
                    break;

                case 17:
                    chino = findViewById(R.id.vos17);
                    break;

                case 18:
                    chino = findViewById(R.id.vos18);
                    break;

                case 19:
                    chino = findViewById(R.id.vos19);
                    break;

                case 20:
                    chino = findViewById(R.id.vos20);
                    break;

                case 21:
                    chino = findViewById(R.id.vos21);
                    break;

                case 22:
                    chino = findViewById(R.id.vos22);
                    break;

                case 23:
                    chino = findViewById(R.id.vos23);
                    break;

                case 24:
                    chino = findViewById(R.id.vos24);
                    break;

                default:
                    chino = findViewById(R.id.vos1);
                    break;
            }
        }
        
        return chino;
    }
}

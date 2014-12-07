package edu.project.cmpe277.musicalheart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {


    private static final String CLIENT_ID = "0828e6ce1d794db2bafa65d74e0d36b7";

    private static final String REDIRECT_URI = "my-test-app://callback";
    public static final String EXTRAS_DATA = "0";
    String genre;
    SharedPreferences sharedpreferences;
    public static final String PREFERENCES = "MusicalHeartGenreTracks" ;
    private Player mPlayer;


    public static final String CLASSICAL = "classical";
    public static final String COUNTRY = "country";
    public static final String ROCK = "rock";
    public static final String POP_FITNESS = "pop fitness";
    public static final String ELECTRONIC_CARDIO = "electronic cardio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartrate);
        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        saveTracksForGenres();
        Intent intent = getIntent();
        Log.i("MainActivity","Inside MainActivity");
        String pulse = intent.getStringExtra(EXTRAS_DATA);
        int pulseInt = Integer.parseInt(pulse);
        if(pulseInt < 60){
            genre = CLASSICAL;
        }else if(pulseInt < 80){
            genre = COUNTRY;
        }else if(pulseInt < 95) {
            genre = ROCK;
        }else if(pulseInt < 120) {
            genre = POP_FITNESS;
        }else {
            genre = ELECTRONIC_CARDIO;
        }
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "streaming"}, null, this);

    }

    public void saveTracksForGenres(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ELECTRONIC_CARDIO, "5D0aA5aGrfMmtzpMc052n9");
        editor.putString(POP_FITNESS, "2qbFI9BLhDBO7Ez99COaLq");
        editor.putString(ROCK, "7mitXLIMCflkhZiD34uEQI");
        editor.putString(COUNTRY, "2xYlyywNgefLCRDG8hlxZq");
        editor.putString(CLASSICAL, "16Nvga6HrBxeJCCmrfmRi8");
        editor.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();

        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "Musical Heart", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    mPlayer.addConnectionStateCallback(MainActivity.this);
                    mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    String track = getTrack(genre);

                    //  mPlayer.play("spotify:track:5D0aA5aGrfMmtzpMc052n9");
                    if (track != null) {
                        Toast.makeText(getApplicationContext(), "Playing a song for you!", Toast.LENGTH_LONG).show();
                        mPlayer.play("spotify:track:" + track);
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, Could not find a song for you", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
    }

    public String getTrack(String genre){
        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String result = sharedpreferences.getString(genre, null);
        Log.d("getTrack", "retrieved track: "+result);
        return result;
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onNewCredentials(String s) {
        Log.d("MainActivity", "User credentials blob received");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();

    }
}

package com.example.rob.robofficial;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.rob.robofficial.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener{
    public SpeechRecognizer sr;
    public TextView text;
    //public Button button;
    public TextView errors;
    public boolean listening=false;
    public ArrayList<String> goodShit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text=(TextView) findViewById(R.id.textMain);

        sr=SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(this);//aha I AM the listener now!

        //startListening();


        errors=(TextView) findViewById(R.id.textErrors);

        //text.setText("nipples");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                text.setText("end me");
                Snackbar.make(view, "how does this work please help", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                startListening();
            }
        });
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listening){
                    //stopListening();
                    sr.stopListening();
                    listening=false;
                    System.out.println("stopped listening");
                    button.setText("Start Listening");
                }else{
                    startListening();
                }
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        text.setText("stop fucking with the options, idiot.");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void error(String text){
        errors.append(text+"\n");
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        System.out.println("ready captain");
        error("onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        System.out.println("started talkin");
        error("onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        System.out.println("new dB value: "+rmsdB);
        //error("RMS changed to "+rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        System.out.println("got a buffer...");
        error("onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        System.out.println("done talkin");
        error("onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        System.out.println("ERROR "+error);
        text.setText("ERROR: "+error);
        if(error==9){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},1);
            sr.stopListening();
            listening=false;
            System.out.println("got record audio permission");
        }
        if(error==2){
            requestPermissions(new String[]{Manifest.permission.INTERNET},1);
            sr.stopListening();
            listening=false;
            System.out.println("got internet access");
        }
        //stopListening();
        sr.stopListening();
        listening=false;
        System.out.println("stopped listening");
        //button.setText("Start Listening");
    }

    @Override
    public void onResults(Bundle results) {
        goodShit=(ArrayList<String>)results.get(sr.RESULTS_RECOGNITION);
        System.out.println("GOOD SHIT COMIN IN HOT BOYS WATCH TF OUT");
        System.out.println(goodShit.get(0));
        text.setText(goodShit.get(0));
        error("YOU JUST SAID: "+goodShit);
        //TODO: Jason just said "wait" in a different conversation
        //it accepted it without me hitting "stop listening"
        //when I hit stop listening, it said error 5 because it had already stopped...
        //onPartialResults() or onEndOfSpeech() ??? hmmm

        startListening();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        System.out.println("partial results...?");
        goodShit=(ArrayList<String>)partialResults.get(sr.RESULTS_RECOGNITION);
        error("partial results are in: "+goodShit);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        System.out.println("there's been an event");
    }

    public void startListening(){
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        sr.startListening(speechIntent);
        listening=true;
        System.out.println("started listening");
        //button.setText("Stop Listening");
        error("started listening");
    }
    public void stopListening(){
        sr.stopListening();
        listening=false;
        System.out.println("stopped listening");
        //button.setText("Start Listening");
        error("stopped listening");
    }
}
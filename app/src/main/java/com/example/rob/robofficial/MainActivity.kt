package com.example.rob.robofficial

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

import java.util.ArrayList

class MainActivity : AppCompatActivity(), RecognitionListener {
    var sr: SpeechRecognizer? = null
    var text: TextView? = null
    var errors: TextView? = null
    var fab:FloatingActionButton? = null
    //var listening = false
    var result: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        text = findViewById<View>(R.id.textMain) as TextView

        sr = SpeechRecognizer.createSpeechRecognizer(this)
        sr!!.setRecognitionListener(this)//this activity is the listener


        errors = findViewById<View>(R.id.textErrors) as TextView

        //text.setText("nipples");
        fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab!!.setOnClickListener { view ->
            text!!.text = "end me"
            Snackbar.make(view, "how does this work please help", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            startListening()//just set the activity for the little email button to do it for now...
        }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        text!!.text = "stop fucking with the options, idiot."
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun error(text: String) {
        errors!!.append(text + "\n")
    }

    override fun onReadyForSpeech(params: Bundle) {
        errors!!.setBackgroundColor(Color.GREEN)
    }

    override fun onBeginningOfSpeech() {
        errors!!.setBackgroundColor(Color.CYAN)
    }

    override fun onRmsChanged(rmsdB: Float) {
        //error("RMS changed to "+rmsdB);
    }

    override fun onBufferReceived(buffer: ByteArray) {
        //error("onBufferReceived")
    }

    override fun onEndOfSpeech() {
        errors!!.setBackgroundColor(Color.WHITE)
    }

    override fun onError(error: Int) {
        println("ERROR $error")
        text!!.text = "ERROR: $error"
        if (error == 9) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            //sr!!.stopListening()
            //listening = false
            println("requested record audio permission")
        }
        if (error == 2) {
            requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)
            //sr!!.stopListening()
            //listening = false
            println("requested internet access")
        }
        //stopListening();
        sr!!.stopListening()
        //listening = false
        println("stopped listening")
        //button.setText("Start Listening");
    }

    override fun onResults(results: Bundle) {
        var result: ArrayList<String> = results.get(/*sr.RESULTS_RECOGNITION*/SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
        println(result[0])
        text!!.text = result[0]//Main one, the one it's most confident about...
        error("YOU JUST SAID: $result")
        //it accepted it without me hitting "stop listening"
        //when I hit stop listening, it said error 5 because it had already stopped...
        //onPartialResults() or onEndOfSpeech() ??? hmmm

        //startListening();
    }

    override fun onPartialResults(partialResults: Bundle) {
        var result: ArrayList<String> = partialResults.get(/*sr.RESULTS_RECOGNITION*/SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String> //copy pasted from above
        error("partial results are in: $result")
    }

    override fun onEvent(eventType: Int, params: Bundle) {
        println("there's been an event")
    }

    fun startListening() {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        sr!!.startListening(speechIntent)
        //listening = true
        //button.setText("Stop Listening");
        error("started listening")
    }
}
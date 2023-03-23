package com.example.pionniers

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class main : AppCompatActivity(), View.OnClickListener{

    private val TAG:String = main::class.java.simpleName;
    var linearLayout:LinearLayout? = null;
    var namesArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG,"onCreate");
        linearLayout = LinearLayout(this);
        var params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout?.orientation = LinearLayout.VERTICAL

        //pour plus tard quand le joeur aura fini le mini-jeu
        //val jsonString = File("assets/pionniers.json").readText()

        val jsonString = this.assets.open("pionniers.json").bufferedReader().use {it.readText()}

        val jsonObject = JSONObject(jsonString)

        val pionniersArray = jsonObject.getJSONArray("pionniers")

        for(i in 0 until pionniersArray.length()){
            val pionnierObject = pionniersArray.getJSONObject(i)
            val name = pionnierObject.getString("name")

            val textView:TextView = TextView(this)
            textView.setText("?")
            textView.textSize = 25f
            textView.setLayoutParams(params)
            textView.id = i

            val button:Button = Button(this)
            button.setOnClickListener(this)
            button.setText("Unlock")
            button.setLayoutParams(params)
            button.id = i


            linearLayout?.addView(textView)
            linearLayout?.addView(button)
            namesArray.add(name)
        }

        addContentView(linearLayout,params)
    }

    override fun onClick(p0: View?){

        val intent:Intent = Intent(this, miniGame::class.java)
        //println(namesArray)
        intent.putExtra("NAMES",namesArray.toTypedArray())
        //println(Arrays.toString(intent.getStringArrayExtra("NAMES")))
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        //Log.i(TAG, "OnStart");
    }

    override fun onResume() {
        super.onResume()
        //Log.i(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        //Log.i(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        //Log.i(TAG,"onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        //Log.i(TAG,"onDestroy")
    }
}
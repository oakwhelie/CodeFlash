package com.codeflash

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlin.math.log

class MainActivity : AppCompatActivity()
{
    var hasflash = false
    var flashon = false
    val CAMERA_REQUEST = 2357687
    var cam = Camera.open()
    var p = cam.getParameters()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data

        ActivityCompat.requestPermissions(  this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST)
        hasflash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        Log.d("onCreate", "onCreate done")

    }

    fun morse(view:View)
    {
        val morsetime = textToMorse(findViewById<EditText>(R.id.text).text.toString())
        Toast.makeText(this, morsetime.toString(), Toast.LENGTH_LONG*2)

        val flash = findViewById<Button>(R.id.flash)
        val morse = findViewById<Button>(R.id.morse)
        val text = findViewById<EditText>(R.id.text)

        flash.isEnabled = false
        morse.isEnabled = false
        text.isEnabled = false

        //now make the buttons look pressed

        for(time in morsetime)
        {
            Log.d("loop", "iterate")
            if(time == 250 || time == 1000)
            {
                Log.d("loop", "alpha")
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                cam.setParameters(p)
                cam.startPreview()
                Thread.sleep(time.toLong())
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                cam.setParameters(p)
                cam.stopPreview()
            }
            if(time == 1001 || time == 2000)
            {
                Log.d("loop", "delay")
                Thread.sleep(time.toLong())
            }
        }
        flash.isEnabled = true
        morse.isEnabled = true
        text.isEnabled = true
        Toast.makeText(this, "morse code sent", Toast.LENGTH_LONG).show()
    }

    fun morseToTime(s:Char) = when(s){
        '.' -> 250
        '-' -> 1000
        'w' -> 2000
        's' -> 1001
        else -> throw IllegalArgumentException("Not all characters are valid")
    }

    fun textToMorse(s: String) : List<Int>
    {
        val morseArr = s.map{value -> convertCharToMorse(value)}
        val res = morseArr.joinToString("w","","")
        println(res)
        val time = res.map { value -> morseToTime(value)}
        return time
    }


    fun convertCharToMorse(c : Char) = when (c.toLowerCase()) {
        'a' -> ".-"
        'b' -> "-..."
        'c' -> "-.-."
        'd' -> "-.."
        'e' -> "."
        'f' -> "..-."
        'g' -> "--."
        'h' -> "...."
        'i' -> ".."
        'j' -> ".---"
        'k' -> "-.-"
        'l' -> ".-.."
        'm' -> "--"
        'n' -> "-."
        'o' -> "---"
        'p' -> ".--."
        'q' -> "--.-"
        'r' -> ".-."
        's' -> "..."
        't' -> "-"
        'u' -> "..-"
        'v' -> "...-"
        'w' -> ".--"
        'x' -> "-..-"
        'y' -> "-.--"
        'z' -> "--.."
        '1' -> ".----"
        '2' -> "..---"
        '3' -> "...--"
        '4' -> "....-"
        '5' -> "....."
        '6' -> "-...."
        '7' -> "--..."
        '8' -> "---.."
        '9' -> "----."
        '0' -> "-----"
        ' ' -> "s"
        else -> throw IllegalArgumentException("Not all characters are valid")
    }

    fun flash(view:View)
    {
        Log.d("flash", "flash clicked")
        if(hasflash)
        {
            Log.d("flash", "hasflash checked")
            if(!flashon)
            {
                Log.d("flash", "flash on")
                var text:String = findViewById<EditText>(R.id.text).text.toString()
                Toast.makeText(this, textToMorse(text).toString(), Toast.LENGTH_LONG).show()
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                cam.setParameters(p)
                cam.startPreview()
                flashon = true
            }
            else
            {
                Log.d("flash", "flash off")
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                cam.setParameters(p)
                cam.stopPreview()
                flashon = false
            }

        }
    }

}

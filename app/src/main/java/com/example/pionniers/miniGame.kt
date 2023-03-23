package com.example.pionniers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class CustomView(context: Context) : View(context){
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


    }
}

class Card(val function:String,context: Context) : AppCompatButton(context){
    var isMatched = false
    var isFlipped = false

    init {
        text = function
    }

    fun flip(){
        if (isMatched) {println("Matched")
            return}
        if(isFlipped){
            this.setText(function)
        } else{
            this.setText("")
        }
        isFlipped = !isFlipped
    }

}

class miniGame() : AppCompatActivity() {
    private lateinit var equationText: TextView
    private lateinit var answerText: TextView
    private lateinit var rootLayout: GridLayout
    private var equations: MutableList<String> = mutableListOf()
    private var answers: MutableList<String> = mutableListOf()
    private var matchedIndices: MutableSet<Int> = mutableSetOf()
    private var currentEquationIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootLayout = GridLayout(this)
        rootLayout.columnCount = 4
        rootLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setContentView(rootLayout)

        // Initialize
        equationText = TextView(this)
        answerText = TextView(this)
        initializeGameData()
        initializeViews()

        // Start game
        reset()
    }

    private fun initializeGameData() {
        equations.addAll(listOf("2+2", "6+5", "7-3", "10-7", "8+4", "9-5", "3+9", "4+6"))
        answers.addAll(listOf("4", "11", "4", "3", "12", "4", "12", "10"))

        equations.shuffle()
        answers.shuffle()
    }

    private fun initializeViews() {
        var currentIndex = 0
        val equationsCount = minOf(equations.size, answers.size)

        for (i in 0 until 8) {
            val equationBox = createBox(equations[i], currentIndex)
            val answerBox = createBox(answers[i], currentIndex + 1)

            rootLayout.addView(equationBox)
            rootLayout.addView(answerBox)

            currentIndex += 2
        }
    }

    private fun createBox(text: String, index: Int): TextView {
        val box = TextView(this)
        box.text = text
        box.textSize = 24F
        box.setTextColor(Color.BLACK)
        box.setBackgroundResource(androidx.appcompat.R.color.material_blue_grey_800)

        if (index < 16) {
            box.setOnClickListener {
                if (!matchedIndices.contains(index) && currentEquationIndex == null) {
                    showText(index)
                    currentEquationIndex = index
                } else if (!matchedIndices.contains(index) && currentEquationIndex != null) {
                    checkAnswer(index)
                }
            }
        }

        val params = GridLayout.LayoutParams()
        params.width = resources.displayMetrics.widthPixels / 5
        params.height = resources.displayMetrics.widthPixels / 5
        params.setMargins(10, 10, 10, 10)
        box.layoutParams = params

        println("Box index: $index")

        return box
    }

    private fun showText(index: Int) {
        println(index)
        val box = rootLayout.getChildAt(index) as TextView
        val text = if (currentEquationIndex == null) equations[index] else answers[index]
        box.text = text
        currentEquationIndex = if (currentEquationIndex == null) index else null
    }

    private fun checkAnswer(index: Int) {
        if (answers[index] == answers[currentEquationIndex!!]) {
            matchedIndices.add(index)
            matchedIndices.add(currentEquationIndex!!)
            showText(index)
            currentEquationIndex = null

            checkIfGameIsOver()
        } else {
            showText(currentEquationIndex!!)
            showText(index)

            Handler().postDelayed({
                val box1 = rootLayout.getChildAt(currentEquationIndex!!) as TextView
                val box2 = rootLayout.getChildAt(index) as TextView
                box1.text = ""
                box2.text = ""
                currentEquationIndex = null
            }, 1000)
        }
    }

    private fun checkIfGameIsOver() {
        if (matchedIndices.size == equations.size) {
            Toast.makeText(this, "Congratulations! You have won the game!", Toast.LENGTH_LONG).show()
        }
    }

    private fun reset() {
        equations.shuffle()
        answers.shuffle()
        matchedIndices.clear()
        currentEquationIndex = null

        rootLayout.postDelayed({
            for (i in 0 until 16) {
                val box = rootLayout.getChildAt(i) as TextView
                box.text = ""
                box.setBackgroundResource(androidx.appcompat.R.color.material_blue_grey_800)
            }
        }, 500)
    }
}
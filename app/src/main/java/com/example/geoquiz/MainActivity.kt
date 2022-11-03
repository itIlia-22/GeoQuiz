package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

const val INDEX_CURRENT = "INDEX_CURRENT"
const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView


    private val viewModel: QuizViewModel by lazy {
        val provider = ViewModelProvider(this)[QuizViewModel::class.java]
        provider
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate")
        setContentView(R.layout.activity_main)
        Log.d(TAG, "Got a QuizViewModel:$viewModel")
        trueButton = findViewById(R.id.trueBtn)
        falseButton = findViewById(R.id.falseBtn)
        nextButton = findViewById(R.id.nextBtn)
        backButton = findViewById(R.id.backBtn)
        cheatButton = findViewById(R.id.Cheat)
        questionTextView = findViewById(R.id.text)

        val currentIndex = savedInstanceState?.getInt(INDEX_CURRENT, 0) ?: 0
        viewModel.currentIndex = currentIndex

        trueButton.setOnClickListener {
            chekAnswer(true)

        }
        falseButton.setOnClickListener {
            chekAnswer(false)
        }
        nextButton.setOnClickListener {
            viewModel.moveToNext()
            updateQuestion()
        }
        backButton.setOnClickListener {
            viewModel.moveBackward()
            updateQuestion()
        }
        cheatButton.setOnClickListener {
            val currentIsTrue = viewModel.currentQuestionAnswer
            val intent = ChatActivity2.newIntent(this@MainActivity, currentIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val option = ActivityOptions.makeClipRevealAnimation(it, 0, 0, it.width, it.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, option.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }


        }

        updateQuestion()


    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode, data
        )
        if (resultCode != Activity.RESULT_OK) return
        if (resultCode == REQUEST_CODE_CHEAT) {
            viewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("TAG", "outState() called")
        outState.putInt(INDEX_CURRENT, viewModel.currentIndex)
    }


    private fun chekAnswer(userAnswer: Boolean) {
        val correctAnswer: Boolean = viewModel.currentQuestionAnswer
        val messageResId = when {
            viewModel.isCheater ->
                R.string.judgment_toast
            userAnswer == correctAnswer ->
                R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun updateQuestion() {
        try {
            val questionTextResId = viewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
        }


    }

}
package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val KEY_IS_CHEATER = "KEY_IS_CHEATER"

class ChatActivity2 : AppCompatActivity() {

    private var answerIsTrue: Boolean = false
    private lateinit var answerTextView: TextView
    private lateinit var sdkTextView: TextView
    private lateinit var showAnswerButton: Button
    private var currentAttempts: Int = 3


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)
        if (savedInstanceState != null) {
            answerIsTrue = savedInstanceState.getBoolean(KEY_IS_CHEATER, false)


        }

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        sdkTextView = findViewById(R.id.version_sdk)
        showAnswerButton = findViewById(R.id.show_answer_button)


        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue ->
                    R.string.TrueBtn

                else -> R.string.FalseBtn


            }
            if (currentAttempts == 1) {
                it.isEnabled = false


            }
            currentAttempts--
            sdkTextView.text = " Attempts $currentAttempts/3"
            answerTextView.setText(answerText)
            Toast.makeText(this, "Oh, it's not good to peek", Toast.LENGTH_LONG).show()
            setAnswerShownResult(true)


        }
        //val text = Build.VERSION.SDK_INT
        sdkTextView.text = "3/3"

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_CHEATER, answerIsTrue)

    }

    private fun setAnswerShownResult(isAnswerShow: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShow)

        }
        setResult(Activity.RESULT_OK, data)

    }


    companion object {
        fun newIntent(context: Context, answerTrue: Boolean): Intent {
            return Intent(context, ChatActivity2::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerTrue)
            }

        }
    }
}
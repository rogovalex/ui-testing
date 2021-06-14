package com.example.uitesting.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.uitesting.R
import com.example.uitesting.domain.PasswordStrength

class PasswordCheckResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_check_result)

        val resultIcon = findViewById<ImageView>(R.id.resultIcon)
        val resultView = findViewById<TextView>(R.id.resultView)

        val result = intent.getSerializableExtra(RESULT) as PasswordStrength

        val iconRes: Int
        val text: String
        val color: Int
        when (result) {
            PasswordStrength.WEAK -> {
                iconRes = R.drawable.ic_weak
                text = "Слабый"
                color = ContextCompat.getColor(this, R.color.red)
            }
            PasswordStrength.MEDIUM -> {
                iconRes = R.drawable.ic_medium
                text = "Средний"
                color = ContextCompat.getColor(this, R.color.orange)
            }
            PasswordStrength.STRONG -> {
                iconRes = R.drawable.ic_strong
                text = "Сильный"
                color = ContextCompat.getColor(this, R.color.green)
            }
        }
        resultIcon.setImageResource(iconRes)
        resultView.text = text
        resultView.setTextColor(color)
    }

    companion object {

        private const val RESULT = "result"

        fun newIntent(context: Context, passwordStrength: PasswordStrength): Intent {
            return Intent(context, PasswordCheckResultActivity::class.java).apply {
                putExtra(RESULT, passwordStrength)
            }
        }
    }
}

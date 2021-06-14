package com.example.uitesting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.uitesting.CustomApplication
import com.example.uitesting.R
import com.example.uitesting.inject.PasswordCheckActivityModule
import javax.inject.Inject

class PasswordCheckActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: PasswordCheckViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_check)

        (application as CustomApplication).component
            .passwordCheckActivityComponent(PasswordCheckActivityModule(this))
            .inject(this)

        val passwordView = findViewById<EditText>(R.id.passwordView)
        val checkButton = findViewById<View>(R.id.checkButton)
        val errorView = findViewById<TextView>(R.id.errorView)

        var skipFirst = savedInstanceState != null
        passwordView.addTextChangedListener(afterTextChanged = {
            if (skipFirst) {
                skipFirst = false
            } else {
                errorView.visibility = View.GONE
            }
        })
        checkButton.setOnClickListener { viewModel.onCheckPressed(passwordView.text.toString()) }

        viewModel.viewState.observe(this) { state ->
            when (state) {
                is PasswordCheckViewModel.ViewState.Checked -> {
                    passwordView.isEnabled = true
                    checkButton.isEnabled = true
                    passwordView.setText("")
                    errorView.visibility = View.GONE
                }
                is PasswordCheckViewModel.ViewState.Error -> {
                    passwordView.isEnabled = true
                    checkButton.isEnabled = true
                    errorView.visibility = View.VISIBLE
                }
                PasswordCheckViewModel.ViewState.Progress -> {
                    passwordView.isEnabled = false
                    checkButton.isEnabled = false
                    errorView.visibility = View.GONE
                }
            }
        }

        viewModel.resultState.observe(this) { strength ->
            startActivity(PasswordCheckResultActivity.newIntent(this, strength))
        }
    }
}

package com.example.uitesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uitesting.domain.PasswordRepository
import com.example.uitesting.domain.PasswordStrength
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

class PasswordCheckViewModel(private val repository: PasswordRepository) : ViewModel() {

    private val mutableViewState = MutableLiveData<ViewState>()
    private val mutableResultState = SingleLiveEvent<PasswordStrength>()
    private var subscription: Subscription? = null

    val viewState: LiveData<ViewState>
        get() = mutableViewState
    val resultState: LiveData<PasswordStrength>
        get() = mutableResultState

    override fun onCleared() {
        subscription?.unsubscribe()
        subscription = null
    }

    fun onCheckPressed(password: String) {
        subscription?.unsubscribe()
        subscription = repository.check(password)
            .map<ViewState> { strength -> ViewState.Checked(strength) }
            .onErrorReturn { error -> ViewState.Error(error) }
            .toObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(ViewState.Progress)
            .subscribe { state ->
                if (state is ViewState.Checked) {
                    mutableResultState.value = state.strength
                }
                mutableViewState.value = state
            }
    }

    sealed class ViewState {
        class Checked(val strength: PasswordStrength) : ViewState()
        object Progress : ViewState()
        class Error(val error: Throwable) : ViewState()
    }
}

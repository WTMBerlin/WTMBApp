package com.wtmberlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wtmberlin.data.SecureMeetupService
import com.wtmberlin.data.TokenResponse
import com.wtmberlin.data.accessToken
import com.wtmberlin.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MeetupAuthViewModel(private val securedMeetupService: SecureMeetupService) : ViewModel() {
    val navigateToMainScreen = MutableLiveData<Event>()
    private val subscriptions = CompositeDisposable()

    fun processAuthResult(result: MeetupAuthResult) {
        subscriptions
            .add(
                securedMeetupService.authorize(code = result.code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onAuthorized)
            )
    }

    private fun onAuthorized(tokenResponse: TokenResponse) {
        accessToken = tokenResponse.access_token

        navigateToMainScreen.value = Event()
    }

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }
}
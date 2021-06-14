package com.example.uitesting.named

import com.example.uitesting.R
import com.example.uitesting.named.NamedViewMatcher.Companion.withId

object PasswordCheckResultLookup {

    fun matchesResultView(): NamedViewMatcher {
        return NamedViewMatcher(
            "Поле результат",
            withId(R.id.resultView)
        )
    }
}

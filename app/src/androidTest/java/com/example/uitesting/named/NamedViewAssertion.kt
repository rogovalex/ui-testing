package com.example.uitesting.named

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions

class NamedViewAssertion private constructor(
    private val name: String,
    private val assertion: ViewAssertion
) : ViewAssertion by assertion {

    override fun toString(): String {
        return name
    }

    companion object {

        fun matches(matcher: NamedViewMatcher): NamedViewAssertion {
            return NamedViewAssertion(matcher.toString(), ViewAssertions.matches(matcher))
        }

        fun doesNotExist(): NamedViewAssertion {
            return NamedViewAssertion("не существует", ViewAssertions.doesNotExist())
        }
    }
}

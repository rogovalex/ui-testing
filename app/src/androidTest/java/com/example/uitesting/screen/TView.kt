package com.example.uitesting.screen

import android.view.View
import org.hamcrest.Matcher

class TView(matcher: Matcher<View>) : TBaseView<TView>(matcher)

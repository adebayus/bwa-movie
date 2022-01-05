package com.sebade.mymovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sebade.mymovie.R
import com.sebade.mymovie.databinding.ActivityOnboardingOneBinding
import com.sebade.mymovie.databinding.ActivityOnboardingTwoBinding

class OnboardingTwoActivity : AppCompatActivity() {

    private lateinit var activityOnboardingTwoBinding: ActivityOnboardingTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var activityOnboardingTwoBinding = ActivityOnboardingTwoBinding.inflate(layoutInflater)
        setContentView(activityOnboardingTwoBinding.root)

        with(activityOnboardingTwoBinding) {
            btnHome.setOnClickListener({
                var intent = Intent(this@OnboardingTwoActivity, OnboardingThreeActivity::class.java)
                startActivity(intent)
            })
        }
    }
}
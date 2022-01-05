package com.sebade.mymovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sebade.mymovie.sign.signing.SignInActivity
import com.sebade.mymovie.databinding.ActivityOnboardingThreeBinding

class OnboardingThreeActivity : AppCompatActivity() {

    private lateinit var activityOnboardingThreeBinding : ActivityOnboardingThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnboardingThreeBinding = ActivityOnboardingThreeBinding.inflate(layoutInflater)
        setContentView(activityOnboardingThreeBinding.root)

        with(activityOnboardingThreeBinding,{
            btnHome.setOnClickListener({
                finishAffinity()
                var intent = Intent(this@OnboardingThreeActivity, SignInActivity::class.java)
                startActivity(intent)
            })
        })

    }
}
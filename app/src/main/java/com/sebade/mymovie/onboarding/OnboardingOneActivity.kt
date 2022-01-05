package com.sebade.mymovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sebade.mymovie.HomeActivity
import com.sebade.mymovie.sign.signing.SignInActivity
import com.sebade.mymovie.databinding.ActivityOnboardingOneBinding
import com.sebade.mymovie.utils.Preferences

class OnboardingOneActivity : AppCompatActivity() {

    private lateinit var activityOnboardingOneBinding : ActivityOnboardingOneBinding
    private lateinit var preference : Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnboardingOneBinding = ActivityOnboardingOneBinding.inflate(layoutInflater)
        setContentView(activityOnboardingOneBinding?.root)

        preference = Preferences(this@OnboardingOneActivity)

        if (preference.getValue("onboarding") == "1"){
            finishAffinity()
            var intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        with(activityOnboardingOneBinding) {
            btnDaftar.setOnClickListener {
                var intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
                startActivity(intent)
            }

            btnHome.setOnClickListener{
                finishAffinity()
                var intent =  Intent(this@OnboardingOneActivity, SignInActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
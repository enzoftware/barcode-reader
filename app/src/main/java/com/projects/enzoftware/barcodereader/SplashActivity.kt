package com.projects.enzoftware.barcodereader

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_welcome.*

class SplashActivity : AppCompatActivity() {

    private lateinit var upToDown: Animation
    private lateinit var downToUp : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar!!.hide()
        upToDown = AnimationUtils.loadAnimation(this,R.anim.up_to_down)
        downToUp = AnimationUtils.loadAnimation(this,R.anim.down_to_up)
        linearLayoutSuperior.animation = upToDown
        linearLayoutInferior.animation = downToUp
        buttonStart.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }
}

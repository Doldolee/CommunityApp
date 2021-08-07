package com.example.mysololife.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mysololife.R
import com.example.mysololife.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        auth = Firebase.auth

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            //로그아웃 후 기존의 액티비티를 모두 지워주는 코드, 안지워주면 뒤로가기 누르면 뒤로가짐
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}
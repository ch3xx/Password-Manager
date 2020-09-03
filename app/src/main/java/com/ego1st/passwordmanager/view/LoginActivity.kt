package com.ego1st.passwordmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ego1st.passwordmanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        logInAuto(auth.currentUser)
    }

    fun logIn(view: View){
        var email = getEmailText.text.toString()
        var password = getPasswordText.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Başarıyla Giriş Yapıldı", Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Giriş başarısız sebep: " + task.exception, Toast.LENGTH_LONG).show()
                    Toast.makeText(baseContext, "Kimlik doğrulama hatalı!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun logInAuto(user: FirebaseUser? = null){
        if(user != null){
            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}
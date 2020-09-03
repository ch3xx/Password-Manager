package com.ego1st.passwordmanager.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.ego1st.passwordmanager.Account
import com.ego1st.passwordmanager.R
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    lateinit var chip: Chip
    var accessToken: Int = 0

    private var firebaseFirestore: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        accessToken = (100000..999999).random()
        textAccessToken.text = accessToken.toString()
    }

    fun save(view: View){
        var chipText: String? = null

        val ids = chipGroup.checkedChipIds

            for (id in ids) {
                chip = chipGroup.findViewById(id!!)
                chipText = chip.text.toString()
            }

        var email: String? = getEmailInput.text.toString()
        var password: String? = getPasswordInputAgain.text.toString()
        var passwordAgain: String? = getPasswordInput.text.toString()

        if(chipText != "" && email != "" && password != "" && passwordAgain != ""){
            if(password == passwordAgain){

                val account = Account(chipText!!, email!!, password!!, passwordAgain!!, accessToken)
                firebaseFirestore.collection("Inputs").add(account)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(applicationContext, "Kayıt başarıyla eklendi", LENGTH_LONG).show()
                        intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(applicationContext, e.localizedMessage, LENGTH_LONG).show()
                    }

            } else {
                Toast.makeText(applicationContext, "Şifreler eşleşmiyor", LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "Lütfen boş bırakılan alanları doldurunuz", LENGTH_LONG).show()
        }
    }

    fun copy(view: View){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("access token", accessToken.toString())
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(applicationContext, "Erişim kodu kopyalandı", Toast.LENGTH_LONG).show()
    }
}
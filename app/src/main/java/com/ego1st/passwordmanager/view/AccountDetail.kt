package com.ego1st.passwordmanager.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.ego1st.passwordmanager.Account
import com.ego1st.passwordmanager.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_account_detail.*
import java.util.*
import kotlin.collections.HashMap

class AccountDetail : AppCompatActivity() {

    var email: String? = null
    private var db: FirebaseFirestore? = null
    private var reference: DocumentReference? = null
    var patientRefC: CollectionReference? = null
    var DOCUMENT_ID : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)

        textEmail.inputType = InputType.TYPE_NULL

        textEmail.setOnLongClickListener{
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("e-mail", textEmail.text.toString())
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(applicationContext, "E-posta kopyalandı", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }

        textPassword.inputType = InputType.TYPE_NULL

        textPassword.setOnLongClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("password", textPassword.text.toString())
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(applicationContext, "Şifre kopyalandı", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }

        //BUG'LI
        switchChange?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                textEmail.inputType = TYPE_TEXT_VARIATION_NORMAL
                textEmail.focusable = View.FOCUSABLE
                textPassword.inputType = TYPE_TEXT_VARIATION_NORMAL
                textPassword.focusable = View.FOCUSABLE
            } else {
                textEmail.inputType = InputType.TYPE_NULL
                textEmail.focusable = View.NOT_FOCUSABLE
                textPassword.inputType = InputType.TYPE_NULL
                textPassword.focusable = View.NOT_FOCUSABLE
            }
        }

        email = intent.getStringExtra("email")

        db = Firebase.firestore
        patientRefC = db!!.collection("patients");

        getDataFromFirebase()
    }

    fun getDataFromFirebase(){

        var collectionReference: CollectionReference = db!!.collection("Inputs")
        var query: Query = collectionReference.whereEqualTo("email", email)

        query.get().addOnCompleteListener(OnCompleteListener { task ->

            if (task.isSuccessful) {
                for (documentSnapshot: DocumentSnapshot in task.getResult()!!) {

                    var account: Account? = documentSnapshot.toObject(Account::class.java)
                    if (account != null) {

                        account.documentId = documentSnapshot.id

                        var documentId: String = account.documentId!!
                        DOCUMENT_ID = documentId
                        reference = db!!.collection("Inputs").document(DOCUMENT_ID!!)

                        textEmail.setText(account.email)
                        textPassword.setText(account.password)

                        val x = account.platform.decapitalize(Locale.ROOT)
                        var imagePlatform: Int = 0
                        when (x) {
                            "google" -> imagePlatform = R.drawable.google
                            "facebook" -> imagePlatform = R.drawable.facebook
                            "instagram" -> imagePlatform = R.drawable.instagram
                            "github" -> imagePlatform = R.drawable.github
                            "discord" -> imagePlatform = R.drawable.discord
                        }
                        imageViewPlatform.setImageResource(imagePlatform)
                    }
                }
            }
        })
    }

    fun update(view: View){

        var data = HashMap<String, Any>()
        data.put("email", textEmail.text.toString())
        data.put("password", textPassword.text.toString())
        data.put("passwordAgain", textPassword.text.toString())

        reference!!.update(data).addOnSuccessListener {
            Toast.makeText(applicationContext, "Kayıt başarıyla güncellendi", LENGTH_LONG).show()
            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    fun delete(view: View){
        reference!!.delete().addOnSuccessListener {
            Toast.makeText(applicationContext, "Kayıt başarıyla silindi", LENGTH_LONG).show()
            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }.addOnFailureListener { e->
            Toast.makeText(applicationContext, e.localizedMessage, LENGTH_LONG).show()
        }
    }
}
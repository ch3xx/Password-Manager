package com.ego1st.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.ego1st.passwordmanager.view.AccountDetail
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_account_detail.*
import kotlinx.android.synthetic.main.activity_transfer_area.*
import java.util.*

class TransferArea : AppCompatActivity() {

    var email: String? = null
    private var db: FirebaseFirestore? = null
    private var reference: DocumentReference? = null
    var patientRefC: CollectionReference? = null
    var DOCUMENT_ID : String? = null
    var accessToken: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_area)

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

                        accessToken = account.accessCode
                        println(accessToken)
                    }
                }
            }
        })
    }

    fun transfer(view: View){
        val tokenInput: Int? = getAccessToken.text.toString().toIntOrNull()
        if(tokenInput != null){
            if(tokenInput == accessToken){
                intent = Intent(this, AccountDetail::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Erişim kodunuz yanlış", LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "Lütfen değer giriniz", LENGTH_LONG).show()
        }
    }
}
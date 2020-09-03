package com.ego1st.passwordmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ego1st.passwordmanager.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var firebaseFirestore: FirebaseFirestore = Firebase.firestore

    var platformList = ArrayList<String>()
    var emailList = ArrayList<String>()

    var adapter: MainRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = MainRecyclerAdapter(emailList, platformList)
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(this, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(v: View?, position: Int) {
                    val email = emailList[position]
                    intent = Intent(this@MainActivity, TransferArea::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            })
        )
    }

    fun getData(){
        firebaseFirestore.collection("Inputs").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage.toString(),
                    LENGTH_LONG
                ).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        for (document in documents) {
                            val account: Account? = document.toObject<Account>()
                            platformList.add(account!!.platform.decapitalize(Locale.ROOT))
                            emailList.add(account!!.email)

                            adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_something -> {
                intent = Intent(this@MainActivity, AddActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
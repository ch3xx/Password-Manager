package com.ego1st.passwordmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainRecyclerAdapter(private val emailArray: ArrayList<String>, private val imageArray : ArrayList<String>) : RecyclerView.Adapter<MainRecyclerAdapter.InfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return InfoHolder(view)
    }

    override fun getItemCount(): Int {
        return emailArray.size
    }

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.recyclerEmailText?.text = emailArray[position]

        val x = imageArray[position].decapitalize(Locale.ROOT)
        var imagePlatform: Int = 0
        when(x){
            "google" -> imagePlatform = R.drawable.google
            "facebook" -> imagePlatform = R.drawable.facebook
            "instagram" -> imagePlatform = R.drawable.instagram
            "github" -> imagePlatform = R.drawable.github
            "discord" -> imagePlatform = R.drawable.discord
        }

        holder.recyclerImageView?.setImageResource(imagePlatform)
    }

    class InfoHolder(view : View) : RecyclerView.ViewHolder(view) {
        var recyclerEmailText : TextView? = null
        var recyclerImageView : ImageView? = null

        init {
            recyclerEmailText = view.findViewById(R.id.row_text_email)
            recyclerImageView = view.findViewById(R.id.row_image_platform)

        }

    }
}

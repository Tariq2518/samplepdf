package com.example.samplepdf

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class booksViewAdapter(val list: ArrayList<File>, val context: Context,  private val listener: MyInterface? = null) : RecyclerView.Adapter<booksViewAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val bookName = itemView.findViewById<TextView>(R.id.bookName)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.files, parent, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bookName.text = list[position].name

        holder.bookName.setOnClickListener {
            listener?.onItemClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}

interface MyInterface{
    fun onItemClick(file: File)
}
package com.example.samplepdf

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity(), MyInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val files =  getAllPdfs()

        files.forEach {
            Log.i("TLogs", "onCreate: ${it.name}")
        }

        val booklist : RecyclerView = findViewById(R.id.bookList)

        booklist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        booklist.adapter = booksViewAdapter(files, this, this)

    }

    private fun getAllPdfs(): ArrayList<File> {
       // canClick = false
        val filesList = ArrayList<File>()
        try {
            val pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            val selection = (MediaStore.Files.FileColumns.MIME_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?")
            val args = arrayOf(pdf)
            val cursor: Cursor? = this.contentResolver.query(uri, projection, selection, args, null)
            val files = cursor?.count?.let { arrayOfNulls<File>(it) }
            cursor?.moveToFirst()
            var i = 0
            if (cursor?.count != 0) {
                do {
                    files?.set(i, File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))))
                    if (files?.get(i)?.name?.endsWith(".pdf")!! && files[i]?.length()!! > 0) {
                        filesList.add(files[i]!!)
                    }
                    i++
                } while (cursor?.moveToNext()!!)
            }
            Log.i("TLogs", "getAll: ${filesList.size}")
            cursor.close()
        } catch (e: Exception) {
            Log.i("TLogs", "getAllPdfs: $e")
        }
//        when (sharePref.getInt("sorting", 1)) {
//            1 -> {
//                filesList.sortBy {
//                    it.name.lowercase()
//                }
//            }
//            2 -> {
//                filesList.sortBy {
//                    it.lastModified()
//                }
//            }
//            3 -> {
//                filesList.sortBy {
//                    it.length()
//                }
//            }
//        }
        filesList.sortBy {
            it.name
        }
//        filesList.forEach {
//            Log.i("TLogs", "getAll: ${it.name}, ${it.path}")
//        }
        return filesList
    }

    override fun onItemClick(file: File) {
        val bundle = Bundle()
        bundle.putString("file", file.absolutePath.toString())

        val intent = Intent(this, PdfViewActivity::class.java)
        intent.putExtra("file", file.path)
        startActivity(intent)
    }


}
package com.example.contentproviderexample

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide


class PhotoCursorAdapter(context: Context, cursor: Cursor?, autoRequery: Boolean) :
    CursorAdapter(context, cursor, autoRequery) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val imageView = view?.findViewById<ImageView>(R.id.iv_photo)
        val uri =
            cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

        Glide.with(context!!).load(uri).into(imageView!!)
    }
}
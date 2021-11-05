package com.example.contentproviderexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.GridView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridView = findViewById<GridView>(R.id.gv_photo)

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,       // ContentProvider가 관리하는 URI
            null,                                      // SELECT (가져올 칼럼명)
            null,                                       // WHERE (필터링할 컬럼명 지정)
            null,                                    // 프리페어드 스테이트먼트 (SELECTION으로 지정한 칼럼명의 조건을 설정)
            MediaStore.Images.ImageColumns.DATE_TAKEN           // ORDER BY (정렬 기준)
        )

        val photoCursorAdapter = PhotoCursorAdapter(this, cursor, false)
        gridView.adapter = photoCursorAdapter
    }
}
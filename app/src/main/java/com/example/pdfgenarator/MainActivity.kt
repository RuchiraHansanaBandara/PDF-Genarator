package com.example.pdfgenarator

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var ScrollText : TextView
    private lateinit var gen_PDF : Button
    private val STORAGE_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ScrollText = findViewById(R.id.ScrollText)
        gen_PDF = findViewById(R.id.gen_PDF)

        gen_PDF.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                }else{
                    savePDF()
                }
            }else{
                savePDF()
            }
        }
    }

    private fun savePDF() {
        val mDoc = com.itextpdf.text.Document()

        val mFileName = SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())

        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"

        try {

            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()

            val data = ScrollText.text.toString().trim()
            mDoc.addAuthor("Ruchira Bandara")
            mDoc.add(Paragraph(data))
            mDoc.close()
            Toast.makeText(this, "$mFileName.pdf\n is create to\n $mFilePath", Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF()
                }else{
                    Toast.makeText(this, "Permission denied !", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
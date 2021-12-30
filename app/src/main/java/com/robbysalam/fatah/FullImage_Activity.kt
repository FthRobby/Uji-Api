package com.robbysalam.fatah

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.robbysalam.fatah.Helper.TouchImageView
import com.fatah.R
import java.io.IOException

class FullImage_Activity : AppCompatActivity() {

    private var imgeurl: String? = null
    internal lateinit var wallpaperManager: WallpaperManager
    internal lateinit var img_wallpaper: TouchImageView
    internal lateinit var bitmap1: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image_)

        /*
         mendapatkan data dari aktivitas sebelumnya dengan mengirim url
         */
        if (intent != null) {
            imgeurl = intent.getStringExtra("imgeurl")
        }
        img_wallpaper = findViewById(R.id.img_wallpaper)

        /*
        megatur url gambar menuju imageview menggunakan glide, disini gambar bisa di zoom
         */
        Glide.with(this).load(imgeurl)
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(img_wallpaper)
    }

    /*
    mengatur gabar agar bisa dijadikan wallpaper background
    */
    fun OnApplyclick(view: View) {
        wallpaperManager = WallpaperManager.getInstance(this@FullImage_Activity)

        try {


            bitmap1 = (img_wallpaper.drawable as BitmapDrawable).bitmap

            wallpaperManager.setBitmap(bitmap1)

            finish()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

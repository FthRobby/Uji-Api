package com.robbysalam.fatah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.robbysalam.fatah.API.ApiRequests
import com.robbysalam.fatah.API.JsonParser
import com.robbysalam.fatah.CustomAdatapter.PhotoListAdapter
import com.robbysalam.fatah.Helper.Utils
import com.robbysalam.fatah.Model.ImagesUrl_Model
import com.fatah.R
import com.robbysalam.fatah.listeners.ListItemClickListener
import com.robbysalam.fatah.listeners.ResponseListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_image_detail.*
import java.util.*

class ImageDetail_Activity : AppCompatActivity(), ResponseListener, ListItemClickListener {


    var mImagesUrl_Model: ImagesUrl_Model? = null
    var img_Photo: ImageView? = null
    private var img_userprofile: CircleImageView? = null
    internal var mImagesUrl_ModelList: MutableList<ImagesUrl_Model> = ArrayList<ImagesUrl_Model>()

    private var mPhotoListAdapter: PhotoListAdapter? = null
    private val loadMore = false
    private var loadBar: ProgressBar? = null

    private lateinit var rvRelated: RecyclerView
    var nestedSV: NestedScrollView? = null

    private var mUtils: Utils? = null
    var tv_userName: TextView? = null
    var tv_desc: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        setSupportActionBar(toolbar)

        if (intent != null) {
            mImagesUrl_Model = intent.extras!!.getSerializable("list") as ImagesUrl_Model?

        }


        iniView()
        mImagesUrl_ModelList.clear()
        /// memnaggil function untuk API
        loadMoreData(mImagesUrl_Model?.photo_id ?: "")


    }

    fun iniView() {

        mUtils = Utils(this@ImageDetail_Activity)
        img_Photo = findViewById(R.id.img_Photo)
        img_userprofile = findViewById(R.id.img_userprofile)
        nestedSV = findViewById(R.id.scrollView)
        loadBar = findViewById(R.id.loadBar)

        tv_userName = findViewById(R.id.tv_userName)
        tv_desc = findViewById(R.id.tv_desc)


        rvRelated = findViewById(R.id.rvRelated)
        rvRelated.setHasFixedSize(true)
        rvRelated.isNestedScrollingEnabled = false
        rvRelated.setItemViewCacheSize(10)

        setdata()


    }


    private fun loadMoreData(str_msg: String) {

        //check internet connection is available ot not if not than show the toast message to  user
        if (mUtils!!.isWifi(this)) {

            /*
            memanggil apigambar yang terkait
            inisialisasai dan memanggilnya menggunakan retrofit
             */

            ApiRequests(this@ImageDetail_Activity, this).getApiRelatedImage(str_msg)

        } else {
            loadBar!!.visibility = View.GONE
            mUtils!!.showToast(resources.getString(R.string.nointernet))
        }
    }


    fun setdata() {

        /*
        atur data dari ImagesUrl_Model menuju textview dan imagesview
         */
        tv_userName!!.text = mImagesUrl_Model!!.username
        if (mImagesUrl_Model!!.description.equals("null") || mImagesUrl_Model!!.description!!.isEmpty()) {
            tv_desc!!.visibility = View.GONE
        } else {
            tv_desc!!.text = mImagesUrl_Model?.description
        }


        /*
        atur url gambar ke Imageview menggunakna  Glide lib
         */
        img_Photo?.let {
            Glide.with(this).load(mImagesUrl_Model?.regular)
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(it)
        }

        /*
        atur url gambar ke User profile Imageview menggunakan  Glide lib
         */

        img_userprofile?.let {
            Glide.with(this).load(mImagesUrl_Model?.profileImage)
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(it)
        }

    }


    override fun onSuccess(`object`: String, action: String) {

        mImagesUrl_ModelList.clear()
        mImagesUrl_ModelList.addAll(JsonParser.json2Related_ImageList(`object`))
        if (mImagesUrl_ModelList.size > 0) {
            mPhotoListAdapter =
                PhotoListAdapter(this@ImageDetail_Activity, mImagesUrl_ModelList, this)
            rvRelated.adapter = mPhotoListAdapter
        }

        loadBar?.visibility = View.GONE

    }

    override fun onError(message: String) {
        loadBar?.visibility = View.GONE
    }


    override fun onListItemClick(position: Int, action: String) {
        mImagesUrl_Model = mImagesUrl_ModelList[position]
        setdata()
    }


    fun OnDetailMenuClick(view: View) {
        when (view.id) {
            R.id.img_Photo ->

                startActivity(
                    Intent(this@ImageDetail_Activity, FullImage_Activity::class.java)
                        .putExtra("imgeurl", mImagesUrl_Model!!.full)
                )
            R.id.img_download -> {
                /*
                download click
                 */
            }
            R.id.img_Share -> {
                /*
                Share click
                 */
            }
        }
    }

}

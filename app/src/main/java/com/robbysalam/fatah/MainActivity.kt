package com.robbysalam.fatah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.robbysalam.fatah.API.ApiRequests
import com.robbysalam.fatah.API.JsonParser
import com.robbysalam.fatah.CustomAdatapter.PhotoListAdapter
import com.robbysalam.fatah.Helper.Utils
import com.robbysalam.fatah.Model.ImagesUrl_Model
import com.fatah.R
import com.robbysalam.fatah.listeners.ListItemClickListener
import com.robbysalam.fatah.listeners.ResponseListener
import java.io.Serializable
import java.util.*

class MainActivity : AppCompatActivity(), ResponseListener, ListItemClickListener {


    private var loadMore = false

    private lateinit var mImage_RecyclerView: RecyclerView
    private lateinit var nestedSV: NestedScrollView

    private var loadBar: ProgressBar? = null
    var mLayout_recyclerView: GridLayoutManager? = null

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    internal var swithlayout: ImageView? = null

    var pageno = 1
    private var isvertical = true

    var mUtils: Utils? = null
    internal var mImagesUrl_ModelList: MutableList<ImagesUrl_Model> = ArrayList<ImagesUrl_Model>()

    // custom Photo Adapter
    private var mPhotoListAdapter: PhotoListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUtils = Utils(this@MainActivity)
        //inisialisasi views
        iniView()
        ///memanggil funciton untuk API
        loadMoreData(1, "firsttime")

    }

    fun iniView() {

        mImage_RecyclerView = findViewById(R.id.rv_image)
        nestedSV = findViewById(R.id.scrollView)
        loadBar = findViewById(R.id.loadBar)
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout)
        swithlayout = findViewById(R.id.swithlayout)

        mLayout_recyclerView = GridLayoutManager(this@MainActivity, 2)
        mImage_RecyclerView.setHasFixedSize(true)
        mImage_RecyclerView.isNestedScrollingEnabled = false
        mImage_RecyclerView.setItemViewCacheSize(10)
        mImage_RecyclerView.layoutManager = mLayout_recyclerView

        /*
            tarik dari atas ke bawah untuk merefresh data gambar dari server.
         */
        mSwipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loadBar!!.visibility = View.VISIBLE
            /// memanggil aAPI untuk gambar lainya
            loadMoreData(pageno, "load_more")

            mSwipeRefreshLayout!!.isRefreshing = false


        })

        /*
        periksa scrollview bersarang mencapai bagian bawah recyclerview atau tidak
        */

        nestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!loadMore) {

                    loadBar!!.visibility = View.VISIBLE
                    loadMore = true
                    pageno = pageno + 1

                    loadMoreData(pageno, "load_more")

                }
            }
        })
    }

    private fun loadMoreData(page: Int, str_msg: String) {

        if (mUtils!!.isWifi(this)) {

            ApiRequests(this@MainActivity, this).getApiRequestMethodarray(page, str_msg)

        } else {
            loadBar!!.visibility = View.GONE
            mUtils!!.showToast(resources.getString(R.string.nointernet))
        }
    }

    override fun onSuccess(`object`: String, action: String) {

        loadMore = false
        mImagesUrl_ModelList.addAll(JsonParser.json2ImageList(`object`))

        if (mImagesUrl_ModelList.size > 0) {


            mPhotoListAdapter = PhotoListAdapter(this@MainActivity, mImagesUrl_ModelList, this)
            mImage_RecyclerView.adapter = mPhotoListAdapter

            loadMore = false

        } else {
            loadBar!!.visibility = View.GONE
            loadMore = true
        }

        loadBar!!.visibility = View.GONE
    }

    override fun onError(message: String) {
        loadMore = false
        mUtils!!.showToast(message)
        loadBar!!.visibility = View.GONE

    }

    override fun onListItemClick(position: Int, action: String) {


        val mImagesUrl_Model = mImagesUrl_ModelList[position]
        startActivity(
            Intent(this, ImageDetail_Activity::class.java)
                .putExtra("list", mImagesUrl_Model as Serializable)
                .putExtra("pos", position)
        )

    }

    fun OnSwitchclick(view: View) {

        if (isvertical) {
            swithlayout!!.setImageResource(R.drawable.ic_view_list_black)
            isvertical = false
            switchList(true)
        } else {
            swithlayout!!.setImageResource(R.drawable.ic_grid_on_black)
            switchList(false)
            isvertical = true
        }

    }


    fun switchList(isVertical: Boolean) {
        if (isVertical) {
            val specManager = LinearLayoutManager(this)
            specManager.orientation = LinearLayoutManager.VERTICAL

            mImage_RecyclerView.layoutManager = specManager
            mImage_RecyclerView.adapter = mPhotoListAdapter
        } else {
            val mGridLayoutManager = GridLayoutManager(this@MainActivity, 2)

            mImage_RecyclerView.layoutManager = mGridLayoutManager
            mImage_RecyclerView.adapter = mPhotoListAdapter
        }
    }


}

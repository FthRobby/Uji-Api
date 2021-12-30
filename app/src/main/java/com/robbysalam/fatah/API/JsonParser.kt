package com.robbysalam.fatah.API


import android.util.Log
import com.robbysalam.fatah.Model.ImagesUrl_Model
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


object JsonParser {


    /*
    menampilkan wallpaper dari API poto
     */
    fun json2ImageList(jsonObject: String): List<ImagesUrl_Model> {

        var jsonArray: JSONArray? = null
        try {
            jsonArray = JSONArray(jsonObject)
        } catch (e: JSONException) {

        }

        val mImageModelslist = ArrayList<ImagesUrl_Model>()
        try {

            if (jsonArray!!.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    /*
                    Menampilkan Gambar dari API
                    panggil metode yang menguraikan dan mengembalikan model POJO dan menyimpan dalam daftar
                     */
                    val mImagesUrl_Model = json2ImageUrl(jsonArray.getJSONObject(i))

                    mImageModelslist.add(mImagesUrl_Model)
                }
            }




            return mImageModelslist
        } catch (e: JSONException) {

            return ArrayList()
        }


    }

    /*
    Parsing gambar dari objek Photo Json dan
    kembalikan kelas Photo pojo
    */
    fun json2ImageUrl(ProdDetail_obj: JSONObject): ImagesUrl_Model {


        val mImagesUrl_Model = ImagesUrl_Model()

        try {
            val object_url = ProdDetail_obj.getJSONObject("urls")
            val objectUser = ProdDetail_obj.getJSONObject("user")


            //parse and set  photo user
            if (ProdDetail_obj.has("id"))
                mImagesUrl_Model.photo_id = ProdDetail_obj.getString("id")
            if (ProdDetail_obj.has("description"))
                mImagesUrl_Model.description = ProdDetail_obj.getString("description")


            /*
               start Image url parsing
               check image url in Url Json ojbject parse and set  photo
                */
            if (object_url.has("thumb"))
                mImagesUrl_Model.thumb = object_url.getString("thumb")

            if (object_url.has("small"))
                mImagesUrl_Model.small = object_url.getString("small")

            if (object_url.has("regular"))
                mImagesUrl_Model.regular = object_url.getString("regular")

            if (object_url.has("full"))
                mImagesUrl_Model.full = object_url.getString("full")


            /*
               start User info parsing
                               */
            if (objectUser.has("id"))
                mImagesUrl_Model.user_id = objectUser.getString("id")

            if (objectUser.has("username"))
                mImagesUrl_Model.username = objectUser.getString("username")

            if (objectUser.has("location"))
                mImagesUrl_Model.location = objectUser.getString("location")

            if (objectUser.has("profile_image")) {
                val objectUserProfile = objectUser.getJSONObject("profile_image")
                if (objectUserProfile.has("medium")) {
                    mImagesUrl_Model.profileImage = objectUserProfile.getString("medium")
                } else if (objectUserProfile.has("large")) {
                    mImagesUrl_Model.profileImage = objectUserProfile.getString("large")
                } else {
                    mImagesUrl_Model.profileImage = objectUserProfile.getString("small")
                }
            }

        } catch (ex: Exception) {

        }



        return mImagesUrl_Model


    }


    /*
   Menguraikan semua wallpaper dari respon API Terkait
    */
    fun json2Related_ImageList(jsonObject: String): List<ImagesUrl_Model> {

        var jsonArray: JSONArray? = null
        try {
            val `object` = JSONObject(jsonObject)

            if (`object`.has("related_collections")) {
                jsonArray = `object`.getJSONObject("related_collections").getJSONArray("results")
            }

        } catch (e: JSONException) {

        }

        val mImageModelslist = ArrayList<ImagesUrl_Model>()
        try {

            Log.e("jsonarra", jsonArray!!.toString() + "")
            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {

                    mImageModelslist.addAll(json2RelatedImageUrl(jsonArray.getJSONObject(i)))


                }
            }




            return mImageModelslist
        } catch (e: JSONException) {

            return ArrayList()
        }


    }

    /*
    menampilkan gambar dari object foto berupa json
    */
    fun json2RelatedImageUrl(ProdDetail_obj: JSONObject): List<ImagesUrl_Model> {


        val mImageModelslist = ArrayList<ImagesUrl_Model>()
        try {


            val jsonArray = ProdDetail_obj.getJSONArray("preview_photos")


            val objectUser = ProdDetail_obj.getJSONObject("user")

            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    val mImagesUrl_Model_ = ImagesUrl_Model()

                    val object_url = jsonArray.getJSONObject(i).getJSONObject("urls")

                    //parse and set  photo user
                    if (ProdDetail_obj.has("id"))
                        mImagesUrl_Model_.photo_id = ProdDetail_obj.getString("id")
                    if (ProdDetail_obj.has("description"))
                        mImagesUrl_Model_.description = ProdDetail_obj.getString("description")

                    if (object_url.has("thumb"))
                        mImagesUrl_Model_.thumb = object_url.getString("thumb")

                    if (object_url.has("small"))
                        mImagesUrl_Model_.small = object_url.getString("small")

                    if (object_url.has("regular"))
                        mImagesUrl_Model_.regular = object_url.getString("regular")

                    if (object_url.has("full"))
                        mImagesUrl_Model_.full = object_url.getString("full")


                    if (objectUser.has("id"))
                        mImagesUrl_Model_.user_id = objectUser.getString("id")

                    if (objectUser.has("username"))
                        mImagesUrl_Model_.username = objectUser.getString("username")

                    if (objectUser.has("location"))
                        mImagesUrl_Model_.location = objectUser.getString("location")

                    if (objectUser.has("profile_image")) {
                        val objectUserProfile = objectUser.getJSONObject("profile_image")
                        if (objectUserProfile.has("medium")) {
                            mImagesUrl_Model_.profileImage = objectUserProfile.getString("medium")
                        } else if (objectUserProfile.has("large")) {
                            mImagesUrl_Model_.profileImage = objectUserProfile.getString("large")
                        } else {
                            mImagesUrl_Model_.profileImage = objectUserProfile.getString("small")
                        }
                    }


                    mImageModelslist.add(mImagesUrl_Model_)
                }
            }


        } catch (ex: Exception) {

        }



        return mImageModelslist


    }


}

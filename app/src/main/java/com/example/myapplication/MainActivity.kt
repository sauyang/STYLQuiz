package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.UserBean
import com.example.myapplication.util.UtilSharedPreferences
import com.example.myapplication.util.UtilStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream

class MainActivity : BaseActivity() {

    private var userList : ArrayList<UserBean> =  ArrayList<UserBean>()
    private val cacheTag : String = "user bean cache"

    override val uiContentId: Int
        get() = R.layout.activity_main

    override fun uiData() {
        showRegisteredList()
    }

    override fun uiFlow(context: Context) {
        // set recycler view adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_content.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rv_content.adapter =
            AdpUserList(this, userList, object : OnItemClickListener {
                override fun onPress(num: Int, userBean: UserBean) {
                    if(UtilSharedPreferences.isFastClick()){
                        //Prevent FastClick
                    }else {
                        var intent = Intent(context, EditActivity::class.java)
                        intent.putExtra(EditActivity.request_data, userBean)
                        startActivityForResult(intent, EditActivity.request_tag)
                    }
                }
            })
    }

    private fun showRegisteredList(){

        if(UtilSharedPreferences.getInstance(this)!!.isRegisterDone()){
           //retrieve list from local storage
            retrieveDataFromLocal()
            return
        }

//        if(userList != null){
//            userList.add(UserBean("1","styl.png","nameisUser","Password","Mark","Hello","markhello@gmail.com","22"," Running"))
//
//
//            userList.forEachIndexed { index, user ->
//                var nameImg = user.imagePath
//                var ima = getAssetData(nameImg)
//                if (ima != null) {
//                    UtilStorage(baseContext,object : UtilStorage.StorageListener{
//                        override fun onSuccess(path: String) {
//                            // set new path
//                            userList.get(index).imagePath
//                        }
//                        override fun onFail(message: String) {
//                            // do nothing
//                        }
//                    }).saveToInternalStorage(ima)
//                }
//            }
//
//            UtilSharedPreferences.getInstance(this)!!.setIsRegisterDone(false)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
           if(requestCode == EditActivity.request_tag){
                val userBean = data?.getSerializableExtra(EditActivity.request_data) as UserBean
                userList.forEachIndexed{ index, item ->
                    if(userBean.id.equals(item.id)){
                        userList.set(index,userBean)
                        rv_content.adapter?.notifyDataSetChanged()
                        storeDataFromLocal()
                    }
                }
            }

        }

    }


    private fun retrieveDataFromLocal(){
        val data = UtilSharedPreferences.getInstance(this)?.getData(cacheTag)
        val itemType = object : TypeToken<ArrayList<UserBean>>() {}.type
        this.userList =  Gson().fromJson(data, itemType)
    }


    private fun getAssetData(fileName: String) : Bitmap?{
        var assetInStream: InputStream? = null
        var bit : Bitmap? = null

        try {
            assetInStream = assets.open(fileName)
            bit = BitmapFactory.decodeStream(assetInStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (assetInStream != null)
                assetInStream!!.close()
            return bit
        }
    }


    private fun storeDataFromLocal(){
        UtilSharedPreferences.getInstance(this)?.saveData(cacheTag,Gson().toJson(userList))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {

            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_recipe, menu)

        return true
    }

}

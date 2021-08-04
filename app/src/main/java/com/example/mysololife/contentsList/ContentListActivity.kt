package com.example.mysololife.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {
    private lateinit var myRef : DatabaseReference
    val bookmarkIdList = mutableListOf<String>()

    private lateinit var rvAdapter :ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        // Write a message to the database
        val database = Firebase.database

        val category = intent.getStringExtra("category")


        if (category == "category1"){
            myRef = database.getReference("contents")
        }else if(category == "category2"){
            myRef = database.getReference("contents2")
        }

        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()
//      glide 띄우기 위해서 baseContext보내줌
        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

//
//      firebase 데이터 읽기_start
        val postListener = object : ValueEventListener {
            //제대로 실행된 경우, datasnapshot이 데이터베이스에 있는 덩어리 데이터임
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children){
                    //ContentModel형태로 데이터를 받음.
                    val item = dataModel.getValue(ContentModel::class.java)
                    Log.d("ContentListActivity", dataModel.toString())
                    Log.d("ContentListActivity", dataModel.key.toString())
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }
                //비동기 형태로 받기 때문에 Adapter를 새롭게 동기화시켜줘야함.
                rvAdapter.notifyDataSetChanged()

            }//error 떴을 경우 출력값 지정
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        //내가 지정한 myRef에 postListener를 연결하여 원하는 데이터베이스의 값 읽기
        myRef.addValueEventListener(postListener)
        //firebase 데이터 읽기_end

        val rv: RecyclerView = findViewById(R.id.rv)

        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this, 2)



//        rvAdapter.itemClick = object : ContentRVAdapter.ItemClick{
//            override fun onClick(view: View, position: Int) {
//                Toast.makeText(baseContext, items[position].title, Toast.LENGTH_LONG).show()
//
//                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
//                intent.putExtra("url", items[position].webUrl)
//                startActivity(intent)
//            }
//
//        }



//        myRef.push().setValue(ContentModel("title1","https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png","https://philosopher-chan.tistory.com/1249?category=941578"))
//        myRef.push().setValue(ContentModel("title2","https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png","https://philosopher-chan.tistory.com/1248?category=941578"))
//
//        items.add(ContentModel("title1","https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png","https://philosopher-chan.tistory.com/1249?category=941578"))
//        items.add(ContentModel("title2","https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png","https://philosopher-chan.tistory.com/1248?category=941578"))
        getBookmarkData()

    }
    private fun getBookmarkData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkIdList.clear()
                for (dataModel in dataSnapshot.children){
//                    Log.d("getBookmarkData",dataModel.key.toString())
//                    Log.d("getBookmarkData",dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString())
                }
                Log.d("getBookmarkData",bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

    }
}
package com.example.mysololife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.databinding.ActivityBoardEditBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key:String

    private lateinit var binding:ActivityBoardEditBinding

    private lateinit var writerUid:String

    private val Tag = BoardEditActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_board_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }



    }
    private fun editBoardData(key:String){
        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(binding.titleArea.text.toString(),
            binding.contentArea.text.toString(),
            writerUid,
            FBAuth.getTime()))
        Toast.makeText(this, "수정완료",Toast.LENGTH_LONG).show()

        finish()

    }

    private fun getBoardData(key:String){
        val postListener = object : ValueEventListener {
            //제대로 실행된 경우, datasnapshot이 데이터베이스에 있는 덩어리 데이터임
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    binding.titleArea.setText(dataModel?.title)
                    binding.contentArea.setText(dataModel?.content)
                    writerUid = dataModel!!.uid

                }catch (e: Exception){
                    Log.d("BoardInsideActivity","삭제완료")

                }


            }//error 떴을 경우 출력값 지정
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        //내가 지정한 myRef에 postListener를 연결하여 원하는 데이터베이스의 값 읽기
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
    //FB storage에서 이미지 가져오기
    private fun getImageData(key:String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key+".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{

            }
        })



    }
}
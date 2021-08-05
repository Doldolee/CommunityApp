package com.example.mysololife.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mysololife.R
import com.example.mysololife.contentsList.BookmarkModel
import com.example.mysololife.databinding.ActivityBoardWriteBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_board_write)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {
            val title =binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            //firebase storage에 이미지를 저장하고 싶다.
            //만약네 내가 게시글을 클릭했을 때 게시글에 대한 정보를 받아와야하는데
            //다른 title, content, time과 같이 게시글에 해당하는 key값으로 이미지 이름을 정해주기 위해서
            //key값을 미리생성하고 데이터베이스에 저장하는데 저장하기전에 key값을 가져옴
            val key = FBRef.boardRef.push().key.toString()

            //board
            //    -key
            //         -boardModel(title, content, uid, time)
            FBRef.boardRef
                .child(key)//key값을 미리 가져왔으므로 push 대신 child로 넣어줌
//                .push()//랜덤한 값 지정
                .setValue(BoardModel(title,content,uid,time))

            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()

            imageUpload(key)

            //Activity 종료시킴킴
           finish()
        }

        //갤러리로 이미지 업로드드
       binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)

        }


    }

    private fun imageUpload(key:String){
        val storage =  Firebase.storage
        val storageRef = storage.reference
        // Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child(key+".png")

        // Get the data from an ImageView as bytes
        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
    //갤러리 열기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode ==100){
            binding.imageArea.setImageURI(data?.data)

        }
    }
}
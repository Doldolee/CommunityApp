package com.example.mysololife.contentsList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef

class BookmarkRVAdapter(val context: Context, val items: ArrayList<ContentModel>, val keyList:ArrayList<String>, val bookmarkIdList:MutableList<String>)
    :RecyclerView.Adapter<BookmarkRVAdapter.Viewholder>() {

//    interface ItemClick{
//        fun onClick(view: View, position: Int)
//    }
//    var itemClick : ItemClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRVAdapter.Viewholder {
//    content_rv_item에 만들어놓은 item을 가져와서 ViewHolder class로 넘겨줌
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        return Viewholder(v)
    }
    //    listActivity에서 adapter로 넘어온 items와 keyList를 bindItems 함수로 하나하나 넣어서 연결해줌
    override fun onBindViewHolder(holder: BookmarkRVAdapter.Viewholder, position: Int) {
        holder.bindItems(items[position], keyList[position])


//        if(itemClick !=null){
//            holder.itemView.setOnClickListener {v->
//                itemClick?.onClick(v,position)
//            }
//        }
    }
    //전체 아이템 갯수 반환
    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        //        onBindViewHolder에서 넣어준 items[position] 와 keyList[position]이 item, key값으로 받아짐
        fun bindItems(item: ContentModel, key: String){

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookMarkArea = itemView.findViewById<ImageView>(R.id.bookMarkArea)

            if(bookmarkIdList.contains(key)){
                bookMarkArea.setImageResource(R.drawable.bookmark_color)
            }else{
                bookMarkArea.setImageResource(R.drawable.bookmark_white)

            }


            itemView.setOnClickListener {
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url",item.webUrl)
                itemView.context.startActivity(intent)
            }

            contentTitle.text = item.title
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)

        }
    }

}
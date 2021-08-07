package com.example.mysololife.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth

class BoardListLVAdapter(val boardList : MutableList<BoardModel>):BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var converView = convertView
        //자기가 쓴글의 백그라운드를 색칠해주기 위해서 view를 재활용하면서 이슈가 발생하므로 주석처리함
//        if(converView == null){
        converView = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)
//        }
        val itemLinearLayoutView = converView?.findViewById<LinearLayout>(R.id.itemView)

        val title = converView?.findViewById<TextView>(R.id.titleArea)
        val content = converView?.findViewById<TextView>(R.id.contentArea)
        val time = converView?.findViewById<TextView>(R.id.timeArea)

        if(boardList[position].uid.equals(FBAuth.getUid())){
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
        }
        content!!.text = boardList[position].content
        time!!.text = boardList[position].time
        title!!.text = boardList[position].title

        return converView!!
    }
}
package com.example.mysololife.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.mysololife.R
import com.example.mysololife.board.BoardInsideActivity
import com.example.mysololife.board.BoardListLVAdapter
import com.example.mysololife.board.BoardModel
import com.example.mysololife.board.BoardWriteActivity
import com.example.mysololife.contentsList.ContentModel
import com.example.mysololife.databinding.FragmentTalkBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding

    private val boardKeyList = mutableListOf<String>()
    private lateinit var boardRVAdapter : BoardListLVAdapter

    private val boardDataList = mutableListOf<BoardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

//        val boardList = mutableListOf<BoardModel>()
//        boardList.add(BoardModel("a","b","c","d"))



        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        //?????????????????? ???????????? BardInside ??????????????? ???????????????
        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            // ?????? ??? ??????????????? listview??? ?????? ????????? title content time ??? ?????? ??????????????? ??????????????? ?????????
//            val intent = Intent(context, BoardInsideActivity::class.java)
//            intent.putExtra("title", boardDataList[position].title)
//            intent.putExtra("content", boardDataList[position].content)
//            intent.putExtra("time", boardDataList[position].time)
//            startActivity(intent)

            // ?????? ??? ??????????????? firebase??? ?????? board??? ?????? ???????????? id??? ???????????? ?????? ???????????? ???????????? ??????
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)


        }





        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }
        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }
        binding.homeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }
        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }
        getFBBoardData()
        return binding.root
    }

    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            //????????? ????????? ??????, datasnapshot??? ????????????????????? ?????? ????????? ????????????
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()

                for (dataModel in dataSnapshot.children){

                    boardKeyList.add(dataModel.key.toString())
                    var item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)

                }
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged()




            }//error ?????? ?????? ????????? ??????
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        //?????? ????????? myRef??? postListener??? ???????????? ????????? ????????????????????? ??? ??????
        FBRef.boardRef.addValueEventListener(postListener)


    }


}
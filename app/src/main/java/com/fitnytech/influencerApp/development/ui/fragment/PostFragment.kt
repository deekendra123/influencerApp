package com.fitnytech.influencerApp.development.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.fitnytech.influencerApp.development.adapter.PostListAdapter
import com.fitnytech.influencerApp.development.databinding.FragmentPostBinding
import com.fitnytech.influencerApp.development.model.MediaFile
import com.fitnytech.influencerApp.development.model.MediaList
import com.fitnytech.influencerApp.development.model.PostData
import com.fitnytech.influencerApp.development.preference.SavedPreference
import com.fitnytech.influencerApp.development.utils.CustomProgressDialog
import com.fitnytech.influencerApp.development.utils.Utils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class PostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPostBinding
    private var mAdapter: PostListAdapter? = null
    private var postDataList:ArrayList<PostData>?=null
    private var mediaDataList:ArrayList<MediaFile>?=null
    private var list:ArrayList<MediaList>?=null
    private var customProgressDialog:CustomProgressDialog? = null
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    private var influcerCode:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root;

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        mediaDataList!!.clear()
        postDataList!!.clear()
        getPosts()
    }
    private fun init(){


        customProgressDialog = CustomProgressDialog()
        postDataList = ArrayList()
        mediaDataList  = ArrayList()
        list = ArrayList()
        influcerCode = activity?.let { SavedPreference.getCode(it) }
        mAdapter = activity?.let { PostListAdapter(it, postDataList!!, list!!) }
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewPost.setHasFixedSize(true)
        binding.recyclerViewPost.addItemDecoration(DividerItemDecoration(binding.recyclerViewPost.context, DividerItemDecoration.VERTICAL)
        )
        binding.recyclerViewPost.adapter = mAdapter
        binding.tvUserName.text = activity?.let { SavedPreference.getUsername(it) }
    }



    private fun getPosts(){

        activity?.let { customProgressDialog!!.show(it, "Please Wait...") }


        var postRef =
                FirebaseDatabase.getInstance().reference.child("influencer1").child("posts").child(influcerCode.toString())

        postRef.keepSynced(true)

        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val postData = snapshot.getValue(PostData::class.java)
                        postDataList!!.add(postData!!)

                         postRef = FirebaseDatabase.getInstance().reference.child("influencer1")
                            .child("posts").child(influcerCode.toString()).child(snapshot.key!!).child("mediaFile")


                        postRef!!.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    val mediaFile = snapshot.getValue(MediaFile::class.java)
                                    mediaDataList!!.add(mediaFile!!)
                                }
                                val mediaList = MediaList(postDataList!!, mediaDataList!!)
                                list!!.add(mediaList)
                                mediaDataList = ArrayList()
                                mAdapter!!.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")

                                Log.e("messages",error.message)

                            }

                        })

                    }

                    customProgressDialog!!.dialog.dismiss()


                }
                else{
                    customProgressDialog!!.dialog.dismiss()
                binding.noDataFoundImage.visibility = View.VISIBLE
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}
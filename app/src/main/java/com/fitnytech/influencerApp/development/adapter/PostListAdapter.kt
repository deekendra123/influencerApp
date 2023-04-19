package com.fitnytech.influencerApp.development.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitnytech.influencerApp.development.databinding.SingleListItemBinding
import com.fitnytech.influencerApp.development.model.MediaList
import com.fitnytech.influencerApp.development.model.PostData


class PostListAdapter(private val context: Context, private val postDataList: ArrayList<PostData>, private val mediaDataList: ArrayList<MediaList>) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    private var postPagerAdapter:PostPagerAdapter?= null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postData = postDataList!![position]


        postPagerAdapter = PostPagerAdapter(context, null, mediaDataList[position].media)

        with(holder){
            with(postData){
                binding.tvCaption.text = this.caption
                binding.viewPager.adapter = postPagerAdapter
                binding.indicator.setViewPager(binding.viewPager)
            }
        }
    }

    override fun getItemCount(): Int {
        return postDataList!!.size
    }

    inner class ViewHolder(val binding: SingleListItemBinding) : RecyclerView.ViewHolder(binding.root)

}
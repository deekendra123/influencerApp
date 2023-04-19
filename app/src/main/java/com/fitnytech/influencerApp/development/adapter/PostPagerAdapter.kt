package com.fitnytech.influencerApp.development.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.fitnytech.influencerApp.development.R
import com.fitnytech.influencerApp.development.model.MediaFile
import java.util.Objects

class PostPagerAdapter(private val context: Context, private val imageList: List<Uri>?, private val medialFile: List<MediaFile>?) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList?.size ?: medialFile!!.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }
    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.post_image_list_layout, container, false)
        val imageView: ImageView = itemView.findViewById(R.id.postImage)
        val videoPlayer: VideoView = itemView.findViewById(R.id.videoPlayer)

        if(imageList==null){
            if (medialFile!![position].mimeType.toString() == "image"||medialFile!![position].mimeType.toString() == "png"||
                medialFile!![position].mimeType.toString() == "jpg"||medialFile!![position].mimeType.toString() == "jpeg"){
                videoPlayer.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                Glide.with(context).load(medialFile!![position].url).into(imageView);

            }
            else{
                videoPlayer.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                val uri = Uri.parse(medialFile!![position].url)
                videoPlayer.setVideoURI(uri);
                videoPlayer.start();
            }
        }
        else{
            if (imageList[position].toString().contains("image") ||
                imageList[position].toString().contains("png") ||
                imageList[position].toString().contains("jpg") ||
                imageList[position].toString().contains("jpeg")){
                videoPlayer.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                imageView.setImageURI(imageList[position])
            }
            else{
                videoPlayer.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                videoPlayer.setVideoURI(imageList[position]);
                videoPlayer.start();
            }

        }


        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
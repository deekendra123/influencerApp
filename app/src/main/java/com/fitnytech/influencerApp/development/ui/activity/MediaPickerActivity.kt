package com.fitnytech.influencerApp.development.ui.activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitnytech.influencerApp.development.R
import com.fitnytech.influencerApp.development.databinding.ActivityMediaPickerBinding
import com.mahdiasd.filepicker.FileModel
import com.mahdiasd.filepicker.FilePicker
import com.mahdiasd.filepicker.FilePickerListener
import com.mahdiasd.filepicker.PickerMode

class MediaPickerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaPickerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_picker)
        binding = ActivityMediaPickerBinding.inflate( layoutInflater)
        test(null)


    }


    fun btn(view: View) {
        val modes =
            mutableListOf(PickerMode.File, PickerMode.Image, PickerMode.Video, PickerMode.Camera)


        FilePicker(this, supportFragmentManager)
            .setMode(*modes.toTypedArray())
            .setDefaultMode(PickerMode.Image)
            .setMaxSelection(5)
            .setCardBackgroundColor(getColor(R.color.white))
            .setCustomText(
                videoText = binding.videoTitle.text.toString(),
                fileManagerText = binding.storage.text.toString(),
                imageText = binding.imageTitle.text.toString(),
                openStorageText = binding.openStorageTitle.text.toString()
            )
            .setShowFileWhenClick(binding.showWhenClick.isChecked)
            .setDeActiveColor(getColor(R.color.deactiveColor))
            .setActiveColor(getColor(R.color.colorPrimary))
            .setListener(object : FilePickerListener {
                override fun selectedFiles(list: List<FileModel>?) {
                }
            })
            .show()

    }


    fun test(view: View?) {
    }


}
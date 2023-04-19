package com.fitnytech.influencerApp.development.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import androidx.appcompat.app.AppCompatActivity
import com.fitnytech.influencerApp.development.R
import com.fitnytech.influencerApp.development.adapter.PostPagerAdapter
import com.fitnytech.influencerApp.development.databinding.ActivityPostBinding
import com.fitnytech.influencerApp.development.model.MediaFile
import com.fitnytech.influencerApp.development.model.Post
import com.fitnytech.influencerApp.development.preference.SavedPreference
import com.fitnytech.influencerApp.development.utils.CustomProgressDialog
import com.fitnytech.influencerApp.development.utils.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.mahdiasd.filepicker.FileModel
import com.mahdiasd.filepicker.FilePicker
import com.mahdiasd.filepicker.FilePickerListener
import com.mahdiasd.filepicker.PickerMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class PostActivity : AppCompatActivity() {

    private var fileNameList: ArrayList<String>? = null
    private var fileDoneList: ArrayList<String>? = null

    private var mStorageReference: StorageReference? = null
    private lateinit var database: DatabaseReference
    lateinit var viewPagerAdapter: PostPagerAdapter
    lateinit var imageList: ArrayList<Uri>

    private lateinit var binding: ActivityPostBinding
    private var postUid: String? = null
    private var customProgressDialog: CustomProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customProgressDialog = CustomProgressDialog()
        fileNameList = ArrayList()
        fileDoneList = ArrayList()

        imageList = ArrayList<Uri>()
        viewPagerAdapter = PostPagerAdapter(this, imageList, null)

        binding.viewPager.adapter = viewPagerAdapter
        mStorageReference = FirebaseStorage.getInstance().reference

        database = Firebase.database.reference

        binding.backFromPost.setOnClickListener(View.OnClickListener {
            onBackPressed()
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            );
        })

        binding.btnUpload.setOnClickListener(View.OnClickListener {


            val modes =
                mutableListOf(PickerMode.File, PickerMode.Image, PickerMode.Video, PickerMode.Camera)


            FilePicker(this, supportFragmentManager)
                .setMode(*modes.toTypedArray())
                .setDefaultMode(PickerMode.Image)
                .setMaxSelection(7)
                //.setShowFileWhenClick(true)
                .setCardBackgroundColor(getColor(R.color.white))
                .setCustomText(
                    videoText = "Video",
                    fileManagerText = "Storage",
                    imageText = "Image",
                    openStorageText = "Storage"
                )
               // .setShowFileWhenClick(binding.showWhenClick.isChecked)
                .setDeActiveColor(getColor(R.color.deactiveColor))
                .setActiveColor(getColor(R.color.colorPrimary))
                .setListener(object : FilePickerListener {
                    override fun selectedFiles(list: List<FileModel>?) {


                        if (list?.size!! >0){
                            binding.viewPager.visibility = View.VISIBLE
                            binding.indicator.visibility = View.VISIBLE
                            binding.etCaption.visibility = View.VISIBLE

                            binding.btnUpload.text = "Add More"
                            postUid = UUID.randomUUID().toString()

                            for (i in 0 until list?.size!!) {
                                val fileUri = Uri.fromFile(list[i].file)


                                val fileName: String = getFileName(fileUri)
                                imageList.add(fileUri)
                                fileNameList!!.add(fileName)
                                fileDoneList!!.add("Uploading")
                                viewPagerAdapter.notifyDataSetChanged()

                            }
                            binding.indicator.setViewPager(binding.viewPager)


                        }
                    }
                })
                .show()

        })


        binding.postNow.setOnClickListener(View.OnClickListener {

            customProgressDialog!!.show(this, "Please Wait...")

            uploadPost()
        })
    }

    private fun uploadPost() {
        var fileToUpload: StorageReference? = null
        var mediaFile: MediaFile? = null
        for (i in 0 until imageList.size) {

            fileToUpload = if (imageList!![i].toString().contains("image") || imageList!![i].toString().contains("png") ||
                imageList!![i].toString().contains("jpg") || imageList!![i].toString().contains("jpeg")) {
                mStorageReference!!.child("influencerImage/" + SavedPreference.getCode(this)).child(
                    fileNameList!![i]
                )

            } else {
                mStorageReference!!.child("influencerVideo/" + SavedPreference.getCode(this)).child(
                    fileNameList!![i]
                )

            }

            fileToUpload.putFile(imageList[i])
                .addOnSuccessListener { //Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    fileDoneList!!.removeAt(i)
                    fileDoneList!!.add(i, "Done")
                    val mediaUid = UUID.randomUUID().toString()
                    val result = it.metadata!!.reference!!.downloadUrl
                    result.addOnSuccessListener {
                        val imageLink = it.toString()
                        mediaFile = if (imageLink.contains("influencerImage")) {
                            MediaFile(imageLink, "image")

                        } else {
                            MediaFile(imageLink, "video")

                        }
                        SavedPreference.getCode(this)
                            ?.let { it1 ->
                                database.child("influencer1").child("posts").child(it1)
                                    .child(postUid.toString()).child("mediaFile")
                                    .child(mediaUid.toString()).setValue(mediaFile)
                            }

                        if (imageList.size==i+1){
                            customProgressDialog!!.dialog.dismiss()
                            val i = Intent(this, HomeActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                            overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            )
                        }
                    }
                }
            val postDate: String =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val postTime: String =
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val post: Post =
                Post(binding.etCaption.text.toString(), postDate, postTime, "free", "yes")
            SavedPreference.getCode(this)
                ?.let { it1 ->

                    database.child("influencer1").child("posts").child(it1)
                        .child(postUid.toString())
                        .setValue(post)
                }

        }
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }



    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        );
    }
}
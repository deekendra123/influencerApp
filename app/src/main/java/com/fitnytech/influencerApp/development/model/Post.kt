package com.fitnytech.influencerApp.development.model



data class Post(val caption: String? = null,val postDate: String? = null, val postTime: String? = null,val postType: String? = null, val loginRequired: String? = null)

data class MediaFile(val url: String? = null, val mimeType: String? = null)

data class PostData(val caption: String? = null, val postDate: String? = null, val postTime: String? = null,val postType: String? = null, val loginRequired: String? = null)

data class MediaList(val post:ArrayList<PostData>?=null, val media:ArrayList<MediaFile>?=null)
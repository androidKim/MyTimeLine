package com.midas.mytimeline.structure.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by taejun on 2018. 3. 26..
 * timeline model
 */
class timeline(val seq:String, val title: String, val content:String, val img_path:String, val date:String, val color_code:String) : Parcelable
{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(seq)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(img_path)
        parcel.writeString(date)
        parcel.writeString(color_code)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<timeline>
    {
        override fun createFromParcel(parcel: Parcel): timeline
        {
            return timeline(parcel)
        }

        override fun newArray(size: Int): Array<timeline?>
        {
            return arrayOfNulls(size)
        }
    }

    //------------------------------------------------------------
    //
    fun loadImage(path:String): Bitmap?
    {
        if(path == null || path.isEmpty())
            return null

        val pBitmap: Bitmap = BitmapFactory.decodeFile(path);
        return pBitmap
    }
}

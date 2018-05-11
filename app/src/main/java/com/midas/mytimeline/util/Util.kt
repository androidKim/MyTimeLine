package com.midas.mytimeline.util

import android.content.Context
import com.midas.mytimeline.R
import java.text.SimpleDateFormat
import java.util.*



/**
 * Created by taejun on 2018. 4. 10..
 */
object Util
{
    //----------------------------------------------------------------------------------------------------
    //
    fun GetCurrentDateTime(): Long
    {
        return System.currentTimeMillis()
    }
    //----------------------------------------------------------------------------------------------------
    //
    fun GetCurrentDateTime(strFormat: String?): String
    {
        var strFormat = strFormat
        if (strFormat == null || strFormat.length <= 0)
            strFormat = "yyyy-MM-dd HH:mm:ss"

        val pDateFormat = SimpleDateFormat(strFormat)
        val pDate = Date()
        return pDateFormat.format(pDate)
    }

    //----------------------------------------------------------------------------------------------------
    //timestamp convert => String
    fun DateTimeFormat(nDateTime: Long, strFormat: String?): String
    {
        var strFormat = strFormat
        if (nDateTime <= 0)
            return ""

        if (strFormat == null || strFormat.length <= 0)
            strFormat = "yyyy-MM-dd HH:mm:ss"

        val pDateFormat = SimpleDateFormat(strFormat)
        val pDate = Date(nDateTime)
        return pDateFormat.format(pDate)
    }
    //----------------------------------------------------------------------------------------------------
    //
    fun GetRandomColor(pContext: Context):String
    {
        var arrColor = pContext.resources.getStringArray(R.array.color_array)
        var nRandom:Int = getRandomNumber(0, arrColor.size)
        var strColor = arrColor[nRandom]
        return strColor;
    }
    //----------------------------------------------------------------------------------------------------
    //
    fun getRandomNumber(from: Int, to: Int) : Int
    {
        val random = Random();
        return random.nextInt(to - from) + from
    }
}
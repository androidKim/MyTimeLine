package com.midas.mytimeline.structure.core

import android.provider.BaseColumns

/**
 * Created by taejun on 2018. 3. 27..
 */
object DBContract
{
    class timelineEntry:BaseColumns
    {
        companion object
        {
            val tb_name = "tb_timeline";
            val seq = "seq";
            val title = "title";
            val content = "content";
            val img_path = "img_path";
            val date = "date";
            val color_code = "color_code";
        }
    }
}
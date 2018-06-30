package com.midas.mytimeline.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.midas.mytimeline.R
import com.midas.mytimeline.structure.core.timeline

class MainRvAdapter(val context: Context, var timelineList: ArrayList<timeline>) :
        RecyclerView.Adapter<MainRvAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_main, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return timelineList.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int)
    {
        holder?.bind(timelineList[position], context)
    }

    //-----------------------------------------------------------
    //
    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    {
        val tv_Date = itemView?.findViewById<TextView>(R.id.tv_Date)
        val tv_Title = itemView?.findViewById<TextView>(R.id.tv_Title)
        val tv_Content = itemView?.findViewById<TextView>(R.id.tv_Content)
        val iv_Attatch = itemView?.findViewById<ImageView>(R.id.iv_Attach)

        fun bind (pInfo: timeline, pContext: Context)
        {
            tv_Date?.text = pInfo.date
            tv_Title?.text = pInfo.title
            tv_Content?.text = pInfo.content
        }
    }

    //-----------------------------------------------------------
    //
    fun refreshData(pArray:ArrayList<timeline>)
    {
        this.timelineList = pArray;
        notifyDataSetChanged();
    }
}
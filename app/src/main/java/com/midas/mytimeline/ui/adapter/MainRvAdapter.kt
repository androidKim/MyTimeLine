package com.midas.mytimeline.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.midas.mytimeline.R
import com.midas.mytimeline.common.Constant
import com.midas.mytimeline.structure.core.timeline
import com.midas.mytimeline.ui.act.ActDetail

class MainRvAdapter(val context: Context, var timelineList: ArrayList<timeline>) :
        RecyclerView.Adapter<MainRvAdapter.Holder>()
{

    /*********************** System Function ***********************/
    //-----------------------------------------------------------
    //
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.row_main, parent, false)
        return Holder(view)
    }

    //-----------------------------------------------------------
    //
    override fun getItemCount(): Int
    {
        return timelineList.size
    }

    //-----------------------------------------------------------
    //
    override fun onBindViewHolder(holder: Holder?, position: Int)
    {
        holder?.bind(timelineList[position], context)
    }

    //-----------------------------------------------------------
    //
    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    {
        val ly_Row = itemView?.findViewById<RelativeLayout>(R.id.ly_Row)
        val tv_Date = itemView?.findViewById<TextView>(R.id.tv_Date)
        val tv_Title = itemView?.findViewById<TextView>(R.id.tv_Title)
        val iv_Attatch = itemView?.findViewById<ImageView>(R.id.iv_Attach)

        fun bind (pInfo: timeline, pContext: Context)
        {
            if(pInfo.date != null)
                tv_Date?.text = pInfo.date

            if(pInfo.title != null)
                tv_Title?.text = pInfo  .title

            //setImage..
            iv_Attatch?.setImageDrawable(null)
            if(pInfo.img_path != null)
            {
                if(!pInfo.img_path.isEmpty())
                {
                    iv_Attatch?.setImageBitmap(pInfo.loadImage(pInfo.img_path))
                    iv_Attatch?.setImageAlpha(50);
                }
            }
            else
            {
                if(pInfo.color_code != null)
                {
                    if(!pInfo.color_code.isEmpty())
                    {
                        var pColor:Int = Color.parseColor(String.format("#50%s", pInfo.color_code));
                        iv_Attatch?.setBackgroundColor(pColor)
                    }
                }
            }


            ly_Row?.setTag(pInfo)
            ly_Row?.setOnClickListener(onClickGoDetail)
        }
    }

    /*********************** User Function ***********************/

    //-----------------------------------------------------------
    //
    fun refreshData(pArray:ArrayList<timeline>)
    {
        this.timelineList = pArray;
        notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------
    //
    fun goDetail(view:View)
    {
        val pInfo:timeline = view.getTag() as timeline;
        val pIntent = Intent(view.context, ActDetail::class.java)
        pIntent.putExtra(Constant.INTENT_DATA_TIMELINE_OBJ, pInfo);
        view.context.startActivity(pIntent);
    }
    /*********************** Listener ***********************/
    //----------------------------------------------------------------------------
    //
    val onClickGoDetail = View.OnClickListener { view ->
        when(view.getId())
        {
            R.id.ly_Row -> goDetail(view);
        }
    }
}
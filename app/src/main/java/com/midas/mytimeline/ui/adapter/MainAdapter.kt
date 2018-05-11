package com.midas.mytimeline.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.midas.mytimeline.R
import com.midas.mytimeline.common.Constant
import com.midas.mytimeline.structure.core.timeline
import com.midas.mytimeline.ui.act.ActDetail
import com.midas.mytimeline.util.Util

/**
 * Created by taejun on 2018. 3. 26..
 * mainlistview adapter..
 */
class MainAdapter(val pContext: Context, var arrData:ArrayList<timeline>, var pRequestManager:RequestManager):BaseAdapter()
{
    //----------------------------------------------------------------------------
    //
    override fun getItem(position: Int): Any
    {
        return arrData[position]
    }

    //----------------------------------------------------------------------------
    //
    override fun getItemId(position: Int): Long
    {
        return 0
    }

    //----------------------------------------------------------------------------
    //
    override fun getCount(): Int
    {
        return arrData.size;
    }

    //----------------------------------------------------------------------------
    //getView...
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
    {
        var pView:View ?= convertView
        var holder:ViewHolder
        var pInfo:timeline = arrData.get(position)

        if(pInfo == null)
            pView

        if(pView == null)
        {
            pView = LayoutInflater.from(pContext).inflate(R.layout.row_main, null)
            holder = ViewHolder()
            holder.ly_Row = pView.findViewById(R.id.ly_Row)
            holder.ly_DynamicFileArea = pView.findViewById(R.id.ly_DynamicFileArea)
            holder.tv_Date = pView.findViewById(R.id.tv_Date);
            holder.tv_Title = pView.findViewById(R.id.tv_Title)
            holder.tv_Content = pView.findViewById(R.id.tv_Content)
            holder.iv_Attach = pView.findViewById(R.id.iv_Attach)
            pView.tag = holder
        }
        else
        {
            holder = pView?.tag as ViewHolder
            holder.init()
        }

        settingRowView(pView!!, pInfo, holder, position);
        return pView
    }

    //----------------------------------------------------------------------------
    //
    fun refreshData(pArray:ArrayList<timeline>)
    {
        this.arrData = pArray;
        notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------
    //
    fun settingRowView(pView:View, pInfo:timeline, holder:ViewHolder, position:Int)
    {
        if(pView == null || pInfo == null)
            return;

        if(pInfo.date != null)
        {
            var nDate:Long = pInfo.date.toLong();
            var strDate:String = Util.DateTimeFormat(nDate, null);

            holder.tv_Date?.text = strDate;
        }


        if(pInfo.title != null)
            holder.tv_Title?.text = pInfo.title

        if(pInfo.content != null)
            holder.tv_Content?.text = pInfo.content

        if(pInfo.img_path != null)
        {
            if(pInfo.img_path.equals("null") || pInfo.img_path.equals(""))
            {
                holder.ly_DynamicFileArea?.visibility = View.GONE
            }
            else
            {
                holder.ly_DynamicFileArea?.visibility = View.VISIBLE
                holder.iv_Attach?.setImageBitmap(pInfo.loadImage(pInfo?.img_path))

                //Glide.with(pContext).load(pInfo.loadImage(pInfo?.img_path)).into(holder.iv_Attach);
                //pRequestManager.load
            }

        }
        else
        {
            holder.ly_DynamicFileArea?.visibility = View.GONE
        }

        //
        if(pInfo.color_code != null)
        {
            if(!pInfo.color_code.equals(""))
            {
                var pTextColor:Int = 0;
                if(pInfo.color_code.equals("FFFFFF"))//background white
                    pTextColor = Color.parseColor("#000000");//textcolor black
                else
                    pTextColor = Color.parseColor("#FFFFFF");//textcolor white

                holder.tv_Date?.setTextColor(pTextColor)
                holder.tv_Title?.setTextColor(pTextColor)
                holder.tv_Content?.setTextColor(pTextColor)

                var pColor:Int = Color.parseColor(String.format("#%s", pInfo.color_code));
                holder.ly_Row?.setBackgroundColor(pColor)
            }

        }

        holder.ly_Row?.setTag(pInfo)
        holder.ly_Row?.setOnClickListener(onClickGoDetail)
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
    /************************  Listener ************************/
    //----------------------------------------------------------------------------
    //
    val onClickGoDetail = View.OnClickListener { view ->
        when(view.getId())
        {
            R.id.ly_Row -> goDetail(view);
        }
    }
    /************************  Inner Class ************************/
    //----------------------------------------------------------------------------
    //viewholder
    public class ViewHolder
    {
        var ly_Row:LinearLayout?=null
        var tv_Date:TextView?=null
        var tv_Title:TextView?= null
        var tv_Content:TextView?=null
        var ly_DynamicFileArea:LinearLayout?=null
        var iv_Attach:ImageView?=null

        fun init()
        {
            tv_Date?.setText(null)
            tv_Title?.setText(null)
            tv_Content?.setText(null)
            iv_Attach?.setImageDrawable(null)
        }
    }
}
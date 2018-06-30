package com.midas.mytimeline.ui.act

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midas.mytimeline.MyApp
import com.midas.mytimeline.R
import com.midas.mytimeline.common.Constant
import com.midas.mytimeline.structure.core.timeline
import com.midas.mytimeline.ui.adapter.MainRvAdapter
import kotlinx.android.synthetic.main.act_main.*
import java.util.*

/*
main
 */
class ActMain : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener
{
    /************************* Define *************************/

    /************************* Member *************************/
    var m_App:MyApp? = null;
    var m_Context:Context? = null;
    var m_RequestManager: RequestManager? = null;
    var m_Adapter:MainRvAdapter? = null;
    var m_arrData:ArrayList<timeline>? = null;
    /************************* Controller *************************/
    //
    /************************* System Fucntion *************************/
    //-------------------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main);
        this.m_App = MyApp();
        this.m_Context = this;
        this.m_RequestManager = Glide.with(this.m_Context as ActMain);

        if(!m_App!!.m_bInit)
            m_App!!.init(m_Context!!);

        initValue();
        recvIntentData();
        settingView();
    }
    //-------------------------------------------------------------
    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Constant.ACTIVITY_FOR_RESULT_INSERT_COMPLETE)
        {
            //refresh..
            setRefresh();
        }
        else
        {

        }
    }
    /************************* User Fucntion *************************/
    //-------------------------------------------------------------
    //
    private fun initValue()
    {

    }
    //-------------------------------------------------------------
    //
    private fun recvIntentData()
    {

    }
    //-------------------------------------------------------------
    //
    private fun settingView()
    {
        var arrData: ArrayList<timeline> = ArrayList();
        arrData = m_App!!.m_LocalDbCtrl.getAllData();
        m_Adapter = MainRvAdapter(this, arrData)
        recyclerView.adapter = m_Adapter;

        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(true)

        //event..
        ly_SwipeRefresh.setOnRefreshListener(this)
        ly_Bottom.setOnClickListener(onClickGoWrite);
    }
    //-------------------------------------------------------------
    //
    private fun setRefresh()
    {
        //UIThread..
        this@ActMain.runOnUiThread(java.lang.Runnable
        {
            var arrData: ArrayList<timeline> = ArrayList();
            arrData = m_App!!.m_LocalDbCtrl.getAllData();

            this.m_Adapter!!.refreshData(arrData);

            ly_SwipeRefresh.setRefreshing(false);
        })
    }
    //-------------------------------------------------------------
    //get List From Db


    //-------------------------------------------------------------
    //
    fun goWrite()
    {
        val pIntent = Intent(this, ActWrite::class.java)
        startActivityForResult(pIntent, 0);
    }
    /************************* Listener *************************/
    //-------------------------------------------------------------
    //
    override fun onRefresh()
    {
        //refresh..
        setRefresh();
    }

    //-------------------------------------------------------------
    //
    val onClickGoWrite = View.OnClickListener { view ->
        when(view.getId())
        {
            R.id.ly_Bottom -> goWrite()
        }
    }

}

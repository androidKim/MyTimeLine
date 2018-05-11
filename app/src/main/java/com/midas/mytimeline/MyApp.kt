package com.midas.mytimeline

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import com.midas.mytimeline.core.DBHelper
import com.midas.mytimeline.ui.act.ActMain



/**
 * Created by taejun on 2018. 3. 23..
 * ApplicationClass..
 */
class MyApp : Application()
{
    /************************ Member ************************/
    lateinit var m_LocalDbCtrl: DBHelper;

    var m_bInit:Boolean = false;

    /************************ System Function  ************************/
    //--------------------------------------------------------------
    //
    override fun onCreate()
    {
        super.onCreate()
    }
    /************************ User Function  ************************/
    //--------------------------------------------------------------
    //
    public fun init(pContext:Context)
    {
        if(m_bInit == false)
        {
            m_LocalDbCtrl = DBHelper(pContext);
            m_bInit = true;
        }
    }
    //--------------------------------------------------------------
    //
    public fun refreshMain(pContext:Context)
    {
        val pIntent = Intent(pContext, ActMain::class.java)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        (pContext as Activity).startActivity(pIntent)
        (pContext as Activity).finish()
    }

    //--------------------------------------------------------------
    //
    companion object
    {

    }
}
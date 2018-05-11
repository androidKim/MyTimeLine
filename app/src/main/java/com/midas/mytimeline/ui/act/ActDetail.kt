package com.midas.mytimeline.ui.act

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.midas.mytimeline.MyApp
import com.midas.mytimeline.R
import com.midas.mytimeline.common.Constant
import com.midas.mytimeline.structure.core.timeline
import kotlinx.android.synthetic.main.act_detail.*



/*
time line detail..
 */
class ActDetail : AppCompatActivity()
{
    /************************* Define *************************/

    /************************* Member *************************/
    var m_App: MyApp = MyApp();
    var m_Context: Context = this;
    var m_Info:timeline?=null;
    /************************* Controller *************************/
    //

    /************************* System Fucntion *************************/
    //-------------------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_detail);

        if(!m_App.m_bInit)
            m_App.init(m_Context);

        initValue();
        recvIntentData();
        settingView();
    }
    //-------------------------------------------------------------
    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Constant.ACTIVITY_FOR_RESULT_MODIFY_COMPLETE)
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
        val pIntent: Intent = intent;

        if(pIntent.hasExtra(Constant.INTENT_DATA_TIMELINE_OBJ))
            m_Info = pIntent.getParcelableExtra<timeline>(Constant.INTENT_DATA_TIMELINE_OBJ);

    }
    //-------------------------------------------------------------
    //
    private fun settingView()
    {
        if(m_Info != null)//exist Contents..
        {
            tv_Title?.text = m_Info?.title;
            tv_Content.text = m_Info?.content;

            //val pFile = File(m_Info?.img_path);
            if(!m_Info?.img_path.equals("") && !m_Info?.img_path.equals("null"))
            {
                val pBitmap: Bitmap = BitmapFactory.decodeFile(m_Info?.img_path);
                if(pBitmap != null)
                {
                    iv_Attach.setImageBitmap(pBitmap);
                }
            }
            ly_BottomModify?.setTag(m_Info)
            ly_BottomDelete?.setTag(m_Info)
            ly_BottomModify.setOnClickListener(onClick);
            ly_BottomDelete.setOnClickListener(onClick);
        }
        else//error page..
        {

        }
    }
    //-------------------------------------------------------------
    //
    private fun goModPage(pView:View)
    {
        //show alert..
        val pInfo:timeline = pView.getTag() as timeline;
        val pIntent = Intent(m_Context, ActModify::class.java)
        pIntent.putExtra(Constant.INTENT_DATA_TIMELINE_OBJ, pInfo);
        startActivity(pIntent);
    }
    //-------------------------------------------------------------
    //
    private fun deleteInfo(pView:View)
    {
        //show dialog..
        val pAlert = AlertDialog.Builder(this@ActDetail).create();
        pAlert.setTitle("Do you want delete?");
        pAlert.setMessage("you can choice!!");
        pAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",{
            dialogInterface, i ->

            val pInfo:timeline = pView.getTag() as timeline;
            m_App.m_LocalDbCtrl.deleteInfo(pInfo.seq);

            //goMain..
            m_App.refreshMain(m_Context);
        })
        pAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
            dialogInterface, i ->
            pAlert.dismiss();
        })
        pAlert.show();
    }
    //-------------------------------------------------------------
    //
    private fun setRefresh()
    {

    }
    /************************* listener *************************/
    //-------------------------------------------------------------
    //onClick
    val onClick = View.OnClickListener { view ->
        when(view.getId())
        {
            R.id.ly_BottomModify -> goModPage(view);
            R.id.ly_BottomDelete -> deleteInfo(view);
        }
    }
}
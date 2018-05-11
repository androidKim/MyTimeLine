package com.midas.mytimeline.ui.act

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.midas.mytimeline.MyApp
import com.midas.mytimeline.R
import com.midas.mytimeline.common.Constant
import com.midas.mytimeline.structure.core.timeline
import kotlinx.android.synthetic.main.act_modify.*

/*
write page..
 */
class ActModify : AppCompatActivity()
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
        setContentView(R.layout.act_modify);

        if(!m_App.m_bInit)
            m_App.init(m_Context);

        initValue();
        recvIntentData();
        settingView();
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
        if(m_Info != null)
        {
            this.edit_Title.setText(m_Info!!.title)
            this.edit_Content.setText(m_Info!!.content)
            this.iBtn_Modify.setOnClickListener(onClick)
        }
    }
    //-------------------------------------------------------------
    //save to local database
    fun modInfo()
    {
        if(checkValidation() == true)
        {
            //show dialog..
            val pAlert = AlertDialog.Builder(this@ActModify).create();
            pAlert.setTitle("Do you want save?");
            pAlert.setMessage("you can choice!!");
            pAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",{
                dialogInterface, i ->
                var seq = m_Info!!.seq;
                var title = this.edit_Title.text.toString();
                var content = this.edit_Content.text.toString();
                var img_path:String = ""
                var date:String = ""
                var color_code:String = ""
                var result = m_App.m_LocalDbCtrl.updateInfo(timeline(seq = seq, title = title, content = content, img_path = img_path, date = date, color_code =  color_code));

                m_App.refreshMain(m_Context);
            })
            pAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
                dialogInterface, i ->
                pAlert.dismiss();
            })
            pAlert.show();
        }
        else
        {
            return;
        }
    }
    //-------------------------------------------------------------
    //
    private fun checkValidation():Boolean
    {
        var bResult:Boolean = false;
        var strTitle:String = edit_Title.text.toString();
        var strContent:String = edit_Content.text.toString();

        if(strTitle.equals(""))
        {
            bResult = false;
            Toast.makeText(m_Context, "please input title...", Toast.LENGTH_SHORT).show();
        }
        else if(strContent.equals(""))
        {
            bResult = false;
            Toast.makeText(m_Context, "please input content...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            bResult = true;
        }

        return bResult;
    }

    /************************* Listener *************************/
    //-------------------------------------------------------------
    //onClick
    val onClick = View.OnClickListener { view ->
        when(view.getId())
        {
            R.id.iBtn_Modify -> modInfo();
        }
    }
}
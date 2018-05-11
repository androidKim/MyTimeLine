package com.midas.mytimeline.ui.act

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.midas.mytimeline.MyApp
import com.midas.mytimeline.R
import com.midas.mytimeline.common.Constant
import com.midas.mytimeline.structure.core.timeline
import com.midas.mytimeline.util.Util
import kotlinx.android.synthetic.main.act_write.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/*
write page..
 */
class ActWrite : AppCompatActivity()
{
    /************************* Define *************************/
    private val REQUEST_TAKE_PHOTO = 1001
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1002

    /************************* Member *************************/
    private var m_App:MyApp = MyApp();
    private var m_Context:Context = this;
    private var m_strImgpath:String ?= null;
    /************************* Controller *************************/
    //
    /************************* System Fucntion *************************/
    //-------------------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_write);

        if(!m_App.m_bInit)
            m_App.init(m_Context);

        initValue();
        recvIntentData();
        settingView();
    }
    //---------------------------------------------------------------------------------------------------
    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)//Intent?  <-- null이 올수도있다
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM)//select gallery
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    m_strImgpath = saveImage(bitmap)
                    Toast.makeText(this@ActWrite, "Image Saved!", Toast.LENGTH_SHORT).show()
                    iv_Attach!!.setImageBitmap(bitmap)

                }
                catch (e: IOException)
                {
                    e.printStackTrace()
                    Toast.makeText(this@ActWrite, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == REQUEST_TAKE_PHOTO)//take photo
        {
            if (data != null)
            {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                iv_Attach!!.setImageBitmap(thumbnail)
                m_strImgpath = saveImage(thumbnail)
                Toast.makeText(this@ActWrite, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //---------------------------------------------------------------------------------------------------
    //
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        when (requestCode)
        {
            REQUEST_SELECT_IMAGE_IN_ALBUM ->
            {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {

                }
                else//허용완료
                {
                    selectImageInAlbum()
                }
            }
            REQUEST_TAKE_PHOTO ->
            {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {

                }
                else//허용완료
                {
                    takePhoto()
                }
            }
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
        ly_BottomPhoto.setOnClickListener(onClick);
        ly_BottomSend.setOnClickListener(onClick);
    }
    //-------------------------------------------------------------
    //
    private fun checkPermissionWriteStorage()
    {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            permissionWriteStorege()
        }
        else
        {
            selectImageInAlbum()
        }
    }
    //-------------------------------------------------------------
    //
    private fun checkPermissionCamera()
    {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            permissionCamerra();
        }
        else
        {
            takePhoto()
        }
    }
    //-------------------------------------------------------------
    //
    private fun permissionWriteStorege()
    {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_SELECT_IMAGE_IN_ALBUM)
    }
    //-------------------------------------------------------------
    //
    private fun permissionCamerra()
    {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)
    }
    //-------------------------------------------------------------
    //save to local database
    fun addInfo()
    {
        if(checkValidation() == true)
        {
            //show dialog..
            val pAlert = AlertDialog.Builder(this@ActWrite).create();
            pAlert.setTitle("Do you want save?");
            pAlert.setMessage("you can choice!!");
            pAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",{
                dialogInterface, i ->

                var title:String = this.edit_Title.text.toString();
                var content:String = this.edit_Content.text.toString();
                var img_path:String ?= this.m_strImgpath;
                if(img_path == null)
                    img_path = ""

                var date:String =  String.format("%s", Util.GetCurrentDateTime())
                var color_code:String = Util.GetRandomColor(m_Context);

                var result = m_App.m_LocalDbCtrl.insertInfo(timeline(seq = "", title = title, content = content, img_path = img_path, date = date, color_code = color_code));

                setResult(Constant.ACTIVITY_FOR_RESULT_INSERT_COMPLETE);
                finish();
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
    //add local image filename
    fun addPhoto()
    {
        //show dialog..
        val pAlert = AlertDialog.Builder(this@ActWrite).create();
        pAlert.setTitle("Do you want add photo?");
        pAlert.setMessage("you can choice!!");
        pAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Gallery",{
            dialogInterface, i ->
            checkPermissionWriteStorage();
            pAlert.dismiss();
        })
        pAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Take Photo",{
            dialogInterface, i ->
            checkPermissionCamera();
            pAlert.dismiss();
        })
        pAlert.show();
    }
    //-------------------------------------------------------------
    //
    fun selectImageInAlbum()
    {
        val pIntent = Intent(Intent.ACTION_GET_CONTENT)
        pIntent.type = "image/*"
        if (pIntent.resolveActivity(packageManager) != null)
        {
            startActivityForResult(pIntent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
    //-------------------------------------------------------------
    //
    fun takePhoto()
    {
        val pIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pIntent.resolveActivity(packageManager) != null)
        {
            startActivityForResult(pIntent, REQUEST_TAKE_PHOTO)
        }
    }
    //-------------------------------------------------------------
    //
    fun saveImage(myBitmap: Bitmap):String
    {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val pFile = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg"))
            pFile.createNewFile()
            val fo = FileOutputStream(pFile)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(pFile.getPath()), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + pFile.getAbsolutePath())
            return pFile.getAbsolutePath()
        }
        catch (e1: IOException)
        {
            e1.printStackTrace()
        }
        return ""
    }
    //-------------------------------------------------------------
    //
    companion object
    {
        private val IMAGE_DIRECTORY = "/mytimeline"
    }
    //-------------------------------------------------------------
    //
    private fun checkValidation():Boolean
    {
        var bResult:Boolean = false;
        var strTitle:String = edit_Title.text.toString()
        var strContent:String = edit_Content.text.toString()

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
            R.id.ly_BottomPhoto -> addPhoto();
            R.id.ly_BottomSend -> addInfo();
        }
    }
}
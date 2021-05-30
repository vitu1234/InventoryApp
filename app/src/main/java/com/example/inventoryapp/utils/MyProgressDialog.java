package com.example.inventoryapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyProgressDialog {

    Context context;
    public ProgressDialog progressDialog;
    public SweetAlertDialog pDialog;
    public MyProgressDialog(Context context) {
        this.context = context;
    }


    public void showDialog(String msg){
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle(msg);
//        progressDialog.setCancelable(false);
////        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void closeDialog(){
//        if(progressDialog != null){
//            progressDialog.dismiss();
//        }
        if (pDialog != null){
            pDialog.dismissWithAnimation();
        }
    }

    public void showDangerAlert(String msg){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF3D00"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void showSuccessAlert(String msg){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FC00D16B"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);
        pDialog.show();
    }

}

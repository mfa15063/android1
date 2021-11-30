package com.ebookfrenzy.foodon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;

public class ReusableCodeForAll {

    public static void ShowAlert(Context context,String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).setTitle(title).setMessage(message).show();
    }

    public static AnimationDrawable anim(Resources getR){
        AnimationDrawable animationDrawable = new AnimationDrawable();

        animationDrawable.addFrame(getR.getDrawable(R.drawable.bghome2), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic2), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic3), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.loginbackgrounds), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic5), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic6), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.bggg), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic9), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic10), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic11), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic12), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic13), 3000);
        animationDrawable.addFrame(getR.getDrawable(R.drawable.pic14), 3000);

        animationDrawable.setOneShot(false);
        animationDrawable.setEnterFadeDuration(450);
        animationDrawable.setExitFadeDuration(1600);
        return animationDrawable;
    }
}

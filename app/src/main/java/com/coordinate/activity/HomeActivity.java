package com.coordinate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coordinate.R;
import com.coordinate.base.view.CoordinateView;

import java.util.Random;

/**
 * Created by LY on 2018/4/6.
 */

public class HomeActivity extends Activity {

    private CoordinateView coordinateView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        coordinateView = findViewById(R.id.coordinateView);

        int[] leNuber = new int[31];
        int[] wkNuber = new int[31];
        Random random = new Random();

        int i = 0 ;
        do{
            int le = random.nextInt(6);
            if(le>=0 && le<6 ){
                leNuber[i++]= le;
            }
        }while (i<31);

        int j = 0 ;
        do{
            int wk = random.nextInt(6);
            if(wk>=0 && wk<6 ){
                wkNuber[j++]= wk;
            }
        }while (j<31);


        coordinateView.setMonth(1 , leNuber  ,wkNuber ,  5);
    }

    /**
     * 添加a方法
     * 在awork分支上进行
     */
    private void a(){
        for( int i = 0 ; i < 100 ; i++ ){
            Log.d("TAG" , "i = "+ i);
        }
    }

    /**
     * 添加b方法
     */
    private void b(){
        for( int i = 0 ; i < 100 ; i++ ){
        }
    }

}

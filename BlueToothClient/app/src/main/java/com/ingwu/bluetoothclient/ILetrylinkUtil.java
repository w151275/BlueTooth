/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ingwu.bluetoothclient;

import android.content.Context;
import android.util.Log;

/**
 * Displays an Android spinner widget backed by data in an array. The array is
 * loaded from the strings.xml resources file.
 */
public class ILetrylinkUtil{
    private static final String TAG = "ILetrylinkUtil";
    private static ILetrylinkUtil sAdapter=null;
    private Context mContext=null;

    public String letrylink_id;
    public String letrylink_uuid;

    public int   letrylink_major;
    public int   letrylink_minor;
    public String password="";
    public byte[] sendData;

    public native int  CheckBLEData(byte[] data);

    public native int  CalculateAccuracy(int rssi);

    public native int  GenSecurityData(String mac);

    public native int  GenLocalData(String mac);

    public native int	GenButtonData(int gpio,int state);

     public  void  onSecurityData(byte[] data )
     {
		 sendData[0]=data[0];
		 sendData[1]=data[1];
		 sendData[2]=data[2];
		 sendData[3]=data[3];
		 sendData[4]=data[4];
		 sendData[5]=data[5];

     }

     public  void  onGenLocalData(byte[] data )
     {
		 sendData[0]=data[0];
		 sendData[1]=data[1];
		 sendData[2]=data[2];
		 sendData[3]=data[3];
		 sendData[4]=data[4];
		 sendData[5]=data[5];

     }


     public  void  onButtonData(byte[] data )
     {
		 sendData[0]=data[0];
		 sendData[1]=data[1];
		 sendData[2]=data[2];
		 sendData[3]=data[3];
		 sendData[4]=data[4];
		 sendData[5]=data[5];
     }


     public  void  onReceiveBeacons(String uuid, int major, int minor )
     {
         Log.i(TAG, "onReceiveBeacons["+uuid+"]["+major+"]["+minor+"]");
         letrylink_uuid=uuid;
         letrylink_major=major;
         letrylink_minor=minor;
       //  letrylink_id=mSqlHandler.queryBtIDByMajorMinor(major,minor);
         if(letrylink_id!=null)
           Log.i(TAG, "id["+letrylink_id+"]");
        else
            letrylink_id="";
     }



     public  void SetContext(Context context)
     {
         mContext=context;
     }
    public static synchronized ILetrylinkUtil getDefaultUtil() {
        if (sAdapter == null) {
           sAdapter = new ILetrylinkUtil();
           sAdapter.letrylink_uuid="";
           sAdapter.letrylink_major=0;
           sAdapter.letrylink_minor=0;
           
           sAdapter.letrylink_id="";
		   sAdapter.sendData = new byte[6];
		 //sAdapter.mContext= Context;
		 
        }
        
        return sAdapter;
    }
	
	/*
	 * this is used to load the 'hello-jni' library on application startup. The
	 * library has already been unpacked into
	 * /data/data/com.example.HelloJni/lib/libhello-jni.so at installation time
	 * by the package manager.
     */
    static {
        System.loadLibrary("iletrylinkutil-jni");
    }
	
}

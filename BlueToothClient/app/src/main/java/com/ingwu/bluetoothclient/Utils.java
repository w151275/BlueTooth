package com.ingwu.bluetoothclient;

public class Utils {

	public static final String TAG = "BeaconTool";
	public static BeaconService mService = null;
	public static final int TypeManually = 1;
	public static final int TypeQRCodeScan = 2;

	public static final int ID = 0;
	public static final int UUID = 1;
	public static final int Major = 2;
	public static final int Minor = 3;
	public static final int Txpower = 4;
	public static final int advslot = 5;

	
	public static final String UPDATE_BEACON_INFO = "updateblecfginfo";
	public static final String SHOW_LISTDEV_INFO = "showlistdevinfo";

}

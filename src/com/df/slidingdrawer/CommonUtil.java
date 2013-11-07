package com.df.slidingdrawer;


import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

public class CommonUtil {
	
	/**
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		if(context == null){
			return (int)dipValue;
		}
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	
	/**
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		if(context == null){
			return (int)pxValue;
		}
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * @param context
	 * @param px
	 * @return
	 */
	public static float px2sp(Context context, Float px) {
		if(context == null){
			return px.intValue();
		}
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}

	/**
	 * @param context
	 * @param sp
	 * @return
	 */
	public static float sp2px(Context context, float sp) {
		if(context == null){
			return sp;
		}
		Resources r = context.getResources();
		float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				r.getDisplayMetrics());
		return size;
	}

	private static int screenWidthPixels;
	private static int screenHeightPixels;

	/**
	 * @param context
	 * @return
	 */
	public static int getScreenWidthPixels(Context context) {

		if (context == null) {
			Log.e("error","Can't get screen size while the activity is null!");
			return 0;
		}

		if (screenWidthPixels > 0) {
			return screenWidthPixels;
		}
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		screenWidthPixels = dm.widthPixels;
		return screenWidthPixels;
	}

	/**
	 * @param context
	 * @return
	 */
	public static int getScreenHeightPixels(Context context) {
		if (context == null) {
			Log.e("error","Can't get screen size while the activity is null!");
			return 0;
		}

		if (screenHeightPixels > 0) {
			return screenHeightPixels;
		}
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		screenHeightPixels = dm.heightPixels;
		return screenHeightPixels;
	}

}


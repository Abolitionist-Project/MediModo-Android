package com.quantimodo.medication.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ConvertUtils
{
	public static int dpToPx(float dp, Resources res)
	{
		DisplayMetrics metrics = res.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	public static int pxToDp(float px, Resources res)
	{
		DisplayMetrics metrics = res.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return (int) dp;
	}

	public static float spToPx(float px, Resources res)
	{
		float scaledDensity = res.getDisplayMetrics().scaledDensity;
		return px * scaledDensity;
	}
}

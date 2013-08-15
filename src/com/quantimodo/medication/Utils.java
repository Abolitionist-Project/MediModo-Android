package com.quantimodo.medication;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Utils
{
	public static int convertDpToPixel(float dp, Resources res)
	{
		DisplayMetrics metrics = res.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	public static int convertPixelsToDp(float px, Resources res)
	{
		DisplayMetrics metrics = res.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return (int) dp;
	}

	public static float convertSpToPixels(float px, Resources res)
	{
		float scaledDensity = res.getDisplayMetrics().scaledDensity;
		return px * scaledDensity;
	}

	public static void expandView(final View v, Animation.AnimationListener listener)
	{
		v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation()
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t)
			{
				v.getLayoutParams().height = interpolatedTime == 1
						? ViewGroup.LayoutParams.WRAP_CONTENT
						: (int) (targtetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds()
			{
				return true;
			}
		};

		a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
		if (listener != null)
		{
			a.setAnimationListener(listener);
		}
		v.startAnimation(a);
	}

	public static void collapseView(final View v, Animation.AnimationListener listener)
	{
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation()
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t)
			{
				if (interpolatedTime == 1)
				{
					v.setVisibility(View.GONE);
				}
				else
				{
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds()
			{
				return true;
			}
		};

		a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 3);
		if (listener != null)
		{
			a.setAnimationListener(listener);
		}
		v.startAnimation(a);
	}
}

package com.quantimodo.medication.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRoboto extends TextView
{
	private static Typeface font;
	private Context mContext;

	public TextViewRoboto(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
		createFont();
	}

	public TextViewRoboto(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		createFont();
	}

	public TextViewRoboto(Context context)
	{
		super(context);
		mContext = context;
		createFont();
	}

	public void createFont()
	{
		if (font == null)
		{
			font = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto_regular.ttf");
		}
		setTypeface(font);
	}

	@Override
	public void setTypeface(Typeface tf)
	{
		super.setTypeface(tf);
	}

}
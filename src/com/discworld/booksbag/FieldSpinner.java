package com.discworld.booksbag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class FieldSpinner extends LinearLayout
{
   private Title oTitle;
   private Spinner oSpinner;
   
   public FieldSpinner(Context context)
   {
      super(context);
      
      vInit(context);
   }

   public FieldSpinner(Context context, AttributeSet attrs)
   {
      super(context, attrs);

      vInit(context);
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldEditTextUpdatableClearable, 0, 0);
      
      String title = a.getString(R.styleable.FieldEditTextUpdatableClearable_title);
      int titleValueColor = a.getColor(R.styleable.FieldEditTextUpdatableClearable_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldEditTextUpdatableClearable_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldEditTextUpdatableClearable_titleLineSize, 0);
      String contentDescription = a.getString(R.styleable.FieldEditTextUpdatableClearable_android_contentDescription);

      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      oTitle.setText(title);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
      oSpinner.setContentDescription(contentDescription);
   }
   
   void vInit(Context context)
   {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_spinner, this, true);
      
      oTitle = (Title)this.findViewById(R.id.title);
      oSpinner = (Spinner) this.findViewById(R.id.spinner);
   }
   
   public void setTitle(String title)
   {
      oTitle.setText(title);
   }
   
   public void setTitle(int resid)
   {
      oTitle.setText(resid);
   }
   
   public void setTitleColor(int valueColor)
   {
      oTitle.setColor(valueColor);
   }
   
   public void setTitleTextSize(int textSize)
   {
      oTitle.setTextSize(textSize);
   }
   
   public void setLineSize(int lineSize)
   {
      oTitle.setTextSize(lineSize);
   }

   public void setContentDescription(String contentDescription)
   {
      oSpinner.setContentDescription(contentDescription);
   }
   
   public void setAdapter(ArrayAdapter<?> adapter)
   {
      oSpinner.setAdapter(adapter);
   }
   
   public void setOnItemSelectedListener(OnItemSelectedListener listener)
   {
      oSpinner.setOnItemSelectedListener(listener);
   }

   public void setSelection(int position)
   {
      if(position >= 0)
         oSpinner.setSelection(position);
   }
}

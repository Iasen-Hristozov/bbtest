package com.discworld.booksbag;

import com.discworld.booksbag.dto.AutoCompleteTextViewX;
import com.discworld.booksbag.dto.AutoCompleteTextViewX.OnUpdateListener;
import com.discworld.booksbag.dto.AutoCompleteTextViewX.Callback;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class FieldAutoCompleteTextView extends LinearLayout
{
   
   private Title oTitle;
   AutoCompleteTextViewX oAutoCompleteTextViewX;
   
   public FieldAutoCompleteTextView(Context context)
   {
      super(context);
      
      vInit();
   }

   public FieldAutoCompleteTextView(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      
      vInit();
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldAutoCompleteTextView, 0, 0);

      String titleText = a.getString(R.styleable.FieldAutoCompleteTextView_title);
      int titleValueColor = a.getColor(R.styleable.FieldAutoCompleteTextView_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldAutoCompleteTextView_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldAutoCompleteTextView_titleLineSize, 0);
      String text = a.getString(R.styleable.FieldAutoCompleteTextView_android_text);
      String hint = a.getString(R.styleable.FieldAutoCompleteTextView_android_hint);
      
      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      oTitle.setText(titleText);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
      oAutoCompleteTextViewX.setText(text);
      oAutoCompleteTextViewX.setHint(hint);
   }
   
   public void vInit()
   {
      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_auto_complete_text_view, this, true);
      
      oTitle = (Title)this.findViewById(R.id.title);
      oAutoCompleteTextViewX = (AutoCompleteTextViewX) this.findViewById(R.id.autoCompleteTextView);
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
   
   public void setText(String text)
   {
      oAutoCompleteTextViewX.setText(text);
   }

   public void setText(int resid)
   {
      oAutoCompleteTextViewX.setText(resid);;
   }
   
   public Editable getText()
   {
      return oAutoCompleteTextViewX.getText();
   }
   
   public void setHint(String hint)
   {
      oAutoCompleteTextViewX.setHint(hint);
   }

   public void setHint(int resid)
   {
      oAutoCompleteTextViewX.setHint(resid);;
   }
   
   public void setThreshold(int threshold)
   {
      oAutoCompleteTextViewX.setThreshold(threshold);
   }
   
   public void setAdapter(ArrayAdapter<?> adapter)
   {
      oAutoCompleteTextViewX.setAdapter(adapter);
   }
   
   public void setUpdateListener(OnUpdateListener onUpdateListener)
   {
      oAutoCompleteTextViewX.setOnUpdateListener(onUpdateListener);
   }
   
   public void setCallback(Callback callback)
   {
      oAutoCompleteTextViewX.setCallback(callback);
   }   
   
   public void setOnItemSelectedListener(OnItemSelectedListener l)
   {
      oAutoCompleteTextViewX.setOnItemSelectedListener(l);
   }
}

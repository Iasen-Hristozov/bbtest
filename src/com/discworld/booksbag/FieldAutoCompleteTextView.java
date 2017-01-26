package com.discworld.booksbag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

public class FieldAutoCompleteTextView extends LinearLayout
{
   
   private Title oTitle;
   AutoCompleteTextView autoCompleteTextView;
   
   public FieldAutoCompleteTextView(Context context)
   {
      super(context);
   }

   public FieldAutoCompleteTextView(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldAutoCompleteTextView, 0, 0);
      String titleText = a.getString(R.styleable.FieldAutoCompleteTextView_text);
      
      int titleValueColor = a.getColor(R.styleable.FieldAutoCompleteTextView_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldAutoCompleteTextView_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldAutoCompleteTextView_titleLineSize, 0);
      String text = a.getString(R.styleable.FieldAutoCompleteTextView_text);
      
      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_auto_complete_text_view, this, true);
//      addView(inflater.inflate(R.layout.title, this));
      
      oTitle = (Title)this.findViewById(R.id.title);
      oTitle.setText(titleText);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
      autoCompleteTextView = (AutoCompleteTextView) this.findViewById(R.id.autoCompleteTextView);
      autoCompleteTextView.setText(text);
   }
   
   public void setTitle(String title)
   {
      oTitle.setText(title);
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
      autoCompleteTextView.setText(text);
   }
   
   public void setAdapter(ArrayAdapter<?> adapter)
   {
      autoCompleteTextView.setAdapter(adapter);
   }
   
   public void setOnItemSelectedListener(OnItemSelectedListener l)
   {
      autoCompleteTextView.setOnItemSelectedListener(l);
   }
}

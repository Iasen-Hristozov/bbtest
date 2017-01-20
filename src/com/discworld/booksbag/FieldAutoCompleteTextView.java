package com.discworld.booksbag;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;

public class FieldAutoCompleteTextView extends LinearLayout
{
   
   AutoCompleteTextView autoCompleteTextView;
   
   public FieldAutoCompleteTextView(Context context)
   {
      super(context);
      // TODO Auto-generated constructor stub
   }

   public FieldAutoCompleteTextView(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldAutoCompleteTextView, 0, 0);
      String titleText = a.getString(R.styleable.FieldAutoCompleteTextView_text);
      
      int valueColor = a.getColor(R.styleable.FieldAutoCompleteTextView_color, android.R.color.black);
      int textSize = a.getDimensionPixelOffset(R.styleable.FieldAutoCompleteTextView_textSize, 0);
      int lineSize = a.getDimensionPixelOffset(R.styleable.FieldAutoCompleteTextView_lineSize, 1);
      
      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_auto_complete_text_view, this, true);
//      addView(inflater.inflate(R.layout.title, this));
      
      Title title = (Title)this.findViewById(R.id.title);
      title.setText(titleText);
      title.setColor(valueColor);
      title.setTextSize(textSize);
      title.setLineSize(lineSize);
      
      autoCompleteTextView = (AutoCompleteTextView) this.findViewById(R.id.autoCompleteTextView);
      
//      TextView title = (TextView)this.findViewById(R.id.tv_title);
//      LinearLayout line = (LinearLayout)this.findViewById(R.id.ll_line);
//      TextView title1 = (TextView) getChildAt(0);
//      title.setText(titleText);
//      title.setTextColor(valueColor);
//      if(textSize > 0)
//         title.setTextSize(textSize);
      
//      line.setBackgroundColor(valueColor);
//      android.view.ViewGroup.LayoutParams params = line.getLayoutParams();
//   // Changes the height and width to the specified *pixels*
//      if(lineSize > 0)
//      {
//         params.height = lineSize;
//         params.width = LayoutParams.MATCH_PARENT;
//         line.setLayoutParams(params);
//      }
         
      // mValue = getChildAt(1);
      // mValue.setBackgroundColor(valueColor);
      // mImage = (ImageView) getChildAt(2);
//   }   
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

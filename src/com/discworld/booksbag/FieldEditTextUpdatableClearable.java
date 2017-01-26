package com.discworld.booksbag;

import com.discworld.booksbag.dto.EditTextUpdatable;
import com.discworld.booksbag.dto.EditTextUpdatable.OnUpdateListener;
import com.discworld.booksbag.dto.EditTextUpdatableClearable;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FieldEditTextUpdatableClearable extends LinearLayout
{
   private Title oTitle;
   private EditTextUpdatableClearable editTextUpdatableClearable;
   
   public FieldEditTextUpdatableClearable(Context context)
   {
      super(context);
      // TODO Auto-generated constructor stub
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_edit_text_updatable_clearable, this, true);
      oTitle = (Title)this.findViewById(R.id.title);
      editTextUpdatableClearable = (EditTextUpdatableClearable) this.findViewById(R.id.editTextUpdatableClearable);

   }

   public FieldEditTextUpdatableClearable(Context context, AttributeSet attrs)
   {
      super(context, attrs);
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldEditTextUpdatableClearable, 0, 0);
      String title = a.getString(R.styleable.FieldEditTextUpdatableClearable_title);
      int titleValueColor = a.getColor(R.styleable.FieldEditTextUpdatableClearable_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldEditTextUpdatableClearable_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldEditTextUpdatableClearable_titleLineSize, 0);
      String text = a.getString(R.styleable.FieldEditTextUpdatableClearable_android_text);
      int inputType = a.getInteger(R.styleable.FieldEditTextUpdatableClearable_android_inputType, 0);
      String contentDescription = a.getString(R.styleable.FieldEditTextUpdatableClearable_android_contentDescription);
      String hint = a.getString(R.styleable.FieldEditTextUpdatableClearable_android_hint);

      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_edit_text_updatable_clearable, this, true);
//      addView(inflater.inflate(R.layout.title, this));
      
      oTitle = (Title)this.findViewById(R.id.title);
      oTitle.setText(title);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
      editTextUpdatableClearable = (EditTextUpdatableClearable) this.findViewById(R.id.editTextUpdatableClearable);
      editTextUpdatableClearable.setText(text);
      if(inputType > 0)
         editTextUpdatableClearable.setInputType(inputType);
      editTextUpdatableClearable.setContentDescription(contentDescription);
      editTextUpdatableClearable.setHint(hint);
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
      editTextUpdatableClearable.setText(text);
   }

   public void setText(int resid)
   {
      editTextUpdatableClearable.setText(resid);
   }
   
   public void setInputType(int type)
   {
      editTextUpdatableClearable.setInputType(type);;
   }

   public void setContentDescription(String contentDescription)
   {
      editTextUpdatableClearable.setContentDescription(contentDescription);
   }
   
   public void setHint(String hint)
   {
      editTextUpdatableClearable.setHint(hint);
   }
   
   public void setHint(int resid)
   {
      editTextUpdatableClearable.setHint(resid);
   }
   
   public void setUpdateListener(OnUpdateListener onUpdateListener)
   {
      editTextUpdatableClearable.setOnUpdateListener(onUpdateListener);
   }
   
   public void setCallback(EditTextUpdatableClearable.Callback callback)
   {
      editTextUpdatableClearable.setCallback(callback);
   }
   
   public Editable getText()
   {
      return editTextUpdatableClearable.getText();
   }
}

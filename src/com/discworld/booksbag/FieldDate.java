package com.discworld.booksbag;

import java.util.Calendar;

import com.discworld.booksbag.dto.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

public class FieldDate extends LinearLayout
{
   private Title oTitle;
   private Button btnSpinner;
   private Date date = new Date(0);
   private String hint = "";
   private String contentDescription = "";
   private OnUpdateListener onUpdateListener = null;
   
   public FieldDate(Context context)
   {
      super(context);
      
//      vInit(context, alFields, oFieldType);
      vInit(context);
   }
   
   public FieldDate(Context context, AttributeSet attrs)
   {
      super(context, attrs);

      vInit(context);
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldDate, 0, 0);
      
      String title = a.getString(R.styleable.FieldDate_title);
      int titleValueColor = a.getColor(R.styleable.FieldMultiSpinner_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldMultiSpinner_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldMultiSpinner_titleLineSize, 0);
      contentDescription = a.getString(R.styleable.FieldMultiSpinner_android_contentDescription);
      hint = a.getString(R.styleable.FieldMultiSpinner_android_hint);

      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      oTitle.setText(title);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
      btnSpinner.setContentDescription(contentDescription);
   }
   
   void vInit(Context context)
   {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_date, this, true);
      
      oTitle = (Title)this.findViewById(R.id.title);
      btnSpinner = (Button) this.findViewById(R.id.spinner);
      
      final Activity activity = (Activity) context;
      btnSpinner.setOnClickListener(new OnClickListener() 
      {
         @Override
         public void onClick(View v) 
         {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(activity.getFragmentManager(), "datePicker");         
         }
      });
   }
   
   private void setButtonText(Button oButton, Date date)
   {
      if(date.toInt() != 0)
      {
         
         oButton.setText(date.iDay + "/" + date.iMonth+ "/" + date.iYear);
         oButton.setTextColor(Color.BLACK);
      }
      else
      {
         oButton.setText(hint);
         oButton.setTextColor(Color.GRAY);
      }
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
      btnSpinner.setContentDescription(contentDescription);
   }
   
   public void setHint(String hint)
   {
      this.hint = hint;
      setButtonText(btnSpinner, date);
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
      setButtonText(btnSpinner, date);
   }

   private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
   {
      @Override
      public Dialog onCreateDialog(Bundle savedInstanceState)
      {
         if(date.toInt() == 0)
         {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            date = new Date(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
         }

         // Create a new instance of DatePickerDialog and return it
         return new DatePickerDialog(getActivity(), this, date.iYear, date.iMonth - 1, date.iDay);
      }

      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
      {
         date = new Date(dayOfMonth, monthOfYear + 1, year);
         setButtonText(btnSpinner, date);
         if(onUpdateListener != null)
         {
            onUpdateListener.onUpdate(FieldDate.this);
         }
      }
   }

   public void setUpdateListener(OnUpdateListener onUpdateListener)
   {
      this.onUpdateListener = onUpdateListener;
   }
   
   public interface OnUpdateListener
   {
      void onUpdate(Date date);
      void onUpdate(FieldDate oFieldDate);
   }
}

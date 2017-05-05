package com.discworld.booksbag;

import java.util.ArrayList;

import com.discworld.booksbag.dto.Field;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class FieldSpinner extends LinearLayout
{
   private Title oTitle;
   private Spinner oSpinner;
//   private ArrayList<Field> alFieldValues = null;
   private OnUpdateListener onUpdateListener = null;

   public FieldSpinner(Context context)
   {
      super(context);
      
      vInit(context);
   }

   
   public FieldSpinner(Context context, ArrayList<Field> alFieldValues)
   {
      super(context);
      
      vInit(context, alFieldValues);
   }

   public FieldSpinner(Context context, AttributeSet attrs)
   {
      super(context, attrs);

      vInit(context);
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldSpinner, 0, 0);
      
      String title = a.getString(R.styleable.FieldSpinner_title);
      int titleValueColor = a.getColor(R.styleable.FieldSpinner_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldSpinner_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldSpinner_titleLineSize, 0);
      String contentDescription = a.getString(R.styleable.FieldSpinner_android_contentDescription);

      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      oTitle.setText(title);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
      oSpinner.setContentDescription(contentDescription);
   }
   
   public FieldSpinner(Context context, AttributeSet attrs, ArrayList<Field> alFieldValues)
   {
      super(context, attrs);

      vInit(context, alFieldValues);
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldSpinner, 0, 0);
      
      String title = a.getString(R.styleable.FieldSpinner_title);
      int titleValueColor = a.getColor(R.styleable.FieldSpinner_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldSpinner_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldSpinner_titleLineSize, 0);
      String contentDescription = a.getString(R.styleable.FieldSpinner_android_contentDescription);

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
//      this.alFieldValues = alFieldValues;
      
      
//      Field oField = null;
//      int iSelected = -1;
//      
//      String tFieldValues[] = new String[alFieldValues.size()];
//      for(int i = 0; i < alFieldValues.size(); i++)
//      {
//         tFieldValues[i] = alFieldValues.get(i).sValue;
//         if(oField != null && oField.iID == alFieldValues.get(i).iID)
//            iSelected = i;
//      }
//
////      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);  
//      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (context, R.layout.spinner_item, tFieldValues);
////      ArrayAdapter oArrayAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
//      oSpinner.setAdapter(oArrayAdapter);
//      oSpinner.setSelection(iSelected);
//      oSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//      {
//         @Override
//         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
//         {
////            f.copy(alFieldsValues.get(pos));
////            ((Field) oFieldSpinner.getTag()).copy(alFieldsValues.get(pos));
//            if(onUpdateListener != null)
//               onUpdateListener.onUpdate(FieldSpinner.this, pos);
//            
//         }
//
//         @Override
//         public void onNothingSelected(AdapterView<?> parent)
//         {
//            // TODO Auto-generated method stub
//            
//         }
//      });      
   }
   
   void vInit(Context context, ArrayList<Field> alFieldValues)
   {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_spinner, this, true);
      
      oTitle = (Title)this.findViewById(R.id.title);
      oSpinner = (Spinner) this.findViewById(R.id.spinner);
//      this.alFieldValues = alFieldValues;
      
      
      Field oField = null;
      int iSelected = -1;
      
      String tFieldValues[] = new String[alFieldValues.size()];
      for(int i = 0; i < alFieldValues.size(); i++)
      {
         tFieldValues[i] = alFieldValues.get(i).sValue;
         if(oField != null && oField.iID == alFieldValues.get(i).iID)
            iSelected = i;
      }

//      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);  
      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (context, R.layout.spinner_item, tFieldValues);
//      ArrayAdapter oArrayAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
      oSpinner.setAdapter(oArrayAdapter);
      oSpinner.setSelection(iSelected);
      oSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
         {
//            f.copy(alFieldsValues.get(pos));
//            ((Field) oFieldSpinner.getTag()).copy(alFieldsValues.get(pos));
            if(onUpdateListener != null)
               onUpdateListener.onUpdate(FieldSpinner.this, pos);
            
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent)
         {
            // TODO Auto-generated method stub
            
         }
      });      
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
   
   public void setOnUpdateListerener(OnUpdateListener onUpdateListener)
   {
      this.onUpdateListener = onUpdateListener;
   }

   public void setSelection(int position)
   {
      if(position >= 0)
         oSpinner.setSelection(position);
   }
   
   public interface OnUpdateListener
   {
      public void onUpdate(FieldSpinner oFieldSpinner, int pos);
   }
}

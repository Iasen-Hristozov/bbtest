package com.discworld.booksbag;

import java.util.ArrayList;
import java.util.List;

import com.discworld.booksbag.dto.AutoCompleteTextViewX;
import com.discworld.booksbag.dto.Field;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FieldMultiText extends LinearLayout
{
   private Title oTitle;
   private LinearLayout llFields;
   private ArrayList<Field> alFields = null;
   private ArrayAdapter<?> adapter;
   private int iEnuType = 0;
   private String tDictionaryFieldsValues[];
   private String hint = "";
   private String contentDescription = "";
   
   public FieldMultiText(Context context, ArrayList<Field> alFields, int iEnuType)
   {
      super(context);
      
      vInit(context, alFields, iEnuType);
   }

   public FieldMultiText(Context context, AttributeSet attrs, ArrayList<Field> alFields, int iEnuType)
   {
      super(context, attrs);
     
      vInit(context, alFields, iEnuType);
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldMultiText, 0, 0);
      
      String title = a.getString(R.styleable.FieldMultiText_title);
      int titleValueColor = a.getColor(R.styleable.FieldMultiText_titleColor, 0);
      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldMultiText_titleTextSize, 0);
      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldMultiText_titleLineSize, 0);
      contentDescription = a.getString(R.styleable.FieldMultiText_android_contentDescription);
      hint = a.getString(R.styleable.FieldMultiText_android_hint);

      a.recycle();

      setOrientation(LinearLayout.VERTICAL);
      setGravity(Gravity.CENTER_VERTICAL);

      oTitle.setText(title);
      oTitle.setColor(titleValueColor);
      oTitle.setTextSize(titleTextSize);
      oTitle.setLineSize(titleLineSize);
      
   }
   
   void vInit(Context context, ArrayList<Field> alFields, int iType)
   {
      this.iEnuType = iType;
      this.alFields = alFields;
      
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_spinner, this, true);
      
      oTitle = (Title)this.findViewById(R.id.title);
      
      alFields = new ArrayList<>();
      
      llFields = (LinearLayout) findViewById(R.id.ll_fields);
      findViewById(R.id.ib_add_field).setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            addField(llFields);
         }
      });
   }

   private void addField(LinearLayout llFields)
   {
      Field oField = new Field(iEnuType);
      alFields.add(oField);
      
      LayoutInflater oInflater = LayoutInflater.from(getContext());
      final View vRow = oInflater.inflate(R.layout.row_field, null);
      vRow.findViewById(R.id.ib_remove_field).setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            View vParent = (View) v.getParent();
            removeField(vParent);
         }
      });

      final AutoCompleteTextViewX etValue = (AutoCompleteTextViewX)vRow.findViewById(R.id.et_value);
      etValue.setHint(hint);
// RECONSIDER !!!!
//      etValue.setOnUpdateListener(new AutoCompleteTextViewX.OnUpdateListener()
//      {
//         @Override
//         public void onUpdate(EditText et)
//         {
//            String sAuthor = et.getText().toString();
//            ((Field) vRow.getTag()).sValue = et.getText().toString();
//         }
//      });
      etValue.setAdapter(adapter);
      etValue.setThreshold(1);
      if(oField.iID != 0) // fldAuthor is not new 
         etValue.setText(oField.sValue);
      etValue.setOnItemClickListener(new OnItemClickListener()
      {
         @Override
         public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
         {
//            String selection = (String) adapter.getItemAtPosition(position);
            Field selection = (Field) adapter.getItemAtPosition(position);
            ((Field) vRow.getTag()).copy(selection);
//            int pos = -1;
//            for (int i = 0; i < adapter.getCount(); i++)
//            {
//               
//               if(adapter.getItemAtPosition(i).equals(selection))
//                  ((Field) vRow.getTag()).copy(f);
//            }
//            for (int i = 0; i < tDictionaryFieldsValues.length; i++) 
//            {
//               if (tDictionaryFieldsValues[i].equals(selection)) 
//               {
//                  pos = i;
//                  break;
//               }
//            }
//            System.out.println("Position " + pos); //check it now in Logcat            
         }
      });
      vRow.setTag(oField);
      llFields.addView(vRow);
      if(llFields.getChildCount() == 1)
         vRow.findViewById(R.id.ib_remove_author).setVisibility(View.INVISIBLE);
   }
   
   private void removeField(View vParent)
   {
      Field fldAuthor = (Field) vParent.getTag();
      alFields.remove(fldAuthor);
      ViewGroup parent = (ViewGroup) vParent.getParent();
      parent.removeView(vParent);
   }   
   
   public void setFields(ArrayList<Field> alFields)
   {
      this.alFields  = alFields;
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
      this.contentDescription = contentDescription;
   }

   public void setHint(String hint)
   {
      this.hint = hint;
   }
   
   public void setAdapter(ArrayAdapter<?> adapter)
   {
      this.adapter = adapter;
   }
   
   public interface OnAddRemoveFiled
   {
      public void onFieldAdd();
      public void onFieldRemove();
   }
   
   public class FieldArrayAdapter extends ArrayAdapter<Field> 
   {
//      private final Context context;
      private final ArrayList<Field> alFields;
      private final ArrayList<Field> alFields_All;
      private final ArrayList<Field> alFields_Suggestion;
//      private final int mLayoutResourceId;

      public FieldArrayAdapter(Context context, int resource, ArrayList<Field> alFields) 
      {
         super(context, resource, alFields);
//         this.context = context;
//         this.mLayoutResourceId = resource;
         this.alFields = new ArrayList<>(alFields);
         this.alFields_All = new ArrayList<>(alFields);
         this.alFields_Suggestion = new ArrayList<>();
      }

      public int getCount() 
      {
         return alFields.size();
      }

      public Field getItem(int position) 
      {
         return alFields.get(position);
      }

      public long getItemId(int position) 
      {
         return position;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) 
      {
         TextView view = (TextView) super.getView(position, convertView, parent);
         Field oField = getItem(position);
         view.setText(oField.sValue);
         return convertView;
      }

      @Override
      public Filter getFilter() 
      {
         return new Filter() 
         {
            @Override
            public String convertResultToString(Object resultValue) 
            {
               return ((Field) resultValue).sValue;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) 
            {
               if (constraint != null) 
               {
                  alFields_Suggestion.clear();
                  for (Field oField : alFields_All) 
                  {
                     if (oField.sValue.toLowerCase().startsWith(constraint.toString().toLowerCase())) 
                     {
                        alFields_Suggestion.add(oField);
                     }
                  }
                  FilterResults filterResults = new FilterResults();
                  filterResults.values = alFields_Suggestion;
                  filterResults.count = alFields_Suggestion.size();
                  return filterResults;
               } 
               else 
               {
                  return new FilterResults();
               }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) 
            {
               alFields.clear();
               if (results != null && results.count > 0) 
               {
                  // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                  List<?> result = (List<?>) results.values;
                  for (Object object : result) 
                  {
                     if(object instanceof Field) 
                     {
                        alFields.add((Field) object);
                     }
                  }
               } 
               else if (constraint == null) 
               {
                  // no filter, add entire original list back in
                  alFields.addAll(alFields_All);
               }
               notifyDataSetChanged();
            }
         };
      }
   }   
}

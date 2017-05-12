package com.discworld.booksbag;

import java.util.ArrayList;
import java.util.List;

import com.discworld.booksbag.FieldMultiTextOld.OnAddRemoveFieldListener;
import com.discworld.booksbag.dto.AutoCompleteTextViewX;
import com.discworld.booksbag.dto.AutoCompleteTextViewX.OnUpdateListener;
//import com.discworld.booksbag.dto.Field;
//import com.discworld.booksbag.dto.FieldType;


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

public class FieldMultiTextOld extends LinearLayout
{
   public interface OnAddRemoveFieldListener
   {

      void onFieldRemove(Object object);

      void onFieldNewAdd(View vRow);

   }

   private Title oTitle;
   private LinearLayout llFields;
//   private ArrayList<Field> alFields = null;
   private ArrayAdapter<Item> adapter;
//   private int iEnuType = 0;
   private String hint = "";
   private String contentDescription = "";
   private Context context;
//   private FieldType oFieldType;
   private ArrayList<? extends Object> alItems;
   private ArrayList<? extends Item> alItems2;
   private OnAddRemoveFieldListener onAddRemoveFieldListener;

   public FieldMultiTextOld(Context context)
   {
      super(context);
      
      vInit(context);
   }
   
   
//   public FieldMultiText(Context context, ArrayList<Field> alFields, FieldType oFieldType)
//   {
//      super(context);
//      
//      vInit(context, alFields, oFieldType);
//   }

   public FieldMultiTextOld(Context context, AttributeSet attrs)
   {
      super(context, attrs);
     
      vInit(context);
      
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
   
//   public FieldMultiText(Context context, AttributeSet attrs, ArrayList<Field> alFields, FieldType oFieldType)
//   {
//      super(context, attrs);
//     
////      vInit(context, alFields, iEnuType);
//      vInit(context, alFields, oFieldType);
//      
//      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldMultiText, 0, 0);
//      
//      String title = a.getString(R.styleable.FieldMultiText_title);
//      int titleValueColor = a.getColor(R.styleable.FieldMultiText_titleColor, 0);
//      int titleTextSize = a.getDimensionPixelOffset(R.styleable.FieldMultiText_titleTextSize, 0);
//      int titleLineSize = a.getDimensionPixelOffset(R.styleable.FieldMultiText_titleLineSize, 0);
//      contentDescription = a.getString(R.styleable.FieldMultiText_android_contentDescription);
//      hint = a.getString(R.styleable.FieldMultiText_android_hint);
//
//      a.recycle();
//
//      setOrientation(LinearLayout.VERTICAL);
//      setGravity(Gravity.CENTER_VERTICAL);
//
//      oTitle.setText(title);
//      oTitle.setColor(titleValueColor);
//      oTitle.setTextSize(titleTextSize);
//      oTitle.setLineSize(titleLineSize);
//   }

   void vInit(Context context)
   {
      this.context = context;
//      this.hint = oFieldType.sName;
      
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_multi_text, this, true);

      oTitle = (Title)this.findViewById(R.id.title);
      
//      setTitle(oFieldType.sName + "s");
      
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
   
//   void vInit(Context context, ArrayList<Field> alFields, FieldType oFieldType)
//   {
//      this.alFields = alFields;
//      this.context = context;
//      this.oFieldType = oFieldType;
//      this.hint = oFieldType.sName;
//      
//      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      inflater.inflate(R.layout.field_multi_text, this, true);
//
//      oTitle = (Title)this.findViewById(R.id.title);
//      
//      setTitle(oFieldType.sName + "s");
//      
////      alFields = new ArrayList<>();
//      
//      llFields = (LinearLayout) findViewById(R.id.ll_fields);
//      findViewById(R.id.ib_add_field).setOnClickListener(new View.OnClickListener()
//      {
//         @Override
//         public void onClick(View v)
//         {
//            addField(llFields);
//         }
//      });
//
//      boolean hasFieldsOfType = false;
//      for(Field oField : alFields)
//      {
//         if(oField.iTypeID == oFieldType.iID)
//         {
//            addField(llFields, oField);
//            hasFieldsOfType = true;
//         }
//      }
//      if(!hasFieldsOfType)
//         addField(llFields);
//   }

//   public void setItems(ArrayList<? extends Object> alItems)
//   {
//      this.alItems = alItems;
//      addFields();
//   }
   
   private void addFields()
   {
      if(alItems == null || adapter == null)
         return;
      
      boolean hasFieldsOfType = false;
      for(int i = 0; i < adapter.getCount(); i++)
      {
         if(alItems.contains(adapter.getItem(i)))
         {
            addField(llFields, adapter.getItem(i));
            hasFieldsOfType = true;
         }
      }
      if(!hasFieldsOfType)
         addField(llFields);

   }
   
   private void addField(LinearLayout llFields)
   {
//      Field oField = new Field(oFieldType.iID);
//      alFields.add(oField);
//      addField(llFields, oField);
      
   }

//   private void addField(LinearLayout llFields, Field oField)
//   private void addField(LinearLayout llFields, Object object)
   private void addField(LinearLayout llFields, Item item)
   {
      LayoutInflater oInflater = LayoutInflater.from(context);
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
      etValue.setContentDescription(contentDescription);
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
//      if(oField.iID != 0) // fldAuthor is not new 
//         etValue.setText(oField.sValue);
      etValue.setText(item.getValue());
      
      etValue.setOnItemClickListener(new OnItemClickListener()
      {
         @Override
         public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
         {
//            String selection = (String) adapter.getItemAtPosition(position);
//            Field selection = (Field) adapter.getItemAtPosition(position);
//            ((Field)vRow.getTag()).copy(selection);
            
            Item selection = (Item) adapter.getItemAtPosition(position);
//            ((Field)vRow.getTag()).copy(selection);
            ((Item)vRow.getTag()).copy(selection);
            
//            ((Field) vRow.getTag()).copy(selection);
            int pos = -1;
            for (int i = 0; i < adapter.getCount(); i++)
            {
               
               if(adapter.getItemAtPosition(i).equals(selection))
               {
//                  ((Field) vRow.getTag()).copy(f);
//                  etValue.setText(selection.sValue);
//                  pos = i;
               }
            }
//            for (int i = 0; i < tDictionaryFieldsValues.length; i++) 
//            {
//               if (tDictionaryFieldsValues[i].equals(selection)) 
//               {
//                  pos = i;
//                  break;
//               }
//            }
            System.out.println("Position " + pos); //check it now in Logcat            
         }
      });
      etValue.setOnUpdateListener(new OnUpdateListener()
      {
         @Override
         public void onUpdate(EditText et)
         {
//            ((Field)vRow.getTag()).sValue = et.getText().toString();
            ((Item)vRow.getTag()).setValue(et.getText().toString());
         }
      });
//      vRow.setTag(oField);
      vRow.setTag(item);

      llFields.addView(vRow);
      if(llFields.getChildCount() == 1)
         vRow.findViewById(R.id.ib_remove_field).setVisibility(View.INVISIBLE);
   }
   
   private void removeField(View vParent)
   {
//      Field fldAuthor = (Field) vParent.getTag();
//      alFields.remove(fldAuthor);
      Item item = (Item) vParent.getTag();
      alItems2.remove(item);
      ViewGroup parent = (ViewGroup) vParent.getParent();
      parent.removeView(vParent);
   }   
   
//   public void setFields(ArrayList<Field> alFields)
//   {
//      this.alFields  = alFields;
//   }
   
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
   
   public void setAdapter(ArrayAdapter<Item> adapter)
   {
      this.adapter = adapter;
   }
   
   public interface OnAddRemoveFiled
   {
      public void onFieldAdd();
      public void onFieldRemove();
   }
   
//   public class FieldArrayAdapter extends ArrayAdapter<Field> 
//   {
////      private final Context context;
//      private final ArrayList<Field> alFields;
//      private final ArrayList<Field> alFields_All;
//      private final ArrayList<Field> alFields_Suggestion;
////      private final int mLayoutResourceId;
//
//      public FieldArrayAdapter(Context context, int resource, ArrayList<Field> alFields) 
//      {
//         super(context, resource, alFields);
////         this.context = context;
////         this.mLayoutResourceId = resource;
//         this.alFields = new ArrayList<>(alFields);
//         this.alFields_All = new ArrayList<>(alFields);
//         this.alFields_Suggestion = new ArrayList<>();
//      }
//
//      public int getCount() 
//      {
//         return alFields.size();
//      }
//
//      public Field getItem(int position) 
//      {
//         return alFields.get(position);
//      }
//
//      public long getItemId(int position) 
//      {
//         return position;
//      }
//
//      @Override
//      public View getView(int position, View convertView, ViewGroup parent) 
//      {
//         TextView view = (TextView) super.getView(position, convertView, parent);
//         Field oField = getItem(position);
//         view.setText(oField.sValue);
//         return convertView;
//      }
//
//      @Override
//      public Filter getFilter() 
//      {
//         return new Filter() 
//         {
//            @Override
//            public String convertResultToString(Object resultValue) 
//            {
//               return ((Field) resultValue).sValue;
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) 
//            {
//               if (constraint != null) 
//               {
//                  alFields_Suggestion.clear();
//                  for (Field oField : alFields_All) 
//                  {
//                     if (oField.sValue.toLowerCase().startsWith(constraint.toString().toLowerCase())) 
//                     {
//                        alFields_Suggestion.add(oField);
//                     }
//                  }
//                  FilterResults filterResults = new FilterResults();
//                  filterResults.values = alFields_Suggestion;
//                  filterResults.count = alFields_Suggestion.size();
//                  return filterResults;
//               } 
//               else 
//               {
//                  return new FilterResults();
//               }
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) 
//            {
//               alFields.clear();
//               if (results != null && results.count > 0) 
//               {
//                  // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
//                  List<?> result = (List<?>) results.values;
//                  for (Object object : result) 
//                  {
//                     if(object instanceof Field) 
//                     {
//                        alFields.add((Field) object);
//                     }
//                  }
//               } 
//               else if (constraint == null) 
//               {
//                  // no filter, add entire original list back in
//                  alFields.addAll(alFields_All);
//               }
//               notifyDataSetChanged();
//            }
//         };
//      }
//   }

   public void setOnAddRemoveListener(OnAddRemoveFieldListener onAddRemoveFieldListener)
   {
      this.onAddRemoveFieldListener = onAddRemoveFieldListener;
   }   

   public interface Item 
   {
      String getValue();
      void setValue(String string);
      int getId();
      void copy(Object o);
   }
   
   public void setItems2(ArrayList<? extends Item> alItems3)
   {
      this.alItems2 = alItems3;
   }
   
   public void setItems(ArrayAdapter<Item> adapter, ArrayList<? extends Item> alItems3)
   {
      this.adapter = adapter;
      for(Item item: alItems3)
      {
         int i = adapter.getPosition(item);
         if(i >= 0)
            addField(llFields, item);
      }
      
      this.alItems2 = alItems3;
   }
   
}

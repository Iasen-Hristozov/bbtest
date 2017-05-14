package com.discworld.booksbag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discworld.booksbag.FieldMultiSpinner.Item;
import com.discworld.booksbag.dto.AutoCompleteTextViewX;
import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.Changeable;
import com.discworld.booksbag.dto.Date;
import com.discworld.booksbag.dto.EditTextX;
import com.discworld.booksbag.dto.Field;
import com.discworld.booksbag.dto.FieldType;
import com.discworld.booksbag.dto.MultiSpinner;
import com.discworld.booksbag.dto.Price;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class EditBookActivity extends AppCompatActivity implements MultiSpinner.multispinnerListener
{
   public final static String BOOK_ID = "book_id";

   private Book oBook;
   private EditText etTitle,
                    etDescription;

   private LinearLayout llFields;
   private DBAdapter oDbAdapter = null;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_edit_book);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

      oDbAdapter = new DBAdapter(this);

      Bundle extras = getIntent().getExtras();
      if(extras == null)
         return;

      long iBookID = extras.getLong(BOOK_ID);

//      // BEGIN_INCLUDE (inflate_set_custom_view)
//      // Inflate a "Done" custom action bar view to serve as the "Up" affordance.
//      final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//      final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done, null);
//      customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
//            new View.OnClickListener()
//            {
//               @Override
//               public void onClick(View v)
//               {
//                  // "Done"
//                  finish();
//               }
//            });

      // Show the custom action bar view and hide the normal Home icon and title.
//      final ActionBar actionBar = getActionBar();
      final ActionBar actionBar = getSupportActionBar();
      actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                                  ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME| ActionBar.DISPLAY_SHOW_TITLE);
//      actionBar.setCustomView(customActionBarView);
      actionBar.setCustomView(R.layout.actionbar_custom_view_done);
      // END_INCLUDE (inflate_set_custom_view)


      actionBar.getCustomView().findViewById(R.id.actionbar_done)
                               .setOnClickListener(new View.OnClickListener()
                               {
                                  @Override
                                  public void onClick(View v)
                                  {
                                     saveBook();
                                     setResult(RESULT_OK, new Intent());
                                     finish();                  // "Done"
                                  }
                               });

      if(iBookID != 0)
         oBook = oDbAdapter.getBook(iBookID);
      else
         oBook = new Book();
      
      llFields = (LinearLayout) findViewById(R.id.ll_fields);
      
      for(FieldType oFieldType: DBAdapter.FIELD_TYPES)
      {
         switch(oFieldType.iType)
         {
            case FieldType.TYPE_TEXT:
               addFieldText(llFields, oFieldType);
            break;
            
            case FieldType.TYPE_MULTIFIELD:
               addFieldMultiText(llFields, oFieldType);
            break;
            
            case FieldType.TYPE_TEXT_AUTOCOMPLETE:
               addAutocompleteField(llFields, oFieldType);
            break;
            
            case FieldType.TYPE_SPINNER:
               addFieldSpinner(llFields, oFieldType);
            break;
            
            case FieldType.TYPE_MULTI_SPINNER:
               addFieldMultiSpinner(llFields, oFieldType);
            break;
            
            case FieldType.TYPE_MONEY:
               addFieldMoney(llFields, oFieldType);
            break;
            
            case FieldType.TYPE_DATE:
               addFieldDate(llFields, oFieldType);
            break;
               
         }
      }
   }

   @Override
   protected void onPause()
   {
      oDbAdapter.close();

      super.onPause();
   }

   @Override
   protected void onResume()
   {
      super.onResume();

      oDbAdapter.open();

//      oBook = oDbAdapter.getBook(oBook.iID);
   }

   @Override
   public void onItemschecked(boolean[] checked)
   {

   }

   // BEGIN_INCLUDE (handle_cancel)
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      super.onCreateOptionsMenu(menu);
      getMenuInflater().inflate(R.menu.cancel, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case R.id.cancel:
            // "Cancel"
            setResult(RESULT_CANCELED, new Intent());
            finish();
            return true;
      }
      return super.onOptionsItemSelected(item);
   }
   // END_INCLUDE (handle_cancel)

   @Override
   public void onBackPressed()
   {
      saveBook();
      setResult(RESULT_OK, new Intent());
      super.onBackPressed();
   }

   private void saveBook()
   {
      if(oBook.iID != 0)
         oDbAdapter.updateBook(oBook);
      else
         oDbAdapter.insertBook(oBook);
   }
   
   private <T> void addFieldText(LinearLayout rootView, FieldType oFieldType)
   {
      switch(oFieldType.iID)
      {
         case DBAdapter.FLD_TITLE:
//            oField.setTag(oBook.csTitle);
//            cValue = (Changeable<T>) oBook.csTitle;
            addFieldText(rootView, oFieldType, oBook.csTitle);
         break;
         
         
         case DBAdapter.FLD_DESCRIPTION:
//            oField.setTag(oBook.csDescription);
//            cValue = oBook.csDescription;
            addFieldText(rootView, oFieldType, oBook.csDescription);
         break;

         case DBAdapter.FLD_VOLUME:
//            oField.setTag(oBook.ciVolume);
//            cValue = oBook.ciVolume;
            addFieldText(rootView, oFieldType, oBook.ciVolume);
         break;

         case DBAdapter.FLD_PAGES:
//            oField.setTag(oBook.ciPages);
//            cValue = oBook.ciPages;
            addFieldText(rootView, oFieldType, oBook.ciPages);
         break;
         
         case DBAdapter.FLD_EDITION:
//            oField.setTag(oBook.ciEdition);
//            cValue = oBook.ciEdition;
            addFieldText(rootView, oFieldType, oBook.ciEdition);
         break;

         case DBAdapter.FLD_ISBN:
//            oField.setTag(oBook.csISBN);
//            cValue = oBook.csISBN;
            addFieldText(rootView, oFieldType, oBook.csISBN);
         break;
         
         case DBAdapter.FLD_WEB:
//            oField.setTag(oBook.csWeb);
//            cValue = oBook.csWeb;
            addFieldText(rootView, oFieldType, oBook.csWeb);
         break;
         
         default:
            return;
      }
   }

   private <T> void addFieldText(ViewGroup rootView, FieldType oFieldType,  final Changeable<T> cValue)
   {
      final FieldEditTextUpdatableClearable oField = new FieldEditTextUpdatableClearable(this);
      oField.setTitle(oFieldType.sName);
      oField.setText(cValue.toString());
      oField.setHint(oFieldType.sName);
      oField.setInputType(oFieldType.iInputType);
      if(oFieldType.isMultiline)
         oField.setMultiline();
      oField.setUpdateListener(new EditTextX.OnUpdateListener()
      {
         
         @Override
         public void onUpdate(EditText et)
         {
            Class<?> c = cValue.getGenericType();
            T t = null;
            
            Class<?> clazz;
            Object object;
            try
            {
               clazz = Class.forName(c.getName());
               Constructor<?> ctor = clazz.getConstructor(String.class);
               object = ctor.newInstance(et.getText().toString());

            } catch(ClassNotFoundException 
                    | NoSuchMethodException
                    | SecurityException
                    | InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException e)
            {
               e.printStackTrace();
               return;
            } 
            
            cValue.value = (T) object;
            
            if(t instanceof Integer)
            {
               t = (T) Integer.valueOf(et.getText().toString());
               cValue.value = t;
            }
            else if(t instanceof String)
            {
               t = (T) et.getText().toString();
               cValue.value = t;
               
            }
               
         }
      });
      rootView.addView(oField);      
   }   
   

   
   private void addAutocompleteField(ViewGroup rootView, final FieldType oFieldType)
   {
      final ArrayList<Field> alFieldValues = oDbAdapter.getFieldValues(oFieldType.iID);

      Field oField = new Field(oFieldType.iID);
      for(int i = 0; oField == null || i < oBook.alFields.size(); i++)
         if(oFieldType.iID == oBook.alFields.get(i).iTypeID)
            oField = oBook.alFields.get(i);
    
      final FieldAutoCompleteTextView oFieldAutoCompleteTextView = new FieldAutoCompleteTextView(this);
      oFieldAutoCompleteTextView.setTitle(oFieldType.sName);
      oFieldAutoCompleteTextView.setHint(oFieldType.sName);
      if(!oField.sValue.isEmpty())
         oFieldAutoCompleteTextView.setText(oField.sValue);
      oFieldAutoCompleteTextView.setTag(oField);
      
      ArrayFieldsAdapter oArrayAdapter = new ArrayFieldsAdapter(this, android.R.layout.select_dialog_item, alFieldValues);
      oFieldAutoCompleteTextView.setAdapter(oArrayAdapter);
      oFieldAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener()
      {
         public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
         {
            Field fldSelected = (Field)adapter.getItemAtPosition(position);
            ((Field)oFieldAutoCompleteTextView.getTag()).copy(fldSelected);
         }
      });
      oFieldAutoCompleteTextView.setUpdateListener(new AutoCompleteTextViewX.OnUpdateListener()
      {
         @Override
         public void onUpdate(EditText et)
         {
            boolean isFound = false;
            for(Field oField : alFieldValues)
            {
               if(et.getText().toString().equalsIgnoreCase(((Field)oFieldAutoCompleteTextView.getTag()).sValue))
               {
                  isFound = true;
                  ((Field)oFieldAutoCompleteTextView.getTag()).copy(oField);
               }
            }
            if(!isFound)
            {
               Field oNewFieldValue = new Field(oFieldType.iID, et.getText().toString());
               alFieldValues.add(oNewFieldValue);
               oBook.alFields.add(oNewFieldValue);
            }
         }
      });
    
      rootView.addView(oFieldAutoCompleteTextView);
   }   
   
   private void addFieldSpinner(ViewGroup rootView, FieldType oFieldType)
   {
      Field oField = null;
      int iSelected = -1;
      
      final ArrayList<Field> alFieldValues = oDbAdapter.getFieldValues(oFieldType.iID);

//      final FieldSpinner oFieldSpinner = new FieldSpinner(this, alFieldValues);
      final FieldSpinner oFieldSpinner = new FieldSpinner(this);
      oFieldSpinner.setTitle(oFieldType.sName);
      
      for(int i = 0; oField == null || i < oBook.alFields.size() && oField.iTypeID != oFieldType.iID; i++)
         oField = oBook.alFields.get(i);
      
//      for(Field fldTemp : oBook.alFields)
//      {
//         if(fldTemp.iTypeID == oFieldType.iID)
//         {
//            oField = fldTemp;
//            break;
//         }
//      }
      
//      oFieldSpinner.setTag(oField);
//      oFieldSpinner.setOnUpdateListerener(new FieldSpinner.OnUpdateListener()
//      {
//         
//         @Override
//         public void onUpdate(FieldSpinner oFieldSpinner, int pos)
//         {
//            ((Field) oFieldSpinner.getTag()).copy(alFieldValues.get(pos));
//         }
//      });
//      
//      Field oField = null;
//      int iSelected = -1;
//      final FieldSpinner oFieldSpinner = new FieldSpinner(this);
//      oFieldSpinner.setTitle(oFieldType.sName);
//      
//      for(Field fldTemp : oBook.alFields)
//      {
//         if(fldTemp.iTypeID == oFieldType.iID)
//         {
//            oField = fldTemp;
//            break;
//         }
//      }
//
//      final ArrayList<Field> alFieldsValues = oDbAdapter.getFieldValues(oFieldType.iID);
      String tFieldValues[] = new String[alFieldValues.size()];
      for(int i = 0; i < alFieldValues.size(); i++)
      {
         tFieldValues[i] = alFieldValues.get(i).sValue;
         if(oField != null && oField.iID == alFieldValues.get(i).iID)
            iSelected = i;
      }

//      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);  
      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, tFieldValues);
//      ArrayAdapter oArrayAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
      oFieldSpinner.setAdapter(oArrayAdapter);
      oFieldSpinner.setSelection(iSelected);
      oFieldSpinner.setTag(oField);
      oFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
         {
//            f.copy(alFieldsValues.get(pos));
            ((Field) oFieldSpinner.getTag()).copy(alFieldValues.get(pos));
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent)
         {
            // TODO Auto-generated method stub
            
         }
      });
      
      rootView.addView(oFieldSpinner);
   }

   private void addFieldMultiText(ViewGroup rootView, final FieldType oFieldType)
   {
      final FieldMultiText oFieldMultiText = new FieldMultiText(this);
      // TODO Fix it
      oFieldMultiText.setTitle(oFieldType.sName + "s");
      oFieldMultiText.setHint(oFieldType.sName);

      // Set adapter
      final ArrayList<Field> alFieldsValues = oDbAdapter.getFieldValues(oFieldType.iID);
      final ArrayList<FieldMultiText.Item> alItemsValues = new ArrayList<FieldMultiText.Item>();
      for(FieldMultiText.Item field: alFieldsValues)
         alItemsValues.add(field);

//      String tDictionaryValues[] = new String[alFieldsValues.size()];
//      for(int i = 0; i < alFieldsValues.size(); i++)
//         tDictionaryValues[i] = alFieldsValues.get(i).sValue;
//      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tDictionaryValues);  
      final ArrayItemsAdapter oArrayAdapter1 = new ArrayItemsAdapter(this, android.R.layout.select_dialog_item, alItemsValues);
      
      oFieldMultiText.setItems(oArrayAdapter1, oBook.alFields);
      
      oFieldMultiText.setOnAddRemoveListener(new FieldMultiText.OnAddRemoveFieldListener()
      {
         
         @Override
         public void onFieldRemove(View view)
         {
            oBook.alFields.remove((Field) view.getTag());
         }
         
         @Override
         public void onAddNewField(View view)
         {
            Field fldNew = new Field(oFieldType.iID);
            oBook.alFields.add(fldNew);
            view.setTag(fldNew);
         }

//         @Override
//         public void onFieldUpdated(EditText et)
//         {
//            ((Field )et.getTag()).sValue = et.getText().toString();
//         }
         
         @Override
         public void onFieldUpdated(View view, String value)
         {
            boolean isExists = false;
            for(Field field: alFieldsValues)
            {
               if(field.getValue().equalsIgnoreCase(value))
               {
                  ((Field )view.getTag()).copy(field);
                  isExists = true;
               }
            }
            if(!isExists)
               ((Field )view.getTag()).sValue = value;
         }
         

         @Override
         public void onItemSelect(ArrayAdapter<?> adapter, View view, int position)
         {
            ((Field) view.getTag()).copy(alFieldsValues.get(position));
         }

         @Override
         public void onItemSelect(View view, FieldMultiText.Item selection)
         {
            if(selection instanceof Field)
               ((Field) view.getTag()).copy((Field)selection);
         }
      });
      
      rootView.addView(oFieldMultiText);
   }
   
   private void addFieldMultiSpinner(ViewGroup rootView, final FieldType oFieldType)
   {
      final ArrayList<Field> alFieldValues = oDbAdapter.getFieldValues(oFieldType.iID);
      final FieldMultiSpinner oFieldMultiSpinner = new FieldMultiSpinner(this);
      oFieldMultiSpinner.setTitle(oFieldType.sName + "s");
      oFieldMultiSpinner.setHint(oFieldType.sName);
      
      ArrayList<Item> alItems = new ArrayList<>();
      for(Field oFieldValue : alFieldValues)
      {
         Item item = new Item(oFieldValue.sValue);
         item.setSelected(oBook.alFields.contains(oFieldValue));
         alItems.add(item);
      }
      
      oFieldMultiSpinner.setItems(alItems);
      oFieldMultiSpinner.setOnUpdateListener(new FieldMultiSpinner.OnUpdateListener()
      {
         @Override
         public void onUpdate(Item item)
         {
            boolean isFound = false;
            for(Field oFieldValue : alFieldValues)
            {
               if(oFieldValue.sValue.equalsIgnoreCase(item.getTitle()))
               {
                  isFound = true;
                  if(item.isSelected())
                     oBook.alFields.add(oFieldValue);
                  else
                     oBook.alFields.remove(oFieldValue);
                  break;
               }
            }
            if(!isFound)
            {
               Field oNewFieldValue = new Field(oFieldType.iID, item.getTitle());
               alFieldValues.add(oNewFieldValue);
               oBook.alFields.add(oNewFieldValue);
            }
         }
      });
      
      rootView.addView(oFieldMultiSpinner);
   }
   
   @SuppressWarnings("unchecked")
   private void addFieldMoney(ViewGroup rootView, FieldType oFieldType)
   {
      int iSelected = 0;

      final ArrayList<Field> alCurrencies = oDbAdapter.getFieldValues(DBAdapter.FLD_CURRENCY);
      
      final FieldMoney oFieldMoney = new FieldMoney(this);
      
      switch(oFieldType.iID)
      {
         case DBAdapter.FLD_PRICE:
            oFieldMoney.setTag(oBook.csPrice);
         break;
         
         case DBAdapter.FLD_VALUE:
            oFieldMoney.setTag(oBook.csValue);
         break;
         
         default:
            return;
      }
      
      final Price oPrice = new Price(((Changeable<String>) oFieldMoney.getTag()).value);

      oFieldMoney.setTitle(oFieldType.sName);
      oFieldMoney.setHint(oFieldType.sName);
      oFieldMoney.setValue(oPrice.iValue);

      String tCurrencies[] = new String[alCurrencies.size()];
      for(int i = 0; i < alCurrencies.size(); i++)
      {
         tCurrencies[i] = alCurrencies.get(i).sValue;
         if(oPrice != null && oPrice.iCurrencyID == alCurrencies.get(i).iID)
            iSelected = i;
      }
      
      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, tCurrencies);
      oFieldMoney.setAdapter(oArrayAdapter);
      oFieldMoney.setSelection(iSelected);

      oFieldMoney.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
         {
            oPrice.iCurrencyID = alCurrencies.get(pos).iID;
            ((Changeable<String>) oFieldMoney.getTag()).value = oPrice.toString();
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent)
         {
         }
      });
      
      oFieldMoney.setUpdateListener(new EditTextX.OnUpdateListener()
      {
         
         @Override
         public void onUpdate(EditText et)
         {
            String sValue = et.getText().toString();
            sValue = sValue.replace(" ", "");
            sValue = sValue.replace("-,", "-0,");
            int iValue;
            if(sValue.isEmpty() || sValue.matches("-|,|-,"))
               iValue = 0;
            else
            {
               String [] tsValue = sValue.split("\\" + DBAdapter.separator);
//               String [] tsValue = sValue.split("\\.");
                
               iValue = (tsValue[0].isEmpty() ? 0 : Integer.valueOf(tsValue[0])*100) + (tsValue.length == 2 ? (sValue.contains("-") ? -1 : 1) * (tsValue[1].length() == 1 ? 10 : 1) * Integer.valueOf(tsValue[1]) : 0);
            }
            
            oPrice.iValue = iValue;
            ((Changeable<String>) oFieldMoney.getTag()).value = oPrice.toString();
            
         }
      });
      
      rootView.addView(oFieldMoney);
   }
   
   private void addFieldDate(ViewGroup rootView, FieldType oFieldType)
   {
      Date date;
      
      FieldDate oFieldDate = null;
      
      switch(oFieldType.iID)
      {
         case DBAdapter.FLD_READ_DATE:
            date = new Date(oBook.ciReadDate.value);
            oFieldDate = new FieldDate(this);
            oFieldDate.setTag(oBook.ciReadDate);
         break;
         
         case DBAdapter.FLD_DUE_DATE:
            date = new Date(oBook.ciDueDate.value);
            oFieldDate = new FieldDate(this);
            oFieldDate.setTag(oBook.ciDueDate);
            
         break;
         
         default:
            return;
            
      }
      
      oFieldDate.setTitle(oFieldType.sName);
      oFieldDate.setHint(oFieldType.sName);
      oFieldDate.setDate(date);
      
      oFieldDate.setUpdateListener(new FieldDate.OnUpdateListener()
      {
         @Override
         public void onUpdate(Date date)
         {
         }

         @Override
         public void onUpdate(FieldDate oFieldDate)
         {
            ((Changeable<Integer>) oFieldDate.getTag()).value = oFieldDate.getDate().toInt();
         }
      });
      rootView.addView(oFieldDate);      
   }
   
   public class ArrayFieldsAdapter extends ArrayAdapter<Field> 
   {
      private final String MY_DEBUG_TAG = "ArrayFieldsAdapter";
      private ArrayList<Field> items;
      private ArrayList<Field> itemsAll;
      private ArrayList<Field> suggestions;
      private int viewResourceId;

      public ArrayFieldsAdapter(Context context, int viewResourceId, ArrayList<Field> items) {
          super(context, viewResourceId, items);
          this.items = items;
          this.itemsAll = (ArrayList<Field>) items.clone();
          this.suggestions = new ArrayList<Field>();
          this.viewResourceId = viewResourceId;
      }

      public View getView(int position, View convertView, ViewGroup parent) 
      {
         TextView view = (TextView) super.getView(position, convertView, parent);
         // Replace text with my own
         view.setText(getItem(position).sValue);
         return view;         
         
      }


      @Override
      public Filter getFilter() 
      {
          return nameFilter;
      }

      Filter nameFilter = new Filter() 
      {
         @Override
         public String convertResultToString(Object resultValue) 
         {
            String str = ((Field)(resultValue)).sValue; 
            return str;
         }
         
         @Override
         protected FilterResults performFiltering(CharSequence constraint) 
         {
            if(constraint != null) 
            {
               suggestions.clear();
               for (Field oField : itemsAll) 
               {
                  if(oField.sValue.toLowerCase().startsWith(constraint.toString().toLowerCase()))
                  {
                     suggestions.add(oField);
                  }
               }
               
               FilterResults filterResults = new FilterResults();
               filterResults.values = suggestions;
               filterResults.count = suggestions.size();
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
            ArrayList<Field> filteredList = (ArrayList<Field>) results.values;
            if(results != null && results.count > 0) 
            {
               clear();
               for (Field c : filteredList) 
               {
                  add(c);
               }
               
               notifyDataSetChanged();
            }
         }
      };
   }

}
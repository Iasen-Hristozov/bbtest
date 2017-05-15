package com.discworld.booksbag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.InputType;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import java.util.List;

public class EditBookActivityOld extends AppCompatActivity implements MultiSpinner.multispinnerListener
{
   public final static String BOOK_ID = "book_id";

   private Book oBook;
   private EditText etTitle,
                    etDescription;

   private LinearLayout llAuthors;
   private Button btnShowPopup;
   private DBAdapter oDbAdapter = null;
   private ArrayAdapter<String> adapter;
   String tAuthors[];

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
//      setContentView(R.layout.activity_edit_book_old);
      setContentView(R.layout.activity_edit_book_old);
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


      actionBar.getCustomView().findViewById(R.id.actionbar_done).setOnClickListener(
            new View.OnClickListener()
            {
               @Override
               public void onClick(View v)
               {
                  saveBook();
                  setResult(RESULT_OK, new Intent());
                  finish();                  // "Done"
               }
            });

      etTitle = (EditText) findViewById(R.id.et_title);
      etDescription = (EditText) findViewById(R.id.et_description);
      if(iBookID != 0)
      {
//         oBook = DummyContent.BOOKS_MAP.get(iBookID);
         oBook = oDbAdapter.getBook(iBookID);
         loadBook();
      }
      else
         oBook = new Book();

      ArrayList<Field> alAuthors = oDbAdapter.getFieldValues(DBAdapter.FLD_AUTHOR);
      tAuthors = new String[alAuthors.size()];
      for(int i = 0; i < alAuthors.size(); i++)
         tAuthors[i] = alAuthors.get(i).sValue;
      
//      String tAuthors[] = new String[DummyContent.AUTHORS.size()];
//      for(int i = 0; i < DummyContent.AUTHORS.size(); i++)
//         tAuthors[i] = DummyContent.AUTHORS.get(i).sValue;

      adapter = new ArrayAdapter<String> (this,android.R.layout.select_dialog_item, tAuthors);  
      
      llAuthors = (LinearLayout) findViewById(R.id.ll_authors);
      findViewById(R.id.ib_add_author).setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            addAuthor(llAuthors);
         }
      });
      
      initAuthors(llAuthors, oBook.alFields);
      
//      addAuthor(llAuthors);
      
      FieldEditTextUpdatableClearable fldTitle = new FieldEditTextUpdatableClearable(this);
      fldTitle.setTitle("Title");
      fldTitle.setText(oBook.sTitle);
      fldTitle.setHint("Title");
      fldTitle.setUpdateListener(new EditTextX.OnUpdateListener()
      {
         @Override
         public void onUpdate(EditText et)
         {
            oBook.sTitle = et.getText().toString();
         }
      });
      llAuthors.addView(fldTitle);

//      FieldAutoCompleteTextView  fldLanguage = new FieldAutoCompleteTextView(this);
//      fldLanguage.setTitle("Language");
//      fldLanguage.setText(oBook.sTitle);
//      fldLanguage.setHint("Language");
//      fldLanguage.setUpdateListener(new AutoCompleteTextViewX.OnUpdateListener()
//      {
//         @Override
//         public void onUpdate(EditText et)
//         {
////            oBook. = et.getText().toString();
//         }
//      });
//      
//      ArrayList<Field> alLanguages = oDbAdapter.getFieldValues(DBAdapter.FLD_LANGUAGE);
//      String tLanguages[] = new String[alLanguages.size()];
//      for(int i = 0; i < alLanguages.size(); i++)
//         tLanguages[i] = alLanguages.get(i).sValue;
//
//      ArrayAdapter<String> aaLanguages = new ArrayAdapter<String> (this,android.R.layout.select_dialog_item, tLanguages);  
//      fldLanguage.setThreshold(1);
//      fldLanguage.setAdapter(aaLanguages);
//      llAuthors.addView(fldLanguage);
      
      String ts[] = {oBook.sTitle};
      
//      addTextField(llAuthors, DBAdapter.FLD_TITLE, ts);
      addFieldText(llAuthors, DBAdapter.FLD_TITLE, oBook.csTitle);
      addFieldText(llAuthors, DBAdapter.FLD_VOLUME, oBook.ciVolume);
      addFieldText(llAuthors, DBAdapter.FLD_WEB, oBook.alFields.get(3));
//      addAutocompleteField(llAuthors, oBook.alFields.get(4));
//      addFieldSpinner(llAuthors, oBook.alFields.get(10));
      addFieldSpinner(llAuthors, DBAdapter.FLD_CONDITION);
//      addFieldMultiText(llAuthors, DBAdapter.FLD_AUTHOR);
      addFieldMultiSpinner(llAuthors, DBAdapter.FLD_CATEGORY);
//      addFieldValue(llAuthors, DBAdapter.FLD_PRICE);
//      addFieldMoney(llAuthors, DBAdapter.FLD_PRICE, oBook.csPrice);
      addFieldDate(llAuthors, DBAdapter.FLD_READ_DATE, oBook.ciReadDate);
      
      for(FieldType oFieldType: DBAdapter.FIELD_TYPES)
      {
         switch(oFieldType.iType)
         {
            case FieldType.TYPE_TEXT:
               addFieldText(llAuthors, oFieldType);
            break;
            
            case FieldType.TYPE_MULTIFIELD:
               addFieldMultiText(llAuthors, oFieldType);
            break;
            
            case FieldType.TYPE_TEXT_AUTOCOMPLETE:
               addAutocompleteField(llAuthors, oFieldType);
            break;
            
            case FieldType.TYPE_SPINNER:
               addFieldSpinner(llAuthors, oFieldType);
            break;
            
            case FieldType.TYPE_MULTI_SPINNER:
               addFieldMultiSpinner(llAuthors, oFieldType);
            break;
            
            case FieldType.TYPE_MONEY:
               addFieldMoney(llAuthors, oFieldType);
            break;
            
            case FieldType.TYPE_DATE:
               addFieldDate(llAuthors, oFieldType);
            break;
               
         }
      }
      
      
      MultiSpinner ms   = (MultiSpinner) findViewById(R.id.multi_spinner);
      List<String> list = new ArrayList<String>();
      list.add("one");
      list.add("two");
//      list.add("three");
//      list.add("four");
//      list.add("five");
//      list.add("six");
//      list.add("seven");
//      list.add("eight");
//      list.add("nine");
//      list.add("ten");
      ms.setItems(list, "select", this);
      
//      ms.setOnClickListener(new OnClickListener()
//      {
//         
//         @Override
//         public void onClick(View v)
//         {
//            displayPopupWindow(v);
//            
//         }
//      });

//      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//      setSupportActionBar(toolbar);

      btnShowPopup = (Button) findViewById(R.id.button1);
//      setButtonText(btnShowPopup, DummyContent.CATEGORIES);
      setButtonText(btnShowPopup, oDbAdapter.getFieldValues(DBAdapter.FLD_CATEGORY));
//      for(Field oField: DummyContent.CATEGORIES)
//      {
//         String sButtonText = "";
//         if(oBook.alFields.contains(oField))
//            sButtonText += (sButtonText.isEmpty() ? "" : ", ") + oField.sValue; 
//         if(!sButtonText.isEmpty())
//            btnShowPopup.setText(sButtonText);
//      }
      btnShowPopup.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          // Display popup attached to the button as a position anchor
           
//          displayPopupWindow1(v, oBook.alFields, DummyContent.CATEGORIES);
          displayPopupWindow1(v, oBook.alFields, oDbAdapter.getFieldValues(DBAdapter.FLD_CATEGORY));
        }
      });
   
//      Spinner spinner = (Spinner) findViewById(R.id.spinner1);
//      spinner.setOnClickListener(new OnClickListener()
//      {
//         
//         @Override
//         public void onClick(View v)
//         {
//            // TODO Auto-generated method stub
//            
//         }
//      });
               
   }

   private void addAuthor(LinearLayout llAuthors)
   {
      Field fldAuthor = new Field(DBAdapter.FLD_AUTHOR);
      oBook.alFields.add(fldAuthor);
      addAuthorFld(llAuthors, fldAuthor);
   }

   private void addAuthorFld(LinearLayout llAuthors, Field fldAuthor)
   {
      LayoutInflater oInflater = LayoutInflater.from(this);
      final View vRow = oInflater.inflate(R.layout.row_author, null);
      vRow.findViewById(R.id.ib_remove_author).setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            View vParent = (View) v.getParent();
            removeAuthor(vParent);
         }
      });

//      final EditTextUpdatable etAuthor = (EditTextUpdatable)vRow.findViewById(R.id.et_author);
//      etAuthor.setOnUpdateListener(new EditTextUpdatable.OnUpdateListener()
      final AutoCompleteTextViewX etAuthor = (AutoCompleteTextViewX)vRow.findViewById(R.id.et_author);
      etAuthor.setOnUpdateListener(new AutoCompleteTextViewX.OnUpdateListener()
      {
         @Override
         public void onUpdate(EditText et)
         {
            String sAuthor = et.getText().toString();
            ((Field) vRow.getTag()).sValue = et.getText().toString();
         }
      });
//      etAuthor.setOnFocusChangeListener(new View.OnFocusChangeListener()
//      {
//         @Override
//         public void onFocusChange(View v, boolean hasFocus)
//         {
//            if(!hasFocus)
//            {
//               updateAuthor((EditText)v);
//               etAuthorFocused = (EditText)v;
//            }
//            else
//            {
////               ((EditText)v).setHint("");
//               etAuthorFocused = (EditText)v;
//            }
//         }
//      });
      etAuthor.setAdapter(adapter);
      etAuthor.setThreshold(1);
      if(fldAuthor.iID != 0) // fldAuthor is not new 
         etAuthor.setText(fldAuthor.sValue);
      etAuthor.setOnItemClickListener(new OnItemClickListener()
      {
         @Override
         public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
         {
            String selection = (String) adapter.getItemAtPosition(position);
            int pos = -1;
            for (int i = 0; i < tAuthors.length; i++) {
                if (tAuthors[i].equals(selection)) {
                    pos = i;
                    break;
                }
            }
            System.out.println("Position " + pos); //check it now in Logcat            
         }
      });
      vRow.setTag(fldAuthor);
      llAuthors.addView(vRow);
      if(llAuthors.getChildCount() == 1)
         vRow.findViewById(R.id.ib_remove_author).setVisibility(View.INVISIBLE);
   }

   private void removeAuthor(View vParent)
   {
      Field fldAuthor = (Field) vParent.getTag();
      oBook.alFields.remove(fldAuthor);
      ViewGroup parent = (ViewGroup) vParent.getParent();
      parent.removeView(vParent);
//      llAuthors.removeView(vParent);
   }

   private void initAuthors(LinearLayout llAuthors, final ArrayList<Field> fldSelected)
   {
      for(Field oField : fldSelected)
      {
         if(oField.iTypeID == DBAdapter.FLD_AUTHOR)
            addAuthorFld(llAuthors, oField);
      }
   }

   private void updateAuthor(EditText etAuthor)
   {

      Field fldAuthor = (Field) ((View) etAuthor.getParent()).getTag();
      String sAuthor = etAuthor.getText().toString();
      fldAuthor.sValue = sAuthor;
//      ((Field) vRow.getTag()).sName = et.getText().toString();

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

   private void loadBook()
   {
      etTitle.setText(oBook.sTitle);
      etDescription.setText(oBook.sDescription);
   }

   @Override
   public void onBackPressed()
   {
      saveBook();
      setResult(RESULT_OK, new Intent());
      super.onBackPressed();
   }

   private void saveBook()
   {
      oBook.sTitle = etTitle.getText().toString();
      oBook.sDescription = etDescription.getText().toString();

      if(oBook.iID != 0)
         oDbAdapter.updateBook(oBook);
      else
         oDbAdapter.insertBook(oBook);
   }

   private void displayPopupWindow1(final View anchorView, final ArrayList<Field> fldSelected, final List<Field> fldDictionary)
   {
      final Context context = this;
      final PopupMenu popupMenu = new PopupMenu(this, anchorView);
      initPopupMenu(popupMenu, fldSelected, fldDictionary);
//    for(Field oField: fldDictionary)
      
//      for(int i = 0; i < fldDictionary.size(); i++)
//      {
////       popupMenu.getMenu().add(Menu.NONE, 0, 0, oField.sValue).setCheckable(true).setChecked(fldSelected.contains(oField));
//         Field oField = fldDictionary.get(i);
//         popupMenu.getMenu().add(Menu.NONE, i, 0, oField.sValue).setCheckable(true).setChecked(fldSelected.contains(oField));
//      }
//      popupMenu.getMenu().add(Menu.NONE, fldDictionary.size(), 0, "<add>");
    
//    popupMenu.getMenu().add(Menu.NONE, 1, 1, "Item 1").setCheckable(true).setActionView(R.layout.row_spinner).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//    popupMenu.getMenu().add(2, 2, 2, "Item 2").setCheckable(true);
//    popupMenu.getMenu().add(0, 0, 3, "").setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//    popupMenu.getMenu().add(3, 3, 3, "Item 3").setCheckable(true);
//    popupMenu.getMenu().add(4, 4, 4, "Item 4").setCheckable(true);
//    popupMenu.getMenu().add(5, 5, 5, "Item 5").setCheckable(true);
//    popupMenu.getMenu().add(6, 6, 6, "Item 5").setCheckable(true);
//    popupMenu.getMenu().add(6, 7, 7, "Item 5").setCheckable(true);
//    popupMenu.getMenu().add(6, 8, 8, "Item 5").setCheckable(true);
//    popupMenu.getMenu().add(6, 9, 9, "Item 5").setCheckable(true);
//    popupMenu.getMenu().add(6, 9, 9, "Item 5").setCheckable(true);
//    popupMenu.getMenu().add(6, 10, 10, "Item 5").setCheckable(true);
//    popupMenu.getMenu().setGroupCheckable(6, true, true);
//    popupMenu.inflate(R.menu.popup_menu);
      
      
//      popupMenu.setOnDismissListener(new OnDismissListener());
      popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener()
      {
         final int iFieldType = fldDictionary.get(0).iTypeID;   
         @Override
         public boolean onMenuItemClick(MenuItem menuItem)
         {
            if(menuItem.getItemId() < fldDictionary.size())
            {
               menuItem.setChecked(!menuItem.isChecked());
               if(menuItem.isChecked())
                  fldSelected.add(fldDictionary.get(menuItem.getItemId()));
               else
                  fldSelected.remove(fldDictionary.get(menuItem.getItemId()));
               
   //            for(int i = 0; i < popupMenu.getMenu().size(); i++)
   //            {
   ////               for(Field oField: fldSelected)
   ////                  if(oField.iTypeID == iFieldType)
   ////                     fldSelected.remove(oField);
   //               
   //               if(popupMenu.getMenu().getItem(i).isChecked())
   //                  fldSelected.add(fldDictionary.get(i));
   //               else
   //                  fldSelected.remove(fldDictionary.get(i));
   //            }
               
               setButtonText((Button)anchorView, fldDictionary);
               
               popupMenu.show();
            }
            else
            {
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setTitle(R.string.add_new);
               final EditText etNewValue = new EditText(context);
               builder.setView(etNewValue);
               builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     String sNewValue = etNewValue.getText().toString();
                     Field oField = new Field(iFieldType, sNewValue);
                     fldDictionary.add(oField);
                     fldSelected.add(oField);
                     setButtonText((Button)anchorView, fldDictionary);
                     initPopupMenu(popupMenu, fldSelected, fldDictionary);
//                     listitems.add(listitems.size()-1, sNewValue);
//                     boolean checkedOld[] = new boolean[checked.length];
//                     java.lang.System.arraycopy(checked,0, checkedOld, 0, checked.length);
//                     checked = new boolean[checked.length+1];
//                     java.lang.System.arraycopy(checkedOld, 0, checked, 0, checkedOld.length);
//                     customAdapter.notifyDataSetChanged();
                     InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                     dialog.cancel();
                     popupMenu.show();
                  }
               });

               builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
               {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     InputMethodManager imm = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                     dialog.cancel();
                     popupMenu.show();
                  }
               });

               builder.show();
               
            }
            return true;
//          //This will refer to the default, ascending or descending item.
//            MenuItem subMenuItem = menuItem.getSubMenu().getItem(menuItem.); 
//            //Check or uncheck it.
//            subMenuItem.setChecked(!subMenuItem.isChecked());            
            
         }
      });
      
      popupMenu.show();
   }
   
   private void displayPopupWindow(View anchorView) 
   {
//      PopupWindow popup = new PopupWindow(EditBookActivity.this);
//      View layout = getLayoutInflater().inflate(R.layout.row_spinner, null);
//      popup.setContentView(layout);
//      // Set content width and height
//      popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//      popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
//      // Closes the popup window when touch outside of it - when looses focus
//      popup.setOutsideTouchable(true);
//      popup.setFocusable(true);
//      // Show anchored to button
//      popup.setBackgroundDrawable(new BitmapDrawable());
//      popup.showAsDropDown(anchorView);
      
      int h = anchorView.getHeight();
      float y = anchorView.getY();
      
      
      final ArrayList<String> items = new ArrayList<String>();
      items.add("Item 1");
      items.add("Item 2");
      items.add("Item 3");
      items.add("Item 4");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      items.add("Item 5");
      
      LayoutInflater inflater = (LayoutInflater)EditBookActivityOld.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      LinearLayout layout2 = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));
      PopupWindow pw = new PopupWindow(layout2, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
      
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      int height = metrics.heightPixels;
      

      
      
//      PopupWindow pw = new PopupWindow(layout2);
      pw.setBackgroundDrawable(new BitmapDrawable());
      pw.setContentView(layout2);
      
      final ListView list = (ListView) layout2.findViewById(R.id.dropDownList);
      CustomAdapter adapter = new CustomAdapter(this, 0, 0, items);
      list.setAdapter(adapter);      
      
//      pw.showAsDropDown(btnShowPopup);
      if(y + h/2 > height/2)
      {
//         pw.showAtLocation(btnShowPopup, Gravity.TOP|Gravity.START|Gravity.LEFT, (int)btnShowPopup.getX(), (int)btnShowPopup.getY() - h);
//         pw.showAtLocation(btnShowPopup, Gravity.TOP, (int)btnShowPopup.getX(), (int)btnShowPopup.getY() - h);
         Rect location = locateView(anchorView);
//         pw.showAtLocation(btnShowPopup, Gravity.NO_GRAVITY, (int)btnShowPopup.getX(), (int)btnShowPopup.getY());
         LinearLayout llParent = (LinearLayout) findViewById(R.id.ll_parent);
         int[] loc = new int[2];
         anchorView.getLocationOnScreen(loc);
         int list_height = getListViewHeight(list);
//         pw.showAtLocation(llParent, Gravity.NO_GRAVITY, location.left, list_height);
//       pw.showAsDropDown(anchorView, 0, -(list_height+h));         
//       pw.showAsDropDown(anchorView, 0, -(location.top));
       pw.showAsDropDown(anchorView);
//       pw.isAboveAnchor()
//       pw.showAsDropDown(btnShowPopup, 0, -(list_height));
//         pw.showAtLocation(btnShowPopup, Gravity.TOP, 0, list_height);

//         pw.addOnLayoutChangeListener(new OnLayoutChangeListener()
//         {
//            
//         });
//         layout2.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
 
      }
      else
         pw.showAsDropDown(btnShowPopup);
      
      
//      layout2.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
//               MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//      int lw, lh;
//
//      lh =layout2.getMeasuredHeight();
//      lw = layout2.getMeasuredWidth();
//      
//      int list_height = getListViewHeight(list);
//      int a = 1;
//      int b = a;
//      layout2.on
   }
   
   private int getListViewHeight(ListView list) {
      ListAdapter adapter = list.getAdapter();

      int listviewHeight = 0;

      list.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED), 
                   MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

      listviewHeight = list.getMeasuredHeight() * adapter.getCount() + (adapter.getCount() * list.getDividerHeight());

      return listviewHeight;
}

   private void initPopupMenu(PopupMenu popupMenu, final ArrayList<Field> fldSelected, final List<Field> fldDictionary)
   {
      popupMenu.getMenu().clear();
      
      for(int i = 0; i < fldDictionary.size(); i++)
      {
//       popupMenu.getMenu().add(Menu.NONE, 0, 0, oField.sValue).setCheckable(true).setChecked(fldSelected.contains(oField));
         Field oField = fldDictionary.get(i);
         popupMenu.getMenu().add(Menu.NONE, i, 0, oField.sValue).setCheckable(true).setChecked(fldSelected.contains(oField));
      }
      popupMenu.getMenu().add(Menu.NONE, fldDictionary.size(), 0, "<add>");
      
   }
   
   private void setButtonText(Button oButton, List<Field> alFields)
   {
      String sButtonText = "";
      for(Field oField: alFields)
         if(oBook.alFields.contains(oField))
            sButtonText += (sButtonText.isEmpty() ? "" : ", ") + oField.sValue; 
      if(!sButtonText.isEmpty())
      {
         oButton.setText(sButtonText);
         oButton.setTextColor(Color.BLACK);
      }
      else
      {
         oButton.setText("select");
         oButton.setTextColor(Color.GRAY);
      }
   }
   
   public static Rect locateView(View v)
   {
       int[] loc_int = new int[2];
       if (v == null) return null;
       try
       {
           v.getLocationOnScreen(loc_int);
       } catch (NullPointerException npe)
       {
           //Happens when the view doesn't exist on screen anymore.
           return null;
       }
       Rect location = new Rect();
       location.left = loc_int[0];
       location.top = loc_int[1];
       location.right = location.left + v.getWidth();
       location.bottom = location.top + v.getHeight();
       return location;
   }
   
   private class CustomAdapter extends BaseAdapter
   {
      private Context context;
      private List<String> lsFields;
      LayoutInflater inflater;

      public CustomAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
      {
         this.context = context;
         this.lsFields = objects;
         inflater = ((Activity) context).getLayoutInflater();
      }

      @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent)
      {
         // TODO Auto-generated method stub
         return getCustomView(position, convertView, parent);
      }

      @Override
      public int getCount()
      {
         return lsFields.size();
      }

      @Override
      public Object getItem(int position)
      {
         return lsFields.get(position);
      }

      @Override
      public long getItemId(int position)
      {
         return position;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         // TODO Auto-generated method stub
         return getCustomView(position, convertView, parent);
      }

      public View getCustomView(final int position, View convertView, ViewGroup parent)
      {
         // TODO Auto-generated method stub

         View row;
         final ViewHolder holder;
         if(convertView == null)
         {
            row = inflater.inflate(R.layout.row_spinner, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) row.findViewById(R.id.tv_name);
            holder.cbSelected = (CheckBox) row.findViewById(R.id.cb_selected);
            holder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
               {
//                  checked[position] = !checked[position];
//                  setValues();
               }
            });

            row.setTag(holder);
         }
         else
         {
            row = (View) convertView;
            holder = (ViewHolder) row.getTag();
         }

         holder.tvName.setText(lsFields.get(position));
         if(position == lsFields.size()-1)
            holder.cbSelected.setVisibility(View.INVISIBLE);
         else
            holder.cbSelected.setVisibility(View.VISIBLE);

         return row;
      }

      class ViewHolder
      {
         TextView tvName;
         CheckBox cbSelected;
      }
   }

   private <T> void addFieldText(LinearLayout rootView, FieldType oFieldType)
   {
//      final FieldEditTextUpdatableClearable oField = new FieldEditTextUpdatableClearable(this);
//      final Changeable<T> cValue; 
      
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

   private void addFieldText(ViewGroup rootView, int iEnuFieldType, final Field f)
   {
      FieldType oFieldType = getFieldType(f.iTypeID);
//      FieldType oFieldType = null;
//      for(int i = 0; i < DBAdapter.FIELD_TYPES.size() && (oFieldType == null || oFieldType.iType != iEnuFieldType); i++)
//         oFieldType = DBAdapter.FIELD_TYPES.get(i);
      
      final FieldEditTextUpdatableClearable oField = new FieldEditTextUpdatableClearable(this);
      oField.setTitle(oFieldType.sName);
//      oField.setText(sFieldValue);
      oField.setText(f.sValue);
      oField.setHint(oFieldType.sName);
//      oField.setUpdateListener(new MyUpdateListener(sFieldValue));
//      oField.setTag(sFieldValue);
      oField.setTag(f);
      oField.setUpdateListener(new EditTextX.OnUpdateListener()
      {
         
         @Override
         public void onUpdate(EditText et)
         {
            ((Field) oField.getTag()).sValue = et.getText().toString(); 
//            String s = (String)oField.getTag();
//            s = et.getText().toString();
//            oField.setTag(s);
////            ((String)oField.getTag()) = "aaa";
////            s.
         }
      });
      rootView.addView(oField);      
   }
   
   private void addFieldText(ViewGroup rootView, int iEnuType,  final String [] sValue)
   {
      FieldType oFieldType = getFieldType(iEnuType);
      
      final FieldEditTextUpdatableClearable oField = new FieldEditTextUpdatableClearable(this);
      oField.setTitle(oFieldType.sName);
//      oField.setText(sFieldValue);
      oField.setText(sValue[0]);
//      sValue[0] = "aaaa";
      oField.setHint(oFieldType.sName);
//      oField.setUpdateListener(new MyUpdateListener(sFieldValue));
//      oField.setTag(sFieldValue);
//      oField.setTag(f);
      oField.setUpdateListener(new EditTextX.OnUpdateListener()
      {
         
         @Override
         public void onUpdate(EditText et)
         {
            sValue[0] = et.getText().toString();
//            ((Field) oField.getTag()).sValue = et.getText().toString(); 
//            String s = (String)oField.getTag();
//            s = et.getText().toString();
//            oField.setTag(s);
////            ((String)oField.getTag()) = "aaa";
////            s.
         }
      });
      rootView.addView(oField);      
   }
   
   private <T> void addFieldText(ViewGroup rootView, FieldType oFieldType,  final Changeable<T> cValue)
   {
      final FieldEditTextUpdatableClearable oField = new FieldEditTextUpdatableClearable(this);
      oField.setTitle(oFieldType.sName);
//      oField.setText(sFieldValue);
      oField.setText(cValue.toString());
//      sValue[0] = "aaaa";
      oField.setHint(oFieldType.sName);
      oField.setInputType(oFieldType.iInputType);
//      if(oFieldType.isMultiline)
//         oField.setMultiline();
//      oField.setUpdateListener(new MyUpdateListener(sFieldValue));
//      oField.setTag(sFieldValue);
//      oField.setTag(f);
      final T a;
      oField.setUpdateListener(new EditTextX.OnUpdateListener()
      {
         
         @Override
         public void onUpdate(EditText et)
         {
//            String s = "1";
//            Integer ii = Integer.valueOf(s);
            Class<?> c = cValue.getGenericType();
            T t = null;
//            Class<?> a = c.forName(className);
//            Class.forName(c.getName()) a;
            
            Class<?> clazz;
            Object object;
            try
            {
               clazz = Class.forName(c.getName());
               Constructor<?> ctor = clazz.getConstructor(String.class);
//               Constructor<?> ctor = clazz.getDeclaredConstructor();
               object = ctor.newInstance(et.getText().toString());

            } catch(ClassNotFoundException 
                    | NoSuchMethodException
                    | SecurityException
                    | InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException e)
            {
               // TODO Auto-generated catch block
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
   
   private <T> void addFieldText(ViewGroup rootView, int iEnuType,  final Changeable<T> cValue)
   {
      FieldType oFieldType = getFieldType(iEnuType);
      addFieldText(rootView, oFieldType,  cValue);
   }
   
//   private void addAutocompleteField(ViewGroup rootView, final Field f)
//   {
//      FieldType oFieldType = getFieldType(f.iTypeID);
//      int iSelected = -1;
//      
//      final FieldAutoCompleteTextView oFieldAutoCompleteTextView = new FieldAutoCompleteTextView(this);
//      oFieldAutoCompleteTextView.setTitle(oFieldType.sName);
//      oFieldAutoCompleteTextView.setHint(oFieldType.sName);
//      if(f != null && !f.sValue.isEmpty())
//         oFieldAutoCompleteTextView.setText(f.sValue);
//      oFieldAutoCompleteTextView.setTag(f);
//
//      final ArrayList<Field> alFields = oDbAdapter.getFieldValues(f.iTypeID);
//      final String tFieldValues[] = new String[alFields.size()];
//      for(int i = 0; i < alFields.size(); i++)
//      {
//         tFieldValues[i] = alFields.get(i).sValue;
//         if(f != null && f.iID == alFields.get(i).iID)
//            iSelected = i;
//      }
//
////    ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);
//      ArrayFieldsAdapter oArrayAdapter = new ArrayFieldsAdapter(this, android.R.layout.select_dialog_item, alFields);
//      oFieldAutoCompleteTextView.setAdapter(oArrayAdapter);
//      oFieldAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener()
//      {
//         @Override
//         public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
//         {
//            Field fldSelected = (Field)adapter.getItemAtPosition(position);
//            ((Field)oFieldAutoCompleteTextView.getTag()).copy(fldSelected);
////            String selection = (String) adapter.getItemAtPosition(position);
//////            int pos = -1;
////            for (int i = 0, pos = -1; i < tAuthors.length && pos == -1; i++) 
////            {
////               if (tFieldValues[i].equals(selection)) 
////               {
////                  pos = i;
////                  ((Field)oFieldAutoCompleteTextView.getTag()).copy(alFields.get(pos));
////               }
////            }
//         }
//      });
//      oFieldAutoCompleteTextView.setUpdateListener(new AutoCompleteTextViewX.OnUpdateListener()
//      {
//         @Override
//         public void onUpdate(EditText et)
//         {
//            ((Field)oFieldAutoCompleteTextView.getTag()).sValue = et.getText().toString();
//         }
//      });
//      
//      rootView.addView(oFieldAutoCompleteTextView);
//   }
   
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

//    int iSelected = -1;
  
//    final String tFieldValues[] = new String[alFieldValues.size()];
//    for(int i = 0; i < alFieldValues.size(); i++)
//    {
//       tFieldValues[i] = alFieldValues.get(i).sValue;
//       if(oField.iID == alFieldValues.get(i).iID)
//          iSelected = i;
//    }
//    if(iSelected != -1)
//       oFieldAutoCompleteTextView.setText(oField.sValue);

    
//  ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);
    ArrayFieldsAdapter oArrayAdapter = new ArrayFieldsAdapter(this, android.R.layout.select_dialog_item, alFieldValues);
    oFieldAutoCompleteTextView.setAdapter(oArrayAdapter);
    oFieldAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener()
    {
       @Override
       public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
       {
          Field fldSelected = (Field)adapter.getItemAtPosition(position);
          ((Field)oFieldAutoCompleteTextView.getTag()).copy(fldSelected);
//          String selection = (String) adapter.getItemAtPosition(position);
////          int pos = -1;
//          for (int i = 0, pos = -1; i < tAuthors.length && pos == -1; i++) 
//          {
//             if (tFieldValues[i].equals(selection)) 
//             {
//                pos = i;
//                ((Field)oFieldAutoCompleteTextView.getTag()).copy(alFields.get(pos));
//             }
//          }
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
   
//   private void addAutocompleteField(ViewGroup rootView, FieldType oFieldType)
//   {
//      final ArrayList<Field> alFieldValues = oDbAdapter.getFieldValues(oFieldType.iID);
//      
//      Field oField = new Field(oFieldType.iID);
//      for(int i = 0; oField == null || i < oBook.alFields.size(); i++)
//         if(oFieldType.iID == oBook.alFields.get(i).iTypeID)
//            oField.copy(oBook.alFields.get(i));
//
//      final FieldAutoCompleteTextView oFieldAutoCompleteTextView = new FieldAutoCompleteTextView(this, oField, alFieldValues);
//      oFieldAutoCompleteTextView.setTitle(oFieldType.sName);
//      oFieldAutoCompleteTextView.setHint(oFieldType.sName);
//      
//      if(oField.iID != 0)
//         oFieldAutoCompleteTextView.setText(oField.sValue);
//      oFieldAutoCompleteTextView.setTag(oField);
//
//      oFieldAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener()
//      {
//         @Override
//         public void onItemClick(AdapterView<?> adapter, View view, int position, long rowId)
//         {
//            Field fldSelected = (Field)adapter.getItemAtPosition(position);
//            ((Field)oFieldAutoCompleteTextView.getTag()).copy(fldSelected);
//         }
//      });
//      oFieldAutoCompleteTextView.setUpdateListener(new AutoCompleteTextViewX.OnUpdateListener()
//      {
//         @Override
//         public void onUpdate(EditText et)
//         {
//            ((Field)oFieldAutoCompleteTextView.getTag()).sValue = et.getText().toString();
//         }
//      });
//      
//      rootView.addView(oFieldAutoCompleteTextView);
//   }   
   
   private void addFieldSpinner(ViewGroup rootView, final Field f)
   {
      FieldType oFieldType = getFieldType(f.iTypeID);
      addFieldSpinner(rootView, oFieldType);
   }
   
   private void addFieldSpinner(ViewGroup rootView, int iEnuType)
   {
      FieldType oFieldType = getFieldType(iEnuType);
      addFieldSpinner(rootView, oFieldType);
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
   
   private void addFieldMultiSpinner(ViewGroup rootView, int iEnuType)
   {
      FieldType oFieldType = getFieldType(iEnuType);

      addFieldMultiSpinner(rootView, oFieldType);
      
   }
   
   private void addFieldMultiSpinner(ViewGroup rootView, final FieldType oFieldType)
   {
      final ArrayList<Field> alFieldValues = oDbAdapter.getFieldValues(oFieldType.iID);
//      final FieldMultiSpinner oFieldMultiSpinner = new FieldMultiSpinner(this, oBook.alFields, alFieldValues);
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
   
//   private void addFieldMoney(ViewGroup rootView, int iEnuType)
//   {
//      final FieldType oFieldType = getFieldType(iEnuType);
//      Field fldValue = null;
//      int iSelected = 0;
//      final FieldMoney oFieldValue = new FieldMoney(this);
//      oFieldValue.setTitle(oFieldType.sName);
//      
////      for(Field fldTemp : oBook.alFields)
////      {
////         if(fldTemp.iTypeID == oFieldType.iID)
////         {
////            fldValue = fldTemp;
////            break;
////         }
////      }
//      
////      Price oPrice = fldValue != null ?  new Price(fldValue.sValue) : null;
//      Price oPrice = null;
//
//      switch(iEnuType)
//      {
//         case DBAdapter.FLD_PRICE:
////            oFieldValue.setTag(oBook.sPrice);
//            oPrice = new Price(oBook.sPrice); 
//         break;
//         
//         case DBAdapter.FLD_VALUE:
////            oFieldValue.setTag(oBook.sValue);
//            oPrice = new Price(oBook.sValue);
//         break;
//      }
//      
//      final ArrayList<Field> alCurrencies = oDbAdapter.getFieldValues(DBAdapter.FLD_CURRENCY);
//      String tCurrencies[] = new String[alCurrencies.size()];
//      for(int i = 0; i < alCurrencies.size(); i++)
//      {
//         tCurrencies[i] = alCurrencies.get(i).sValue;
//         if(oPrice != null && oPrice.iCurrencyID == alCurrencies.get(i).iID)
//            iSelected = i;
//      }
//      
//
////      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);  
//      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, tCurrencies);
////      ArrayAdapter oArrayAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
//      oFieldValue.setAdapter(oArrayAdapter);
//      oFieldValue.setSelection(iSelected);
////      oFieldValue.setTag(fldValue);
////      String a  = "1234";
////              
////      oFieldValue.setTag(a);
////      
////      a = "aaa";
//
//      oFieldValue.setValue(oPrice.iValue);
//      oFieldValue.setTag(oPrice);
//      oFieldValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//      {
//         @Override
//         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
//         {
////            f.copy(alFieldsValues.get(pos));
//            ((Price) oFieldValue.getTag()).iCurrencyID = alCurrencies.get(pos).iID;
//            switch(oFieldType.iID)
//            {
//               case DBAdapter.FLD_PRICE:
////                oFieldValue.setTag(oBook.sPrice);
//                  oBook.sPrice = ((Price) oFieldValue.getTag()).toString(); 
//               break;
//             
//               case DBAdapter.FLD_VALUE:
////                oFieldValue.setTag(oBook.sValue);
//                  oBook.sValue = ((Price) oFieldValue.getTag()).toString(); 
//               break;
//            }            
//         }
//
//         @Override
//         public void onNothingSelected(AdapterView<?> parent)
//         {
//            // TODO Auto-generated method stub
//            
//         }
//      });
//      
//      oFieldValue.setUpdateListener(new EditTextX.OnUpdateListener()
//      {
//         
//         @Override
//         public void onUpdate(EditText et)
//         {
//            String sValue = et.getText().toString();
//            sValue = sValue.replace(" ", "");
//            sValue = sValue.replace("-,", "-0,");
//            int iValue;
//            if(sValue.isEmpty() || sValue.matches("-|,|-,"))
//               iValue = 0;
//            else
//            {
//               String [] tsValue = sValue.split("\\" + DBAdapter.separator);
////               String [] tsValue = sValue.split("\\.");
//                
//               iValue = (tsValue[0].isEmpty() ? 0 : Integer.valueOf(tsValue[0])*100) + (tsValue.length == 2 ? (sValue.contains("-") ? -1 : 1) * (tsValue[1].length() == 1 ? 10 : 1) * Integer.valueOf(tsValue[1]) : 0);
//            }
//            
//            ((Price) oFieldValue.getTag()).iValue = iValue;
//            switch(oFieldType.iID)
//            {
//               case DBAdapter.FLD_PRICE:
////                oFieldValue.setTag(oBook.sPrice);
//                  oBook.sPrice = ((Price) oFieldValue.getTag()).toString(); 
//               break;
//             
//               case DBAdapter.FLD_VALUE:
////                oFieldValue.setTag(oBook.sValue);
//                  oBook.sValue = ((Price) oFieldValue.getTag()).toString(); 
//               break;
//            }
//         }
//      });      
//      
//      rootView.addView(oFieldValue);
//   }   
   
//   private void setFieldMoney(String sField, String sValue)
//   {
//      sField = sValue;
//   }
//
//   
//   private void addFieldMoney(ViewGroup rootView, int iEnuType, final Changeable<String> csMoney)
//   {
//      final FieldType oFieldType = getFieldType(iEnuType);
//      Field fldValue = null;
//      int iSelected = 0;
//      final FieldMoney oFieldValue = new FieldMoney(this);
//      oFieldValue.setTitle(oFieldType.sName);
//      
////      for(Field fldTemp : oBook.alFields)
////      {
////         if(fldTemp.iTypeID == oFieldType.iID)
////         {
////            fldValue = fldTemp;
////            break;
////         }
////      }
//      
////      Price oPrice = fldValue != null ?  new Price(fldValue.sValue) : null;
//      Price oPrice = null;
//
//      oPrice = new Price(csMoney.value);
//      
////      switch(iEnuType)
////      {
////         case DBAdapter.FLD_PRICE:
//////            oFieldValue.setTag(oBook.sPrice);
////            oPrice = new Price(oBook.sPrice); 
////         break;
////         
////         case DBAdapter.FLD_VALUE:
//////            oFieldValue.setTag(oBook.sValue);
////            oPrice = new Price(oBook.sValue);
////         break;
////      }
//      
//      final ArrayList<Field> alCurrencies = oDbAdapter.getFieldValues(DBAdapter.FLD_CURRENCY);
//      String tCurrencies[] = new String[alCurrencies.size()];
//      for(int i = 0; i < alCurrencies.size(); i++)
//      {
//         tCurrencies[i] = alCurrencies.get(i).sValue;
//         if(oPrice != null && oPrice.iCurrencyID == alCurrencies.get(i).iID)
//            iSelected = i;
//      }
//      
//
////      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, tFieldValues);  
//      ArrayAdapter<String> oArrayAdapter = new ArrayAdapter<String> (this, R.layout.spinner_item, tCurrencies);
////      ArrayAdapter oArrayAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
//      oFieldValue.setAdapter(oArrayAdapter);
//      oFieldValue.setSelection(iSelected);
////      oFieldValue.setTag(fldValue);
////      String a  = "1234";
////              
////      oFieldValue.setTag(a);
////      
////      a = "aaa";
//
//      oFieldValue.setValue(oPrice.iValue);
//      oFieldValue.setTag(oPrice);
//      oFieldValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//      {
//         @Override
//         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
//         {
////            f.copy(alFieldsValues.get(pos));
//            ((Price) oFieldValue.getTag()).iCurrencyID = alCurrencies.get(pos).iID;
//            csMoney.value = ((Price) oFieldValue.getTag()).toString();
//         }
//
//         @Override
//         public void onNothingSelected(AdapterView<?> parent)
//         {
//            // TODO Auto-generated method stub
//            
//         }
//      });
//      
//      oFieldValue.setUpdateListener(new EditTextX.OnUpdateListener()
//      {
//         
//         @Override
//         public void onUpdate(EditText et)
//         {
//            String sValue = et.getText().toString();
//            sValue = sValue.replace(" ", "");
//            sValue = sValue.replace("-,", "-0,");
//            int iValue;
//            if(sValue.isEmpty() || sValue.matches("-|,|-,"))
//               iValue = 0;
//            else
//            {
//               String [] tsValue = sValue.split("\\" + DBAdapter.separator);
////               String [] tsValue = sValue.split("\\.");
//                
//               iValue = (tsValue[0].isEmpty() ? 0 : Integer.valueOf(tsValue[0])*100) + (tsValue.length == 2 ? (sValue.contains("-") ? -1 : 1) * (tsValue[1].length() == 1 ? 10 : 1) * Integer.valueOf(tsValue[1]) : 0);
//            }
//            
//            ((Price) oFieldValue.getTag()).iValue = iValue;
//            csMoney.value = ((Price) oFieldValue.getTag()).toString();
////            switch(oFieldType.iID)
////            {
////               case DBAdapter.FLD_PRICE:
//////                oFieldValue.setTag(oBook.sPrice);
////                  oBook.sPrice = ((Price) oFieldValue.getTag()).toString(); 
////               break;
////             
////               case DBAdapter.FLD_VALUE:
//////                oFieldValue.setTag(oBook.sValue);
////                  oBook.sValue = ((Price) oFieldValue.getTag()).toString(); 
////               break;
////            }
//         }
//      });      
//      
//      rootView.addView(oFieldValue);
//   }
   
   private void addFieldDate (ViewGroup rootView, int iEnuType, final Changeable<Integer> ciDate)
   {
      FieldType oFieldType = getFieldType(iEnuType);
      
      Date date = new Date(ciDate.value); 
      
      final FieldDate oField = new FieldDate(this);
      oField.setTitle(oFieldType.sName);
      oField.setHint(oFieldType.sName);
      oField.setDate(date);
      oField.setTag(ciDate);
      oField.setUpdateListener(new FieldDate.OnUpdateListener()
      {
         @Override
         public void onUpdate(Date date)
         {
            ((Changeable<Integer>) oField.getTag()).value = date.toInt();
         }

         @Override
         public void onUpdate(FieldDate oFieldDate)
         {
            // TODO Auto-generated method stub
            
         }
      });
      rootView.addView(oField);      
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
   
   
   private FieldType getFieldType(int iEnuFieldType)
   {
      FieldType oFieldType = null;
      
      for(int i = 0; i < DBAdapter.FIELD_TYPES.size() && (oFieldType == null || oFieldType.iID != iEnuFieldType); i++)
         oFieldType = DBAdapter.FIELD_TYPES.get(i);
      
      return oFieldType;
   }
   
   private class MyUpdateListener implements EditTextX.OnUpdateListener
   {
      String sFieldValue;
      MyUpdateListener(String sFiledValue)
      {
         this.sFieldValue = sFiledValue;
      }
      @Override
      public void onUpdate(EditText et)
      {
         sFieldValue = et.getText().toString();
      }
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
//          View v = convertView;
//          if (v == null) {
//              LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//              v = vi.inflate(viewResourceId, null);
//          }
//          Field oField = items.get(position);
//          if (oField != null) {
//              TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
//              if (customerNameLabel != null) {
////                Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
//                  customerNameLabel.setText(oField.sValue);
//              }
//          }
//          return v;
//      }

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

   private static boolean hasBookFieldOfType(Book oBook, int iEnuType)
   {
      for(Field oField : oBook.alFields)
         if(oField.iTypeID == iEnuType)
            return true;
      return false;
   }

   
}

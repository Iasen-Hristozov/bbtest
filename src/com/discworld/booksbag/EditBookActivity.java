package com.discworld.booksbag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.EditTextUpdatable;
import com.discworld.booksbag.dto.Field;
import com.discworld.booksbag.dto.MultiSpinner;

import java.util.ArrayList;
import java.util.List;

public class EditBookActivity extends AppCompatActivity implements MultiSpinner.multispinnerListener
{
   public final static String BOOK_ID = "book_id";

   private Book oBook;
   private EditText etTitle,
                    etDescription;
   private ImageButton ibAddAuthor;

   private LinearLayout llAuthors;
   private EditText etAuthorFocused;
   Button btnShowPopup;
   DBAdapter oDbAdapter = null;

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

      if(iBookID != 0)
      {
         /* TODO Error!!! Fixi it!*/
         oBook = oDbAdapter.getBook(iBookID);
         loadBook();
      }
      else
         oBook = new Book();

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
      llAuthors = (LinearLayout) findViewById(R.id.ll_authors);
      ((ImageButton) findViewById(R.id.ib_add_author)).setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            addAuthor(llAuthors);
         }
      });

      addAuthor(llAuthors);

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
      btnShowPopup.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          // Display popup attached to the button as a position anchor
          displayPopupWindow(v);
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
      Field fldAuthor = new Field(DBAdapter.FLD_AUT);
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

      final EditTextUpdatable etAuthor = (EditTextUpdatable)vRow.findViewById(R.id.et_author);
      etAuthor.setOnUpdateListener(new EditTextUpdatable.OnUpdateListener()
      {
         @Override
         public void onUpdate(EditText et)
         {
            String sAuthor = et.getText().toString();
            ((Field) vRow.getTag()).sName = et.getText().toString();
         }
      });
      etAuthor.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
         @Override
         public void onFocusChange(View v, boolean hasFocus)
         {
            if(!hasFocus)
            {
               updateAuthor((EditText)v);
               etAuthorFocused = (EditText)v;
            }
            else
            {
//               ((EditText)v).setHint("");
               etAuthorFocused = (EditText)v;
            }
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

   private void updateAuthor(EditText etAuthor)
   {

      Field fldAuthor = (Field) ((View) etAuthor.getParent()).getTag();
      String sAuthor = etAuthor.getText().toString();
      fldAuthor.sName = sAuthor;
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
      
      int h = btnShowPopup.getHeight();
      float y = btnShowPopup.getY();
      
      
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
      
      LayoutInflater inflater = (LayoutInflater)EditBookActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
       pw.showAsDropDown(btnShowPopup, 0, -(list_height+h));         
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

      public CustomAdapter(Context context, int resource, int textViewResourceId, List objects)
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
   
   private class MyPopupWindow extends PopupWindow
   {
//      onSi
   }
}

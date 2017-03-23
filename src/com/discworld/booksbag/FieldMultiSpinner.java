package com.discworld.booksbag;

import java.util.ArrayList;
import java.util.List;

import com.discworld.booksbag.dto.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FieldMultiSpinner extends LinearLayout
{
   private Title oTitle;
   private Button btnSpinner;
   private ArrayList<Field> alFields = null;
   private ArrayList<Field> alFieldValues = null;
//   private FieldType oFieldType;
//   private ArrayAdapter<?> adapter;
//   private int iEnuType = 0;
   private String hint = "";
   private String contentDescription = "";
   private Context context;
   
//   public FieldMultiSpinner(Context context, ArrayList<Field> alFields, FieldType oFieldType)
   public FieldMultiSpinner(Context context, ArrayList<Field> alFields, ArrayList<Field> alFieldValues)
   {
      super(context);
      
//      vInit(context, alFields, oFieldType);
      vInit(context, alFields, alFieldValues);
   }

//   public FieldMultiSpinner(Context context, AttributeSet attrs, ArrayList<Field> alFields, FieldType oFieldType)
   public FieldMultiSpinner(Context context, AttributeSet attrs, ArrayList<Field> alFields, ArrayList<Field> alFieldValues)
   {
      super(context, attrs);

//      vInit(context, alFields, oFieldType);
      vInit(context, alFields, alFieldValues);
      
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FieldMultiSpinner, 0, 0);
      
      String title = a.getString(R.styleable.FieldMultiSpinner_title);
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
   
//   void vInit(Context context, ArrayList<Field> alFields, FieldType oFieldType)
   void vInit(Context context, ArrayList<Field> alFields1, ArrayList<Field> alFieldValues1)
   {
      this.context = context;
      this.alFields = alFields1;
      this.alFieldValues = alFieldValues1;
//      this.oFieldType = oFieldType;
//      this.hint = oFieldType.sName;
//      this.hint = alFieldValues.get(0).sValue;
      
      
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      inflater.inflate(R.layout.field_multi_spinner, this, true);
      
      oTitle = (Title)this.findViewById(R.id.title);
      btnSpinner = (Button) this.findViewById(R.id.spinner);
      
      setButtonText(btnSpinner, alFieldValues);
      btnSpinner.setOnClickListener(new OnClickListener() 
      {
         @Override
         public void onClick(View v) 
         {
            displayPopupWindow(v, alFields, alFieldValues);
         }
      });
   }
   
   private void setButtonText(Button oButton, ArrayList<Field> alFieldValues)
   {
      String sButtonText = "";
      for(Field oFieldValue: alFieldValues)
         if(alFields.contains(oFieldValue))
            sButtonText += (sButtonText.isEmpty() ? "" : ", ") + oFieldValue.sValue; 
      if(!sButtonText.isEmpty())
      {
         oButton.setText(sButtonText);
         oButton.setTextColor(Color.BLACK);
      }
      else
      {
         oButton.setText(hint);
         oButton.setTextColor(Color.GRAY);
      }
   }
   
   private void displayPopupWindow(final View anchorView, final ArrayList<Field> alFields, final ArrayList<Field> alFieldValues)
   {
      final PopupMenu popupMenu = new PopupMenu(context, anchorView);
      initPopupMenu(popupMenu, alFields, alFieldValues);

      popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener()
      {
         final int iFieldType = alFieldValues.get(0).iTypeID;   
         @Override
         public boolean onMenuItemClick(MenuItem menuItem)
         {
            if(menuItem.getItemId() < alFieldValues.size())
            {
               menuItem.setChecked(!menuItem.isChecked());
               if(menuItem.isChecked())
                  alFields.add(alFieldValues.get(menuItem.getItemId()));
               else
                  alFields.remove(alFieldValues.get(menuItem.getItemId()));
               
               setButtonText((Button)anchorView, alFieldValues);
               
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
                     alFieldValues.add(oField);
                     alFields.add(oField);
                     setButtonText((Button)anchorView, alFieldValues);
                     initPopupMenu(popupMenu, alFields, alFieldValues);

                     InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                     dialog.cancel();
                     popupMenu.show();
                  }
               });

               builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() 
               {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                     dialog.cancel();
                     popupMenu.show();
                  }
               });

               builder.show();
               
            }
            return true;
         }
      });
      
      popupMenu.show();
   }   
   
   private void initPopupMenu(PopupMenu popupMenu, final ArrayList<Field> alFields, final List<Field> alFieldValues)
   {
      popupMenu.getMenu().clear();
      
      for(int i = 0; i < alFieldValues.size(); i++)
      {
//       popupMenu.getMenu().add(Menu.NONE, 0, 0, oField.sValue).setCheckable(true).setChecked(fldSelected.contains(oField));
         Field oField = alFieldValues.get(i);
         popupMenu.getMenu().add(Menu.NONE, i, 0, oField.sValue).setCheckable(true).setChecked(alFields.contains(oField));
      }
      popupMenu.getMenu().add(Menu.NONE, alFieldValues.size(), 0, "<add>");
      
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
      setButtonText(btnSpinner, alFieldValues);
   }
}

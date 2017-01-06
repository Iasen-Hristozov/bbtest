package com.discworld.booksbag.dto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class AutoCompleteTextViewUpdatable extends AutoCompleteTextView
{
   private OnUpdateListener onUpdateListener = null;
   
   private Context oContext;

   OnEditorActionListener oEditorActionListener = new OnEditorActionListener()
   {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
      {
         if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)
         {

            if(onUpdateListener != null)
               onUpdateListener.onUpdate((EditText) v);
            
            InputMethodManager inputManager = (InputMethodManager) oContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, 0);
            
            v.clearFocus();
            return true; 
         }
         return false;      
      }
   };

   public AutoCompleteTextViewUpdatable(final Context context)
   {
      super(context);
      
      vInit(context);
   }
   
   public AutoCompleteTextViewUpdatable(Context context, AttributeSet attrs)
   {
      super(context, attrs);

      vInit(context);
   }
   
   public AutoCompleteTextViewUpdatable(Context context, AttributeSet attrs, int defStyle)
   {
      super(context, attrs, defStyle);

      vInit(context);
   }

   private void vInit(Context context)
   {
      oContext = context;
      
      setOnEditorActionListener(oEditorActionListener);
   }
   
   @Override
   public boolean onKeyPreIme(int keyCode, KeyEvent event)
   {
      if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER) 
      {
         clearFocus();
         return false;
      }         
      return false;
   }

   public void setOnUpdateListener(OnUpdateListener onUpdateListener)
   {
      this.onUpdateListener = onUpdateListener;
   }

   public interface OnUpdateListener
   {
      void onUpdate(EditText et);
   }

}

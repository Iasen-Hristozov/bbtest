package com.discworld.booksbag.dto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.discworld.booksbag.R;

import java.util.List;

/**
 * Created by Iasen on 20.7.2016 г..
 */
public class MultiSpinnerOld extends Spinner implements DialogInterface.OnMultiChoiceClickListener,
      DialogInterface.OnCancelListener
{
   private List<String> listitems;
   private boolean[] checked;
   private CustomAdapter customAdapter;
   public MultiSpinnerOld(Context context)
   {
      super(context);
   }

   public MultiSpinnerOld(Context context, AttributeSet attrs)
   {
      super(context, attrs);
   }

   public MultiSpinnerOld(Context context, AttributeSet attrs, int defStyleAttr)
   {
      super(context, attrs, defStyleAttr);
   }

   @Override
   public void onCancel(DialogInterface dialog)
   {
//      String str="Selected values are: ";
//
//      for (int i = 0; i < listitems.size(); i++)
//      {
//         if (checked[i] == true)
//         {
//            str=str+"   "+listitems.get(i);
//         }
//
//      }
//
//      AlertDialog.Builder alert1 = new AlertDialog.Builder(getContext());
//
//      alert1.setTitle("Items:");
//
//      alert1.setMessage(str);
//
//      alert1.setPositiveButton("Ok", null);
//
//      alert1.show();
//      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                                                              android.R.layout.simple_spinner_item,
//                                                              new String[] { str });
//      setAdapter(adapter);
      // refresh text on spinner
      StringBuffer spinnerBuffer = new StringBuffer();
      boolean someUnselected = false;
      for (int i = 0; i < listitems.size(); i++)
      {
         if (checked[i] == true)
         {
            spinnerBuffer.append(listitems.get(i));
            spinnerBuffer.append(", ");
         }
         else
         {
            someUnselected = true;
         }
      }
      String spinnerText;
      if (someUnselected)
      {
         spinnerText = spinnerBuffer.toString();
         if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
      }
      else
      {
         spinnerText = "select";
      }

//      setPrompt("sssss");
//      spinnerText = "select1";

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                                              android.R.layout.simple_spinner_item,
                                                              new String[] { spinnerText });
      setAdapter(adapter);
   }

   void setValues()
   {
      StringBuffer spinnerBuffer = new StringBuffer();
      boolean someUnselected = false;
      for (int i = 0; i < listitems.size(); i++)
      {
         if (checked[i] == true)
         {
            spinnerBuffer.append(listitems.get(i));
            spinnerBuffer.append(", ");
         }
         else
         {
            someUnselected = true;
         }
      }
      String spinnerText;
      if (someUnselected)
      {
         spinnerText = spinnerBuffer.toString();
         if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
      }
      else
      {
         spinnerText = "select";
      }

//      setPrompt("sssss");
//      spinnerText = "select1";

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                                              android.R.layout.simple_spinner_item,
                                                              new String[] { spinnerText });
      setAdapter(adapter);
   }

   @Override
   public void onClick(DialogInterface dialog, int which, boolean isChecked)
   {
      if (isChecked)
         checked[which] = true;
      else
         checked[which] = false;
   }

   @Override
   public boolean performClick()
   {

      AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//      AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style
//            .AlertDialogCustom));
//      builder.setMultiChoiceItems(listitems.toArray(new CharSequence[listitems.size()]), checked, this);
      ListView lvItems = new ListView(getContext());
      customAdapter = new CustomAdapter(getContext(), 0, 0, listitems);
      lvItems.setAdapter(customAdapter);
      lvItems.setOnItemClickListener(new OnItemClickListener()
      {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id)
         {
            if(position == listitems.size() - 1)
            {
               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
               builder.setMessage("Alert")
                      .setTitle("Warning");
               final EditText etNewValue = new EditText(getContext());
               builder.setView(etNewValue);
               builder.setPositiveButton(android.R.string.ok, new DialogInterface
                     .OnClickListener
                     () {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     String sNewValue = etNewValue.getText().toString();
                     listitems.add(listitems.size()-1, sNewValue);
                     boolean  checkedOld[] = new boolean[checked.length];
                     System.arraycopy(checked,0, checkedOld, 0, checked.length);
                     checked = new boolean[checked.length+1];
                     System.arraycopy(checkedOld, 0, checked, 0, checkedOld.length);
                     customAdapter.notifyDataSetChanged();
                     InputMethodManager imm = (InputMethodManager)  getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                     View view = ((Activity)getContext()).getCurrentFocus();
//                     if (view != null)
//                     {
//                        InputMethodManager imm =
//                              (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                     }
                     dialog.cancel();
                  }
               });

               builder.setNegativeButton(android.R.string.cancel, new DialogInterface
                     .OnClickListener() {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     InputMethodManager imm = (InputMethodManager)  getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                     dialog.cancel();

                  }
               });

               builder.show();
            }

         }
      });
      builder.setView(lvItems);
//      builder.setItems(new CharSequence[]{"tttt"}, this);
      builder.setNegativeButton("done",
                                new DialogInterface.OnClickListener()
                                {

                                   @Override
                                   public void onClick(DialogInterface dialog, int which)
                                   {
                                      dialog.cancel();
                                   }
                                });
      builder.setOnCancelListener(this);
      builder.show();
//      AlertDialog ad = builder.create();
//      ad.show();
//      Button button = (Button) ad.findViewById(android.R.id.button2);
//      button.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
//      button.setPadding(20,0,0,0);

      return true;
   }

   public void setItems(List<String> items,
                        String allText,
                        multispinnerListener listener)
   {
      this.listitems = items;
      this.listitems.add("<add>");

      checked = new boolean[items.size()];
      for (int i = 0; i < checked.length; i++)
         checked[i] =false;


      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                                         android.R.layout.simple_spinner_item,
                                                         new String[] { allText });
//
//      CustomAdapter customAdapter = new CustomAdapter(getContext(), 0, 0, listitems);
      setAdapter(adapter);
//      setAdapter(customAdapter);
   }

   public interface multispinnerListener
   {
      public void onItemschecked(boolean[] checked);
   }

   private class CustomAdapter extends BaseAdapter
   {
      private Context context;
      private List<String> lsFields;
      LayoutInflater inflater;

      public CustomAdapter(Context context, int resource, int textViewResourceId, List objects)
      {
//         super(context, resource, textViewResourceId, objects);
         this.context = context;
         this.lsFields = objects;
         inflater = ((Activity) context).getLayoutInflater();
//         this.lsFields.add("<add>");
//
      }

//      public CustomAdapter(Context context, int resource)
//      {
//         super(context, resource);
//      }

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
         //return super.getView(position, convertView, parent);

//         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         View row;
//         View row = convertView;
         final ViewHolder holder;
         if(convertView == null)
         {
            row = new View(context);
            row = inflater.inflate(R.layout.row_spinner, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) row.findViewById(R.id.tv_name);
            holder.cbSelected = (CheckBox) row.findViewById(R.id.cb_selected);
            holder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
               {
                  checked[position] = !checked[position];
                  setValues();
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
         {
            holder.cbSelected.setVisibility(INVISIBLE);
//            row.setOnClickListener(new OnClickListener()
//            {
//               @Override
//               public void onClick(View v)
//               {
//                  AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                  builder.setMessage("Alert")
//                         .setTitle("Warning");
//                  final EditText etNewValue = new EditText(getContext());
//                  builder.setView(etNewValue);
//                  builder.setPositiveButton(android.R.string.ok, new DialogInterface
//                        .OnClickListener
//                        () {
//                     public void onClick(DialogInterface dialog, int id)
//                     {
//                        String sNewValue = etNewValue.getText().toString();
//                        listitems.add(listitems.size()-1, sNewValue);
//                        boolean  checkedOld[] = new boolean[checked.length];
//                        java.lang.System.arraycopy(checked,0, checkedOld, 0, checked.length);
//                        checked = new boolean[checked.length+1];
//                        java.lang.System.arraycopy(checkedOld, 0, checked, 0, checkedOld.length);
//                        customAdapter.notifyDataSetChanged();
//
//                        dialog.cancel();
//                     }
//                  });
//
//                  builder.setNegativeButton(android.R.string.cancel, new DialogInterface
//                        .OnClickListener() {
//                     public void onClick(DialogInterface dialog, int id)
//                     {
//                        dialog.cancel();
//                     }
//                  });
//
//                  builder.show();
//               }
//            });
         }
         else
         {
            holder.cbSelected.setVisibility(VISIBLE);
         }

         return row;
      }

      class ViewHolder
      {
         TextView tvName;
         CheckBox cbSelected;
      }
   }
}

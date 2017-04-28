package com.discworld.booksbag;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.discworld.booksbag.dto.Field;

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
//       View v = convertView;
//       if (v == null) {
//           LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//           v = vi.inflate(viewResourceId, null);
//       }
//       Field oField = items.get(position);
//       if (oField != null) {
//           TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
//           if (customerNameLabel != null) {
////             Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
//               customerNameLabel.setText(oField.sValue);
//           }
//       }
//       return v;
//   }

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
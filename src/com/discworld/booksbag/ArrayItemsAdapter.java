package com.discworld.booksbag;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.discworld.booksbag.fields.FieldMultiText.Item;

public class ArrayItemsAdapter extends ArrayAdapter<Item> 
{
   private final String MY_DEBUG_TAG = "ArrayFieldsAdapter";
   private ArrayList<? extends Item> items;
   private ArrayList<Item> itemsAll;
   private ArrayList<Item> suggestions;
   private int viewResourceId;

   public ArrayItemsAdapter(Context context, int viewResourceId, ArrayList<Item> alDictionaryFields1) 
   {
       super(context, viewResourceId, alDictionaryFields1);
       this.items = alDictionaryFields1;
       this.itemsAll = (ArrayList<Item>) alDictionaryFields1.clone();
       this.suggestions = new ArrayList<Item>();
       this.viewResourceId = viewResourceId;
   }

   public View getView(int position, View convertView, ViewGroup parent) 
   {
      TextView view = (TextView) super.getView(position, convertView, parent);
      // Replace text with my own
      view.setText(getItem(position).getValue());
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
         String str = ((Item)(resultValue)).getValue(); 
         return str;
      }
      
      @Override
      protected FilterResults performFiltering(CharSequence constraint) 
      {
         if(constraint != null) 
         {
            suggestions.clear();
            for (Item oField : itemsAll) 
            {
               if(oField.getValue().toLowerCase(Locale.getDefault()).startsWith(constraint.toString().toLowerCase()))
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
         ArrayList<Item> filteredList = (ArrayList<Item>) results.values;
         if(results != null && results.count > 0) 
         {
            clear();
            for (Item c : filteredList) 
            {
               add(c);
            }
            
            notifyDataSetChanged();
         }
      }
   };
}
package com.discworld.booksbag;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.Date;
import com.discworld.booksbag.dto.Field;
import com.discworld.booksbag.dto.FieldType;
import com.discworld.booksbag.dummy.DummyContent;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment
{
   /**
    * The fragment argument representing the item ID that this fragment
    * represents.
    */
   public static final String ARG_ITEM_ID = "item_id";

   /**
    * The dummy content this fragment is presenting.
    */
//   private DummyContent.DummyItem mItem;
   private Book mItem;
   private DBAdapter oDbAdapter = null;

   /**
    * Mandatory empty constructor for the fragment manager to instantiate the
    * fragment (e.g. upon screen orientation changes).
    */
   public BookDetailFragment()
   {
   }

   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      oDbAdapter = new DBAdapter(getActivity());

      if (getArguments().containsKey(ARG_ITEM_ID))
      {
         // Load the dummy content specified by the fragment
         // arguments. In a real-world scenario, use a Loader
         // to load content from a content provider.
//         mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//         mItem = DummyContent.BOOKS_MAP.get(getArguments().getLong(ARG_ITEM_ID));
         mItem = oDbAdapter.getBook(getArguments().getLong(ARG_ITEM_ID));

         Activity activity = this.getActivity();
         CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
         if (appBarLayout != null)
         {
//            appBarLayout.setTitle(mItem.content);
            appBarLayout.setTitle(mItem.sTitle);
         }
      }
   }

   @Override
   public void onPause()
   {
      oDbAdapter.close();
      
      super.onPause();
   }

   @Override
   public void onResume()
   {
      super.onResume();
      
      oDbAdapter.open();
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      View rootView = inflater.inflate(R.layout.book_detail, container, false);

      // Show the dummy content as text in a TextView.
      if (mItem != null)
      {
//         ((TextView) rootView.findViewById(R.id.book_detail)).setText(mItem.details);
//         ((TextView) rootView.findViewById(R.id.book_detail)).setText(mItem.sDescription);
         
         LinearLayout llCategories = (LinearLayout) rootView.findViewById(R.id.ll_categories);
         String sName = "", sValue = "";
         
         for(FieldType fieldType: DBAdapter.FIELD_TYPES)
         {
            if(fieldType.iID < 100)
            {
               for(Field oField: mItem.alFields)
               {
                  if(oField.iTypeID == fieldType.iID)
                  {
                     if(!sName.trim().isEmpty())
                        sValue += "\n" + oField.sValue;
                     else
                     {
                        sName = fieldType.sName + ":";
                        sValue = oField.sValue;
                     }
                  }
               }
//               if(!sName.trim().isEmpty())
//               {
//                  addField(llCategories, sName, sValue);
//                  sName = "";
//                  sValue = "";
//               }
            }
            else
            {
               switch(fieldType.iID)
               {
                  case DBAdapter.FLD_DESCRIPTION:
                     if(!mItem.sDescription.trim().isEmpty())
                     {
                        sName = "Description:";
                        sValue = mItem.sDescription;
                     }
                  break;
                  
                  case DBAdapter.FLD_VOLUME:
                     if(mItem.iVolume != 0)
                     {
                        sName = "Volume:";
                        sValue = String.valueOf(mItem.iVolume);
                     }
                  break;
                  
                  case DBAdapter.FLD_PUBLICATION_DATE:
                     if(mItem.iPublicationDate != 0)
                     {
                        sName = "Publication Date:";
                        sValue = String.valueOf(mItem.iPublicationDate);
                     }
                  break;
                  
                  case DBAdapter.FLD_PAGES:
                     if(mItem.iPages != 0)
                     {
                        sName = "Pages:";
                        sValue = String.valueOf(mItem.iPages);
                     }
                  break;
                  
                  case DBAdapter.FLD_PRICE:
                     if(mItem.iPrice != 0)
                     {
                        sName = "Price:";
                        String sPrice = String.format(getResources().getString(R.string.amn_vl), mItem.iPrice / 100, mItem.iPrice % 100);
                        sValue = String.valueOf(sPrice);
                     }
                  break;
                  
                  case DBAdapter.FLD_VALUE:
                     if(mItem.iValue != 0)
                     {
                        sName = "Value:";
                        String sBookValue = String.format(getResources().getString(R.string.amn_vl), mItem.iValue / 100, mItem.iValue % 100);
                        sValue = String.valueOf(sBookValue);
                     }
                  break;
                  
                  case DBAdapter.FLD_DUE_DATE:
                     if(mItem.iDueDate != 0)
                     {
                        sName = "Due Date:";
                        sValue = String.valueOf(new Date(mItem.iDueDate).toString());
                     }
                  break;
                  
                  case DBAdapter.FLD_READ_DATE:
                     if(mItem.iReadDate != 0)
                     {
                        sName = "Read Date:";
                        sValue = String.valueOf(new Date(mItem.iReadDate).toString());
                     }
                  break;
                  
                  case DBAdapter.FLD_EDITION:
                     if(mItem.iEdition != 0)
                     {
                        sName = "Edition:";
                        sValue = String.valueOf(mItem.iEdition);
                     }
                  break;
                  
                  case DBAdapter.FLD_ISBN:
                     if(!mItem.sISBN.trim().isEmpty())
                     {
                        sName = "ISBN:";
                        sValue = String.valueOf(mItem.sISBN);
                     }
                  break;
                  
                  case DBAdapter.FLD_WEB:
                     if(!mItem.sWeb.trim().isEmpty())
                     {
                        sName = "Web:";
                        sValue = String.valueOf(mItem.sWeb);
                     }
                  break;
               }
            }
            if(!sName.trim().isEmpty())
            {
               addField(llCategories, sName, sValue);
               sName = "";
               sValue = "";
            }
         }
      }

      return rootView;
   }

   private void addField(LinearLayout rootView, String sName, String sValue)
   {
      LayoutInflater oInflater = LayoutInflater.from(getActivity());
      
      View vRow = oInflater.inflate(R.layout.row_category, null);
      ((TextView) vRow.findViewById(R.id.tv_name)).setText(sName);
      ((TextView) vRow.findViewById(R.id.tv_value)).setText(sValue);
      
      rootView.addView(vRow);  
   }
}

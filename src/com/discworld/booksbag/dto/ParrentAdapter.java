/*package com.discworld.booksbag.dto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.discworld.booksbag.R;
import com.discworld.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

public class ParrentAdapter extends ExpandableRecyclerAdapter<ParentResult, Result, ParrentResultViewHolder, ResultViewHolder>
{
   private LayoutInflater mInflater;
   private List<ParentResult> lsParrentResult;

   private OnClickListener onClickListener = null;

   private OnLongClickListener onLongClickListener = null;

   public ParrentAdapter(Context context,
                         @NonNull List<ParentResult> parentList)
   {
      super(parentList);
      lsParrentResult = parentList;
      mInflater = LayoutInflater.from(context);

   }

   public void setClickListener(OnClickListener callback)
   {
      this.onClickListener = callback;
   }

   public void setLongClickListener(OnLongClickListener callback)
   {
      this.onLongClickListener = callback;
   }

   @UiThread
   @NonNull
   @Override
   public ParrentResultViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup,
                                                           int viewType)
   {
      View recipeView = mInflater.inflate(R.layout.parrent_view,
                                          parentViewGroup,
                                          false);
      return new ParrentResultViewHolder(recipeView);
   }

   @UiThread
   @NonNull
   @Override
   public ResultViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup,
                                                   int viewType)
   {
      View ingredientView = mInflater.inflate(R.layout.child_view,
                                              childViewGroup,
                                              false);

      return new ResultViewHolder(ingredientView);
   }

   @UiThread
   @Override
   public void onBindParentViewHolder(@NonNull ParrentResultViewHolder oParrentResultViewHolder,
                                      int parentPosition,
                                      @NonNull ParentResult oParrentResult)
   {
      oParrentResultViewHolder.bind(oParrentResult);
   }

   @UiThread
   @Override
   public void onBindChildViewHolder(@NonNull ResultViewHolder oResultViewHolder,
                                     int parentPosition,
                                     int childPosition,
                                     @NonNull Result oResult)
   {
      oResultViewHolder.bind(oResult);
      oResultViewHolder.oView.setOnClickListener(onClickListener);
      oResultViewHolder.oView.setOnLongClickListener(onLongClickListener);
   }

   public void filter(String arg0)
   {
      // TODO Auto-generated method stub

   }

   public void removeAt(int iClickedItemNdx)
   {
//       notifyChildRemoved(0,0);
      mFlatItemList.remove(iClickedItemNdx);
      notifyItemRemoved(1);
      // Result result = mFlatItemList.get(iClickedItemNdx).getChild();
      // mFlatItemList.get(iClickedItemNdx).getParent().
      // for(ParrentResult parrentResult: lsParrentResult)
      // {
      // parrentResult.getChildList().get
      // if(parrentResult.getChildList().contains(result))
      // {
      // parrentResult.getChildList().remove(result);
      // setParentList(lsParrentResult, true);
      // notifyItemRemoved(iClickedItemNdx);
      // notifyChildRemoved(int parentPosition, int childPosition)
      // break;
      // }
      // }
      //
   }

   public int getAllChildsCount()
   {
      int iChildsCount = 0;
      for(ParentResult oParrentResult : getParentList())
      {
         iChildsCount += oParrentResult.getChildList().size();
      }

      return iChildsCount;
   }

   @Override
   public long getItemId(int position)
   {
      if(getItemViewType(position) == ExpandableRecyclerAdapter.TYPE_CHILD)
         return mFlatItemList.get(position).getChild().id;
      else
         return -1;
   }

}*/
/*package com.discworld.booksbag.dto;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.discworld.booksbag.R;
import com.discworld.expandablerecyclerview.ChildViewHolder;

public class ResultViewHolder extends ChildViewHolder<Result> 
{

   public View oView;
   
   private TextView tvContent;

   public ResultViewHolder(@NonNull View itemView) 
   {
       super(itemView);
       tvContent = (TextView) itemView.findViewById(R.id.tv_content);
       oView = itemView;
   }

   public void bind(@NonNull Result result) 
   {
       tvContent.setText(result.content);
       oView.setTag(result);
   }
}*/
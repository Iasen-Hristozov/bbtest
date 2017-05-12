package com.discworld.booksbag.dto;

import com.discworld.booksbag.FieldMultiText.Item;

/**
 * Created by Iasen on 12.7.2016 г..
 */
public class Field implements Item
{
   public int iTypeID;
   public long iID = 0;
   public String sValue = "";

   public Field(int iTypeID)
   {
      this.iTypeID = iTypeID;
   }

   public Field(int iTypeID, String sValue)
   {
      this.iTypeID = iTypeID;
      this.sValue = sValue;
   }

   public Field(int iTypeID, long iID, String sValue)
   {
      this.iID = iID;
      this.iTypeID = iTypeID;
      this.sValue = sValue;
   }

   public void copy(Field f)
   {
      iID = f.iID;
      iTypeID = f.iTypeID;
      sValue = f.sValue;
   }

   @Override
   public String toString()
   {
      return sValue;
   }


   @Override
   public String getValue()
   {
      return sValue;
   }


//   @Override
//   public void copy(Object o)
//   {
//      copy((Field) o);
//   }
}

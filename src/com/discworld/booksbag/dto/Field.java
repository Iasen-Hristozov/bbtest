package com.discworld.booksbag.dto;

/**
 * Created by Iasen on 12.7.2016 г..
 */
public class Field
{
   public int iTypeID;
   public long iID = 0;
   public String sValue;

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
}

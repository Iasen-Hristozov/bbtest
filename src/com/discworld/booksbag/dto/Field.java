package com.discworld.booksbag.dto;

/**
 * Created by Iasen on 12.7.2016 г..
 */
public class Field
{
   public int iTypeID;
   public long iID = 0;
   public String sName;

   public Field(int iTypeID)
   {
      this.iTypeID = iTypeID;
   }

   public Field(long iID, String sName)
   {
      this.iID = iID;
      this.sName = sName;
   }

   public Field(int iTypeID, long iID, String sName)
   {
      this.iID = iID;
      this.iTypeID = iTypeID;
      this.sName = sName;
   }
}

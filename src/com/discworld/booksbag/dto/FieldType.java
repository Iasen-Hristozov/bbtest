package com.discworld.booksbag.dto;

import android.text.InputType;

/**
 * Created by Iasen on 12.7.2016 г..
 */
public class FieldType
{
   public int iID = 0;
   public String sName;
   public boolean isVisible = true;
   public byte iType = 0;
   public int iInputType = 0;
   public String sDigit = "";
   public boolean isMultiline = false;

   public final static byte TYPE_TEXT = 1,
                            TYPE_TEXT_MULTILINE = 2,
                            TYPE_TEXT_AUTOCOMPLETE = 3,
                            TYPE_LIST = 4,
                            TYPE_MONEY = 5,
                            TYPE_MULTIFIELD = 6;
   
   public FieldType(int iID, String sName)
   {
      this.iID = iID;
      this.sName = sName;
   }
   
   public FieldType(int iID, String sName, boolean isVisible, byte iType)
   {
      this(iID, sName);
      this.isVisible = isVisible;
      this.iType = iType;
   }

//   public FieldType(int iID, String sName, boolean isVisible, byte iType, int iInputType)
//   {
//      this(iID, sName);
//      this.isVisible = isVisible;
//      this.iType = iType;
//      this.iInputType = iInputType;
//   }
   
//   public FieldType(int iID, String sName, boolean isVisible, String sDigit)
//   {
//      this(iID, sName);
//      this.isVisible = isVisible;
//      this.sDigit = sDigit;
//   }
   
   public FieldType setMultiline(boolean isMultiline)
   {
      this.isMultiline = isMultiline;
      return this;
   }
   
   public void setDigit(String sDigit)
   {
      this.sDigit = sDigit;
   }
   
   public FieldType setInputType(int iInputType)
   {
      this.iInputType = iInputType;
      return this;
   }

   
}

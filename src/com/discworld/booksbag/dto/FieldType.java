package com.discworld.booksbag.dto;

/**
 * Created by Iasen on 12.7.2016 г..
 */
public class FieldType
{
   public int iID = 0;
   public String sName;
   public boolean isVisible = true;
   public int iType = 0;

   public final static int TYPE_TEXT = 1,
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
   
   public FieldType(int iID, String sName, boolean isVisible, int iType)
   {
      this.isVisible = isVisible;
      this.iType = iType;
   }
}

package com.discworld.booksbag.dto;

import java.util.ArrayList;

/**
 * Created by Iasen on 12.7.2016 г..
 */
public class Book
{
   public int iAuthor1ID,
              iAuthor2ID,
              iAuthor3ID,
              iSeriesID,
              iVolume = 0,
              iCategoryID,
              iLanguageID,
              iPublisherID,
              iPublicationLocationID,
              iPublicationDate = 0,
              iPages = 0,
              iStatusID,
              iRatingID,
              iFormatID,
              iLocationID,
              iPrice = 0,
              iValue = 0,
              iConditionID,
              iDueDate,
              iReadDate,
              iEdition;

   public long iID = 0;
   
   public String sTitle,
                 sDescription,
                 sISBN,
                 sWeb;
   
   public ArrayList<Field> alFields = new ArrayList<>();

   private static final int ID_KEY_ID = 1,
         ID_KEY_TTL = 2,
         ID_KEY_DSCR = 3,
         ID_KEY_VLM = 4,
         ID_KEY_PBL_DT = 5,
         ID_KEY_PGS = 6,
         ID_KEY_PRC = 7,
         ID_KEY_VL = 8,
         ID_KEY_DUE_DT = 9,
         ID_KEY_RD_DT = 10,
         ID_KEY_EDN = 11,
         ID_KEY_ISBN = 12,
         ID_KEY_WEB = 13;

   public Book()
   {
   }

   public Book(int iID,
               String sTitle,
               String sDescription,
               int iVolume,
               int iPublicationDate,
               int iPages,
               int iPrice,
               int iValue,
               int iDueDate,
               int iReadDate,
               int iEdition,
               String sISBN,
               String sWeb)
   {
      this.iID = iID;
      this.sTitle = sTitle;
      this.sDescription = sDescription;
      this.iVolume = iVolume;
      this.iPublicationDate = iPublicationDate;
      this.iPages = iPages;
      this.iPrice = iPrice;
      this.iValue = iValue;
      this.iDueDate = iDueDate;
      this.iReadDate = iReadDate;
      this.iEdition = iEdition;
      this.sISBN = sISBN;
      this.sWeb = sWeb;
   }
}

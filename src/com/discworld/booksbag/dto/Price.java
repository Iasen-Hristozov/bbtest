package com.discworld.booksbag.dto;

public class Price
{
   public int iValue;
   public long iCurrencyID;
   
   public Price(String sPrice)
   {
      String sParts[] = sPrice.split("\\|");
      iValue = Integer.parseInt(sParts[0]);
      if(sParts.length > 1)
         iCurrencyID = Long.parseLong(sParts[1]);
   }
   
   public Price(int iValue, int iCurrency)
   {
      this.iValue = iValue;
      this.iCurrencyID = iCurrency;
   }

   @Override
   public String toString()
   {
      return String.valueOf(iValue) + "|" + String.valueOf(iCurrencyID);
   }
}

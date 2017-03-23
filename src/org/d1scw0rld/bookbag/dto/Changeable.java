package com.discworld.booksbag.dto;

public class Changeable<T> 
{
   public T value = null;

//   public Changeable() 
//   {
//   }
   public Changeable(T value) 
   {
      this.value = value;
   }

   public String toString() 
   {
      return value.toString();
   }
   
   public Class getGenericType()
   {
      return value.getClass();
   }
   
   public void set(T value)
   {
      this.value = value;
   }

   public boolean equals(Object other) 
   {
      if (other instanceof Changeable) 
         return value.equals(((Changeable)other).value);
      else 
         return value.equals(other);
   }

   public int hashCode() 
   {
      return value.hashCode();
   }
}
package com.discworld.booksbag.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.discworld.booksbag.DBAdapter;
import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.Field;
import com.discworld.booksbag.dto.FieldType;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent
{

   /**
    * An array of sample (dummy) items.
    */
//   public static final ArrayList<DummyItem> ITEMS = new ArrayList<DummyItem>();
   public static final ArrayList<Book> BOOKS = new ArrayList<Book>();
   public static final ArrayList<Field> AUTHORS = new ArrayList<Field>();
   public static final ArrayList<Field> SERIES = new ArrayList<Field>();
   public static final ArrayList<Field> CATEGORIES = new ArrayList<Field>();
   public static final ArrayList<Field> LANGUAGES = new ArrayList<Field>();
   public static final ArrayList<Field> PUBLISHERS = new ArrayList<Field>();
   public static final ArrayList<Field> PUBLISHING_LOCATIONS = new ArrayList<Field>();
   public static final ArrayList<Field> STATUS = new ArrayList<Field>();
   public static final ArrayList<Field> RATINGS = new ArrayList<Field>();
   public static final ArrayList<Field> FORMATS = new ArrayList<Field>();
   public static final ArrayList<Field> LOCATIONS = new ArrayList<Field>();
   public static final ArrayList<Field> CONDITIONS = new ArrayList<Field>();
   public static final ArrayList<Field> CURRENCIES = new ArrayList<Field>();
   
   /**
    * A map of sample (dummy) items, by ID.
    */
//   public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
   public static final Map<Long, Book> BOOKS_MAP = new HashMap<Long, Book>();


   static
   {
      // Add some sample items.
      
      AUTHORS.add(new Field(1, DBAdapter.FLD_AUTHOR, "Жул Верн"));
      AUTHORS.add(new Field(2, DBAdapter.FLD_AUTHOR, "Александър Дюма"));
      AUTHORS.add(new Field(3, DBAdapter.FLD_AUTHOR, "Карл Май"));

      SERIES.add(new Field(4, DBAdapter.FLD_SERIE, "Тримата мускетари"));
      SERIES.add(new Field(5, DBAdapter.FLD_SERIE, "Винету"));
      
      CATEGORIES.add(new Field(6, DBAdapter.FLD_CATEGORY, "Приключения"));
      CATEGORIES.add(new Field(7, DBAdapter.FLD_CATEGORY, "Криминал"));
      
      LANGUAGES.add(new Field(8, DBAdapter.FLD_LANGUAGE, "Bulgarian"));
      LANGUAGES.add(new Field(9, DBAdapter.FLD_LANGUAGE, "Russian"));
      LANGUAGES.add(new Field(10, DBAdapter.FLD_LANGUAGE, "English"));
      
      PUBLISHERS.add(new Field(11, DBAdapter.FLD_PUBLISHER, "Отечество"));
      PUBLISHERS.add(new Field(12, DBAdapter.FLD_PUBLISHER, "Галактика"));
      PUBLISHERS.add(new Field(14, DBAdapter.FLD_PUBLISHER, "Юношески романи"));

      PUBLISHING_LOCATIONS.add(new Field(15, DBAdapter.FLD_PUBLICATION_LOCATION, "София"));
      PUBLISHING_LOCATIONS.add(new Field(16, DBAdapter.FLD_PUBLICATION_LOCATION, "Варна"));
      PUBLISHING_LOCATIONS.add(new Field(17, DBAdapter.FLD_PUBLICATION_LOCATION, "Москва"));
      
      STATUS.add(new Field(18, DBAdapter.FLD_STATUS, "Available"));
      STATUS.add(new Field(19, DBAdapter.FLD_STATUS, "Wanted"));
      STATUS.add(new Field(20, DBAdapter.FLD_STATUS, "Loan"));
      
      RATINGS.add(new Field(21, DBAdapter.FLD_RATING, "1"));
      RATINGS.add(new Field(22, DBAdapter.FLD_RATING, "2"));
      RATINGS.add(new Field(23, DBAdapter.FLD_RATING, "3"));

      FORMATS.add(new Field(24, DBAdapter.FLD_FORMAT, "Hard copy"));
      FORMATS.add(new Field(25, DBAdapter.FLD_FORMAT, "fb2"));
      FORMATS.add(new Field(26, DBAdapter.FLD_FORMAT, "epub"));
      
      LOCATIONS.add(new Field(27, DBAdapter.FLD_LOCATION, "Горна библиотека"));
      LOCATIONS.add(new Field(28, DBAdapter.FLD_LOCATION, "Долна библиотека"));

      CONDITIONS.add(new Field(29, DBAdapter.FLD_CONDITION, "As New"));
      CONDITIONS.add(new Field(30, DBAdapter.FLD_CONDITION, "Fine"));
      CONDITIONS.add(new Field(31, DBAdapter.FLD_CONDITION, "Very Good"));
      CONDITIONS.add(new Field(32, DBAdapter.FLD_CONDITION, "Good"));
      CONDITIONS.add(new Field(33, DBAdapter.FLD_CONDITION, "Fair"));
      CONDITIONS.add(new Field(34, DBAdapter.FLD_CONDITION, "Poor"));
      CONDITIONS.add(new Field(35, DBAdapter.FLD_CONDITION, "Binding Copy"));
      CONDITIONS.add(new Field(36, DBAdapter.FLD_CONDITION, "Reading Copy"));

      
      CURRENCIES.add(new Field(37, DBAdapter.FLD_CURRENCY, "BGN"));
      CURRENCIES.add(new Field(38, DBAdapter.FLD_CURRENCY, "EUR"));
      CURRENCIES.add(new Field(39, DBAdapter.FLD_CURRENCY, "RBL"));
      
      Book oBook = new Book(1,                                                   // ID
                            "Трех мушкетера",                                    // Title
                            "Приключения Атоса, Портоса, Арамиса и Д'Артаняна",  // Description
                            1,                                                   // Volume
                            1978,                                                // Publication Date
                            360,                                                 // Pages
                            "260|34",                                            // Price
                            "1500|32",                                           // Value
                            0,                                                   // Due Date
                            0,                                            // Read date
                            3,                                                   // Edition
                            "",                                                  // ISBN
                            "");                                                 // Web
//      oBook.alFields.add(new Field(DBAdapter.FLD_AUTHOR, 2, "Александър Дюма"));
//      oBook.alFields.add(new Field(DBAdapter.FLD_AUTHOR, 1, "Жул Верн"));
      oBook.alFields.add(AUTHORS.get(1));
      oBook.alFields.add(AUTHORS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_SERIE, 4, "Трех мушкетера"));
      oBook.alFields.add(SERIES.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_CATEGORY, 6, "Приключения"));
      oBook.alFields.add(CATEGORIES.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_LANGUAGE, 9, "Руски"));
      oBook.alFields.add(LANGUAGES.get(1));
//      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLISHER, 14, "Юношески романи"));
      oBook.alFields.add(PUBLISHERS.get(2));
//      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 17, "Москва"));
      oBook.alFields.add(PUBLISHING_LOCATIONS.get(2));
//      oBook.alFields.add(new Field(DBAdapter.FLD_STATUS, 18, "Налична"));
      oBook.alFields.add(STATUS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_RATING, 22, "2"));
      oBook.alFields.add(RATINGS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_FORMAT, 24, "Hard copy"));
      oBook.alFields.add(FORMATS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_CONDITION, 29, "Отлично"));
      oBook.alFields.add(CONDITIONS.get(0));
      BOOKS.add(oBook);
      BOOKS_MAP.put(oBook.iID, oBook);
      
      oBook = new Book(2,                                            // ID
                       "Винету",                                     // Title
                       "Приключенията на Винету и Олд Шетърхенд",    // Description
                       2,                                            // Volume
                       1981,                                         // Publication Date
                       321,                                          // Pages
                       "150|32",                                     // Price
                       "500|32",                                     // Value
                       0,                                            // Due Date
                       19850620,                                     // Read date
                       5,                                            // Edition
                       "",                                           // ISBN
                       "");                                          // Web
//      oBook.alFields.add(new Field(DBAdapter.FLD_AUTHOR, 3, "Карл Май"));
      oBook.alFields.add(AUTHORS.get(2));
//      oBook.alFields.add(new Field(DBAdapter.FLD_SERIE, 5, "Винету"));
      oBook.alFields.add(SERIES.get(1));
//      oBook.alFields.add(new Field(DBAdapter.FLD_CATEGORY, 6, "Приключения"));
      oBook.alFields.add(CATEGORIES.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_LANGUAGE, 8, "Български"));
      oBook.alFields.add(LANGUAGES.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLISHER, 11, "Отечество"));
      oBook.alFields.add(PUBLISHERS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 15, "София"));
      oBook.alFields.add(PUBLISHING_LOCATIONS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_STATUS, 18, "Налична"));
      oBook.alFields.add(STATUS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_RATING, 21, "1"));
      oBook.alFields.add(RATINGS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_FORMAT, 24, "Hard copy"));
      oBook.alFields.add(FORMATS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_LOCATION, 27, "Горна библиотека"));
      oBook.alFields.add(LOCATIONS.get(0));
//      oBook.alFields.add(new Field(DBAdapter.FLD_CONDITION, 31, "Добро"));
      oBook.alFields.add(CONDITIONS.get(2));
      BOOKS.add(oBook);
      BOOKS_MAP.put(oBook.iID, oBook);
      
      
   }

//   private static void addItem(DummyItem item)
//   {
//      ITEMS.add(item);
//      ITEM_MAP.put(item.id, item);
//   }
//
//   private static DummyItem createDummyItem(int position)
//   {
//      return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
//   }

   private static String makeDetails(int position)
   {
      StringBuilder builder = new StringBuilder();
      builder.append("Details about Item: ")
             .append(position);
      for (int i = 0; i < position; i++)
      {
         builder.append("\nMore details information here.");
      }
      return builder.toString();
   }

   /**
    * A dummy item representing a piece of content.
    */
   public static class DummyItem
   {
      public final String id;
      public final String content;
      public final String details;

      public DummyItem(String id, String content, String details)
      {
         this.id = id;
         this.content = content;
         this.details = details;
      }

      @Override
      public String toString()
      {
         return content;
      }
   }
}

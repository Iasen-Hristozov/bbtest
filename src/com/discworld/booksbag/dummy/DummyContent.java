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

//   private static final int COUNT = 25;
//   private static final int BOOKS_COUNT = 3;

   static
   {
      // Add some sample items.
//      for (int i = 1; i <= COUNT; i++)
//      {
//         addItem(createDummyItem(i));
//      }
      
      AUTHORS.add(new Field(DBAdapter.FLD_AUTHOR, 1, "Жул Верн"));
      AUTHORS.add(new Field(DBAdapter.FLD_AUTHOR, 2, "Александър Дюма"));
      AUTHORS.add(new Field(DBAdapter.FLD_AUTHOR, 3, "Карл Май"));

      SERIES.add(new Field(DBAdapter.FLD_SERIE, 4, "Тримата мускетари"));
      SERIES.add(new Field(DBAdapter.FLD_SERIE, 5, "Винету"));
      
      CATEGORIES.add(new Field(DBAdapter.FLD_CATEGORY, 6, "Приключения"));
      CATEGORIES.add(new Field(DBAdapter.FLD_CATEGORY, 7, "Криминал"));
      
      LANGUAGES.add(new Field(DBAdapter.FLD_LANGUAGE, 8, "Български"));
      LANGUAGES.add(new Field(DBAdapter.FLD_LANGUAGE, 9, "Руски"));
      LANGUAGES.add(new Field(DBAdapter.FLD_LANGUAGE, 10, "Английски"));
      
      PUBLISHERS.add(new Field(DBAdapter.FLD_PUBLISHER, 11, "Отечество"));
      PUBLISHERS.add(new Field(DBAdapter.FLD_PUBLISHER, 12, "Галактика"));
      PUBLISHERS.add(new Field(DBAdapter.FLD_PUBLISHER, 14, "Юношески романи"));

      PUBLISHING_LOCATIONS.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 15, "София"));
      PUBLISHING_LOCATIONS.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 16, "Варна"));
      PUBLISHING_LOCATIONS.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 17, "Москва"));
      
      STATUS.add(new Field(DBAdapter.FLD_STATUS, 18, "Налична"));
      STATUS.add(new Field(DBAdapter.FLD_STATUS, 19, "Търси се"));
      STATUS.add(new Field(DBAdapter.FLD_STATUS, 20, "Дадена"));
      
      RATINGS.add(new Field(DBAdapter.FLD_RATING, 21, "1"));
      RATINGS.add(new Field(DBAdapter.FLD_RATING, 22, "2"));
      RATINGS.add(new Field(DBAdapter.FLD_RATING, 23, "3"));

      FORMATS.add(new Field(DBAdapter.FLD_FORMAT, 24, "Hard copy"));
      FORMATS.add(new Field(DBAdapter.FLD_FORMAT, 25, "fb2"));
      FORMATS.add(new Field(DBAdapter.FLD_FORMAT, 26, "epub"));
      
      LOCATIONS.add(new Field(DBAdapter.FLD_LOCATION, 27, "Горна библиотека"));
      LOCATIONS.add(new Field(DBAdapter.FLD_LOCATION, 28, "Долна библиотека"));

      CONDITIONS.add(new Field(DBAdapter.FLD_CONDITION, 29, "Отлично"));
      CONDITIONS.add(new Field(DBAdapter.FLD_CONDITION, 30, "Много добро"));
      CONDITIONS.add(new Field(DBAdapter.FLD_CONDITION, 31, "Добро"));
      
      CURRENCIES.add(new Field(DBAdapter.FLD_CURRENCY, 32, "BGN"));
      CURRENCIES.add(new Field(DBAdapter.FLD_CURRENCY, 33, "EUR"));
      CURRENCIES.add(new Field(DBAdapter.FLD_CURRENCY, 34, "RBL"));
      
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

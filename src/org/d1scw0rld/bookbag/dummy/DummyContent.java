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
   public final static int FLD_DESCRIPTION = 100,
                           FLD_VOLUME = 101,
                           FLD_PUBLICATION_DATE = 102,
                           FLD_PAGES = 103,
                           FLD_PRICE = 104,
                           FLD_VALUE = 105,
                           FLD_DUE_DATE = 106,
                           FLD_READ_DATE = 107,
                           FLD_EDITION = 108,
                           FLD_ISBN = 109,
                           FLD_WEB = 110;                           

   /**
    * An array of sample (dummy) items.
    */
   public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
   public static final List<Book> BOOKS = new ArrayList<Book>();
   public static final List<Field> AUTHORS = new ArrayList<Field>();
   public static final List<Field> SERIES = new ArrayList<Field>();
   public static final List<Field> CATEGORIES = new ArrayList<Field>();
   public static final List<Field> LANGUAGES = new ArrayList<Field>();
   public static final List<Field> PUBLISHERS = new ArrayList<Field>();
   public static final List<Field> PUBLISHING_LOCATIONS = new ArrayList<Field>();
   public static final List<Field> STATUS = new ArrayList<Field>();
   public static final List<Field> RATINGS = new ArrayList<Field>();
   public static final List<Field> FORMATS = new ArrayList<Field>();
   public static final List<Field> LOCATIONS = new ArrayList<Field>();
   public static final List<Field> CONDITIONS = new ArrayList<Field>();
   public static final List<FieldType> FIELD_TYPES = new ArrayList<>();
   
   /**
    * A map of sample (dummy) items, by ID.
    */
   public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
   public static final Map<Long, Book> BOOKS_MAP = new HashMap<Long, Book>();

   private static final int COUNT = 25;
   private static final int BOOKS_COUNT = 3;

   static
   {
      // Add some sample items.
      for (int i = 1; i <= COUNT; i++)
      {
         addItem(createDummyItem(i));
      }
      
      AUTHORS.add(new Field(DBAdapter.FLD_AUTHOR, 1, "��� ����"));
      AUTHORS.add(new Field(DBAdapter.FLD_AUTHOR, 2, "���������� ����"));
      AUTHORS.add(new Field(DBAdapter.FLD_AUTHOR, 3, "���� ���"));

      SERIES.add(new Field(DBAdapter.FLD_SERIE, 4, "������� ���������"));
      SERIES.add(new Field(DBAdapter.FLD_SERIE, 5, "������"));
      
      CATEGORIES.add(new Field(DBAdapter.FLD_CATEGORY, 6, "�����������"));
      CATEGORIES.add(new Field(DBAdapter.FLD_CATEGORY, 7, "��������"));
      
      LANGUAGES.add(new Field(DBAdapter.FLD_LANGUAGE, 8, "���������"));
      LANGUAGES.add(new Field(DBAdapter.FLD_LANGUAGE, 9, "�����"));
      LANGUAGES.add(new Field(DBAdapter.FLD_LANGUAGE, 10, "���������"));
      
      PUBLISHERS.add(new Field(DBAdapter.FLD_PUBLISHER, 11, "���������"));
      PUBLISHERS.add(new Field(DBAdapter.FLD_PUBLISHER, 12, "���������"));
      PUBLISHERS.add(new Field(DBAdapter.FLD_PUBLISHER, 14, "�������� ������"));

      PUBLISHING_LOCATIONS.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 15, "�����"));
      PUBLISHING_LOCATIONS.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 16, "�����"));
      PUBLISHING_LOCATIONS.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 17, "������"));
      
      STATUS.add(new Field(DBAdapter.FLD_STATUS, 18, "�������"));
      STATUS.add(new Field(DBAdapter.FLD_STATUS, 19, "����� ��"));
      STATUS.add(new Field(DBAdapter.FLD_STATUS, 20, "������"));
      
      RATINGS.add(new Field(DBAdapter.FLD_RATING, 21, "1"));
      RATINGS.add(new Field(DBAdapter.FLD_RATING, 22, "2"));
      RATINGS.add(new Field(DBAdapter.FLD_RATING, 23, "3"));

      FORMATS.add(new Field(DBAdapter.FLD_FORMAT, 24, "Hard copy"));
      FORMATS.add(new Field(DBAdapter.FLD_FORMAT, 25, "fb2"));
      FORMATS.add(new Field(DBAdapter.FLD_FORMAT, 26, "epub"));
      
      LOCATIONS.add(new Field(DBAdapter.FLD_LOCATION, 27, "����� ����������"));
      LOCATIONS.add(new Field(DBAdapter.FLD_LOCATION, 28, "����� ����������"));

      CONDITIONS.add(new Field(DBAdapter.FLD_CONDITION, 29, "�������"));
      CONDITIONS.add(new Field(DBAdapter.FLD_CONDITION, 30, "����� �����"));
      CONDITIONS.add(new Field(DBAdapter.FLD_CONDITION, 31, "�����"));
      
      Book oBook = new Book(1, "���� ���������", "����������� �����, �������, ������� � �'��������", 1, 1978, 360, 260, 1500, 0, 19850620, 3, "", "");
      oBook.alFields.add(new Field(DBAdapter.FLD_AUTHOR, 2, "���������� ����"));
      oBook.alFields.add(new Field(DBAdapter.FLD_AUTHOR, 1, "��� ����"));
      oBook.alFields.add(new Field(DBAdapter.FLD_SERIE, 4, "���� ���������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_CATEGORY, 6, "�����������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_LANGUAGE, 9, "�����"));
      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLISHER, 14, "�������� ������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 17, "������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_STATUS, 18, "�������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_RATING, 22, "2"));
      oBook.alFields.add(new Field(DBAdapter.FLD_FORMAT, 24, "Hard copy"));
      oBook.alFields.add(new Field(DBAdapter.FLD_CONDITION, 29, "�������"));
      BOOKS.add(oBook);
      BOOKS_MAP.put(oBook.iID, oBook);
      
      oBook = new Book(2, "������", "������������� �� ������ � ��� ���������", 2, 1981, 321, 150, 500, 0, 19850620, 5, "", "");
      oBook.alFields.add(new Field(DBAdapter.FLD_AUTHOR, 3, "���� ���"));
      oBook.alFields.add(new Field(DBAdapter.FLD_SERIE, 5, "������"));
//      oBook.alFields.add(new Field(DBAdapter.FLD_CATEGORY, 6, "�����������"));
      oBook.alFields.add(CATEGORIES.get(0));
      oBook.alFields.add(new Field(DBAdapter.FLD_LANGUAGE, 8, "���������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLISHER, 11, "���������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_PUBLICATION_LOCATION, 15, "�����"));
      oBook.alFields.add(new Field(DBAdapter.FLD_STATUS, 18, "�������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_RATING, 21, "1"));
      oBook.alFields.add(new Field(DBAdapter.FLD_FORMAT, 24, "Hard copy"));
      oBook.alFields.add(new Field(DBAdapter.FLD_LOCATION, 27, "����� ����������"));
      oBook.alFields.add(new Field(DBAdapter.FLD_CONDITION, 31, "�����"));
      BOOKS.add(oBook);
      BOOKS_MAP.put(oBook.iID, oBook);
      
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_AUTHOR, "Authors"));
      FIELD_TYPES.add(new FieldType(FLD_DESCRIPTION, "Description"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_SERIE, "Serie"));
      FIELD_TYPES.add(new FieldType(FLD_VOLUME, "Volume"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_CATEGORY, "Category"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_LANGUAGE, "Language"));
      FIELD_TYPES.add(new FieldType(FLD_PAGES, "Pages"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_PUBLISHER, "Publisher"));
      FIELD_TYPES.add(new FieldType(FLD_PUBLICATION_DATE, "Publication Date"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_PUBLICATION_LOCATION, "Publication Location"));
      FIELD_TYPES.add(new FieldType(FLD_EDITION, "Edition"));
      FIELD_TYPES.add(new FieldType(FLD_PRICE, "Price"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_STATUS, "Status"));
      FIELD_TYPES.add(new FieldType(FLD_VALUE, "Value"));
      FIELD_TYPES.add(new FieldType(FLD_READ_DATE, "Read Date"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_RATING, "Rating"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_FORMAT, "Format"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_LOCATION, "Location"));
      FIELD_TYPES.add(new FieldType(DBAdapter.FLD_CONDITION, "Condition"));
      FIELD_TYPES.add(new FieldType(FLD_DUE_DATE, "Due Date"));
      FIELD_TYPES.add(new FieldType(FLD_ISBN, "ISBN"));
      FIELD_TYPES.add(new FieldType(FLD_WEB, "Web"));
      
   }

   private static void addItem(DummyItem item)
   {
      ITEMS.add(item);
      ITEM_MAP.put(item.id, item);
   }

   private static DummyItem createDummyItem(int position)
   {
      return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
   }

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
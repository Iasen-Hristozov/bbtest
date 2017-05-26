package com.discworld.booksbag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dto.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity
{
   public final static int SHOW_EDIT_BOOK = 101;
   
   private static final String XPR_DIR = "BooksBag",
                               DB_PATH = "//data//com.discworld.booksbag//databases//";
   
   private DBAdapter oDbAdapter = null;

   /**
    * Whether or not the activity is in two-pane mode, i.e. running on a tablet
    * device.
    */
   private boolean mTwoPane;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_book_list);

      oDbAdapter = new DBAdapter(this);
      oDbAdapter.open();
      
      verifyStoragePermissions(this);
      
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      toolbar.setTitle(getTitle());

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View view)
         {
            Intent intent = new Intent(getApplicationContext(), EditBookActivity.class);
            intent.putExtra(EditBookActivity.BOOK_ID, 0);
//            intent.setClass(getApplicationContext(), EditBookActivity.class);
            startActivityForResult(intent, SHOW_EDIT_BOOK);
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
//                    .show();
         }
      });

      View recyclerView = findViewById(R.id.book_list);
      assert recyclerView != null;
      setupRecyclerView((RecyclerView) recyclerView);

      if (findViewById(R.id.book_detail_container) != null)
      {
         // The detail container view will be present only in the
         // large-screen layouts (res/values-w900dp).
         // If this view is present, then the
         // activity should be in two-pane mode.
         mTwoPane = true;
      }
   }
   
   

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }



   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch(item.getItemId())
      {
         case R.id.action_settings:
            return true;

         case R.id.action_exp_db:
            oDbAdapter.close();
            if(bExportDb())
               Toast.makeText(getApplicationContext(), R.string.prf_xpr_db_scs, Toast.LENGTH_SHORT).show();
            oDbAdapter.open();            
            return true;
         
         case R.id.action_imp_db:
            return true;
            
         default:
            return true;
      }
   }



   private void setupRecyclerView(@NonNull RecyclerView recyclerView)
   {
//      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
//      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.BOOKS));
      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(oDbAdapter.getBooks(DBAdapter.ORD_TTL)));
   }

   public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
   {

//      private final List<DummyContent.DummyItem> mValues;
//      private final List<Book> mValues;
      private final List<Result> mValues;

//      public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items)
//      public SimpleItemRecyclerViewAdapter(List<Book> items)
      public SimpleItemRecyclerViewAdapter(List<Result> items)
      {
         mValues = items;
      }

      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
      {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_content, parent, false);
         return new ViewHolder(view);
      }

      @Override
      public void onBindViewHolder(final ViewHolder holder, int position)
      {
         holder.mItem = mValues.get(position);
         holder.mIdView.setText(String.valueOf(mValues.get(position)._id));
         holder.mContentView.setText(mValues.get(position).content);

         holder.mView.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               if (mTwoPane)
               {
                  Bundle arguments = new Bundle();
                  arguments.putLong(BookDetailFragment.ARG_ITEM_ID, holder.mItem._id);
                  BookDetailFragment fragment = new BookDetailFragment();
                  fragment.setArguments(arguments);
                  getSupportFragmentManager().beginTransaction()
                                             .replace(R.id.book_detail_container, fragment)
                                             .commit();
               }
               else
               {
                  Context context = v.getContext();
                  Intent intent = new Intent(context, BookDetailActivity.class);
                  intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem._id);

                  context.startActivity(intent);
               }
            }
         });
      }

      @Override
      public int getItemCount()
      {
         return mValues.size();
      }

      public class ViewHolder extends RecyclerView.ViewHolder
      {
         public final View mView;
         public final TextView mIdView;
         public final TextView mContentView;
         public Result mItem;

         public ViewHolder(View view)
         {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
         }

         @Override
         public String toString()
         {
            return super.toString() + " '" + mContentView.getText() + "'";
         }
      }
   }

   @Override
   protected void onPause()
   {
      oDbAdapter.close();

      super.onPause();
   }

   @Override
   protected void onResume()
   {
      super.onResume();

      oDbAdapter.open();

//      oBook = oDbAdapter.getBook(oBook.iID);
   }

   private boolean bExportDb()
   {
      try
      {
//         File flRoot = android.os.Environment.getExternalStorageDirectory();

         // File flXprDir = new File (flRoot.getAbsolutePath() + "/" + XPR_DIR);
         File flXprDir = new File(Environment.getExternalStorageDirectory() + "/" + XPR_DIR);

         File flData = Environment.getDataDirectory();

         if(flXprDir.canWrite())
         {
            File flCurrentDB = new File(flData, DB_PATH
                     + DBAdapter.DATABASE_NAME);
            File flBackupDB = new File(flXprDir, DBAdapter.DATABASE_NAME);

            if(flCurrentDB.exists())
            {
               FileChannel src = new FileInputStream(flCurrentDB).getChannel();
               FileChannel dst = new FileOutputStream(flBackupDB).getChannel();

               dst.transferFrom(src, 0, src.size());
               return true;
            }
         }
      } catch(Exception e)
      {
         System.out.println(e.getMessage());
         Log.e("BookListActivity", e.getMessage());
         return false;
      }
      // finally
      // {
      // try
      // {
      // src.close();
      // dst.close();
      // } catch(IOException e)
      // {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }
      // }
      return false;
   }
   
   public static void verifyStoragePermissions(Activity activity) {
      // Check if we have write permission
      int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

      if (permission != PackageManager.PERMISSION_GRANTED) {
          // We don't have permission so prompt the user
          ActivityCompat.requestPermissions(
                  activity,
                  new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                  1);
      }
  }
}

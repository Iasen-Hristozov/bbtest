package com.discworld.booksbag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.discworld.booksbag.dto.Book;
import com.discworld.booksbag.dummy.DummyContent;

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

   private void setupRecyclerView(@NonNull RecyclerView recyclerView)
   {
//      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.BOOKS));
   }

   public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
   {

//      private final List<DummyContent.DummyItem> mValues;
      private final List<Book> mValues;

//      public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items)
      public SimpleItemRecyclerViewAdapter(List<Book> items)
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
         holder.mIdView.setText(String.valueOf(mValues.get(position).iID));
         holder.mContentView.setText(mValues.get(position).sTitle);

         holder.mView.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               if (mTwoPane)
               {
                  Bundle arguments = new Bundle();
                  arguments.putLong(BookDetailFragment.ARG_ITEM_ID, holder.mItem.iID);
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
                  intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem.iID);

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
         public Book mItem;

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
}

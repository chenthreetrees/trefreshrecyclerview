package com.threetree.trefreshrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.threetree.ttrefreshrecyclerview.HTLoadMoreListener;
import com.threetree.ttrefreshrecyclerview.HTRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private HTRefreshRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (HTRefreshRecyclerView)findViewById(R.id.recycler);

        HTRefreshHolder mRefreshHolder = new HTRefreshHolder(this);
        mRecyclerView.setRefreshViewHolder(mRefreshHolder);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLoadMoreViewShow(false);

        List list = new ArrayList();
        for (int i=0;i<30;i++)
        {
            list.add("item" + i);
        }
        final MyAdapter adapter = new MyAdapter(list);
        mRecyclerView.setOnLoadMoreListener(new HTLoadMoreListener() {
            @Override
            public void onLoadMore()
            {
                adapter.insert("item");
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    public void run()
                    {
                        mRecyclerView.setRefreshCompleted(true);
                    }
                }, 500);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter
    {
        List mList;
        public MyAdapter(List list)
        {
            mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            TextView textView = new TextView(MainActivity.this);
            Holder holder = new Holder(textView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
        {
            String text = (String)mList.get(position);
            TextView textView = (TextView) holder.itemView;
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    remove(position);
                    return true;
                }
            });
            textView.setText(text);
        }

        @Override
        public int getItemCount()
        {
            return mList.size();
        }

        public void remove(int position)
        {
            mList.remove(position);
            notifyItemRemoved(position);
        }


        public void insert(Object item)
        {
            mList.add(item);
            notifyItemInserted(mList.size()-1);
        }

        class Holder extends RecyclerView.ViewHolder
        {
            public Holder(View itemView)
            {
                super(itemView);
            }
        }
    }
}

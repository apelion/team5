package zhangying.bwei.com.rikao1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.xlistviewlibrary.View.XListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import zhangying.bwei.com.mlibrary.NetUtil;
import zhangying.bwei.com.rikao1.bean.Prodct;

public class MainActivity extends AppCompatActivity {
    String url="http://api.expoon.com/AppNews/getNewsList/type/1/p/1";
    List<Prodct.DataBean> list =new ArrayList<>();
    int page;
    private XListView xlv;
    private ImageLoader instance;
    private MAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xlv = (XListView) findViewById(R.id.xlv);
        instance = ImageLoader.getInstance();
        initData(0);
        mAdapter = new MAdapter();
        xlv.setAdapter(mAdapter);
        xlv.setPullLoadEnable(true);
      //xlv.setXListViewListener(this);
    }

    private void initData(int page) {
        new MAsyncTask().execute(url+page);
    }




        private void uicomplete() {
            xlv.stopRefresh();
            xlv.stopLoadMore();
            xlv.setRefreshTime("刚刚"+System.currentTimeMillis());
        }

        public void onRefresh() {
            list.clear();
            initData(0);
        }

    //上拉加载


    public void onLoadMore() {
        page++;
        initData(page);
    }


    private class MAdapter extends BaseAdapter{
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position%2;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int viewType=getItemViewType(i);
            switch (viewType){
                case 0:
                    ViewHolder viewHolder=null;
                    if(view==null){
                        viewHolder=new ViewHolder();
                        view=View.inflate(MainActivity.this,R.layout.item1,null);
                        viewHolder.textview=(TextView)view.findViewById(R.id.textView);
                        viewHolder.imageView=(ImageView)view.findViewById(R.id.imageView);
                        view.setTag(viewHolder);
                    }else{
                        viewHolder= (ViewHolder) view.getTag();
                    }
                    viewHolder.textview.setText(list.get(i).getNews_title());
                    instance.displayImage(list.get(i).getPic_url(),viewHolder.imageView);
                    break;
                case 1:
                    ViewHolder1 viewHolder1=null;
                    if(view==null){
                        viewHolder1=new ViewHolder1();
                        view=View.inflate(MainActivity.this,R.layout.item2,null);
                        viewHolder1.textView1=(TextView)view.findViewById(R.id.item2_text);
                        viewHolder1.textView2=(TextView)view.findViewById(R.id.image1);
                        view.setTag(viewHolder1);
                    }else{
                        viewHolder1= (ViewHolder1) view.getTag();
                    }
                    viewHolder1.textView1.setText(list.get(i).getNews_title());
                    viewHolder1.textView2.setText(list.get(i).getNews_summary());
                    break;

            }
            return view;
        }
    }
    private class MAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            return NetUtil.getNetJson(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Prodct product = gson.fromJson(s, Prodct.class);
            List<Prodct.DataBean> data = product.getData();
            list.addAll(data);
            mAdapter.notifyDataSetChanged();
            uicomplete();//停止刷新的方法
        }
    }

}

    class ViewHolder {
        TextView textview;
        ImageView imageView;
}
     class ViewHolder1{
        TextView textView1;
        TextView textView2;
}


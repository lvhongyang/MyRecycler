package com.example.recyclerviewdemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //获取的json数据中的数据集合
    private List<TestBean.美女Bean> list = new ArrayList<>();

    //创建一个list集合存储recyclerview中的图片的高度
    private List<Integer> heights = new ArrayList<>();

    //声明recyclerview引用
    private RecyclerView mRecyclerView;

    //声明自定义请求类
    private MyAdapter adapter;

    private SwipeRefreshLayout mysw;

    //用插件自动生成初始化view代码的方法
    private void assignViews() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mysw = (SwipeRefreshLayout)findViewById(R.id.Sw);

        //设置recyclerview要实现的类型为StaggeredGrid瀑布流类型
        //并再构造方法中指定列数3，纵向排列
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, RecyclerView.VERTICAL));

    }


    //网络请求数据的网址
    private String url = "http://c.3g.163.com/recommend/getChanListNews?channel=T1456112189138&size=20&passport=&devId=1uuFYbybIU2oqSRGyFrjCw%3D%3D&lat=%2F%2FOm%2B%2F8ScD%2B9fX1D8bxYWg%3D%3D&lon=LY2l8sFCNzaGzqWEPPgmUw%3D%3D&version=9.0&net=wifi&ts=1464769308&sign=bOVsnQQ6gJamli6%2BfINh6fC%2Fi9ydsM5XXPKOGRto5G948ErR02zJ6%2FKXOnxX046I&encryption=1&canal=meizu_store2014_news&mac=sSduRYcChdp%2BBL1a9Xa%2F9TC0ruPUyXM4Jwce4E9oM30%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        assignViews();

        //开启网络下载数据的方法
        startTask();

        mysw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                mysw.setRefreshing(false);
            }
        });




    }
    private void startTask() {

        //通过类名直接调用静态方法获取单例对象再调用getBeanOfOK()方法传入参数通过接口回调获取数据
        OkHttpUtils.getInstance().getBeanOfOk(this, url, TestBean.class,
                new OkHttpUtils.CallBack<TestBean>() {
                    @Override
                    public void getData(TestBean testBean) {

                        Log.i("===", "getData: "+testBean.toString());
                        if(testBean!=null){

                            //如果不为空用本地list接收
                            list.addAll(testBean.get美女());

                            //数据一旦回调成功初始化数据源和适配器
                            initHights();

                            initAdapter();
                        }
                    }
                });


    }

    private void initAdapter() {


        //创建自定义适配器对象
        adapter = new MyAdapter(this,list,heights);

        //设置recyclerview适配器
        mRecyclerView.setAdapter(adapter);

        //刷新适配器
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {
            @Override
            public void onItemclic(View view, int data) {
                Toast.makeText(MainActivity.this,"点击了"+data+"数据",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initHights() {
        //设置随机数
        Random random = new Random();

        for (int i = 0; i < list.size(); i++) {

            //集合中存储每个回调图片对应的随机高度
            heights.add(random.nextInt(200)+200);
        }

    }
}

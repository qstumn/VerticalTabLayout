# VerticalTabLayout
一个垂直竖向的Android TabLayout  

![Alt text](https://github.com/qstumn/VerticalTabLayout/blob/master/demo.png?raw=true)

###一些特性
* 支持自定义Indicator大小

* 支持自定义Indicator位置

* 支持Indicator设置圆角

* 支持为Tab设置Badge

* 支持Adapter的方式创建Tab

* 多种Tab高度设置模式

* 很方便的和ViewPager结合使用

##how to use:
####1.gradle
`compile 'q.rorbin:VerticalTabLayout:1.0.0'`

####2.xml

    <q.rorbin.verticaltablayout.VerticalTabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#EDEDED"
        app:indicator_color="#FFFFFF"
        app:indicator_gravity="fill"
        app:tab_height="50dp"
        app:tab_mode="scrollable" />

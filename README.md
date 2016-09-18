# VerticalTabLayout
一个垂直竖向的Android TabLayout  


##Change Log

    v1.1.0
    1. 修复了TabSelectedListener会错误的多次触发的问题
    2. 修复了removeAllTabs方法无法正确的移除掉所有tab的问题
    3. 修改setTabSelectedListener为addTabSelectedListener
    4. 为Indicator的移动添加了动画
    
    

![](https://github.com/qstumn/VerticalTabLayout/blob/master/demo.png?raw=true)

###一些特性
* 支持自定义Indicator大小

* 支持自定义Indicator位置

* 支持Indicator设置圆角

* 支持Tab设置Badge

* 支持Adapter的方式创建Tab

* 多种Tab高度设置模式

* 很方便的和ViewPager结合使用

## how to use:
###1. gradle
`compile 'q.rorbin:VerticalTabLayout:1.1.0'`

###2. xml

    <q.rorbin.verticaltablayout.VerticalTabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#EDEDED"
        app:indicator_color="#FFFFFF"
        app:indicator_gravity="fill"
        app:tab_height="50dp"
        app:tab_mode="scrollable" />
    

###3. 属性说明

xml | code | 说明
---|---|---
app:indicator_color | setIndicatorColor | 指示器颜色
app:indicator_width | setIndicatorWidth | 指示器宽度
app:indicator_gravity | setIndicatorGravity | 指示器位置
app:indicator_corners | setIndicatorCorners | 指示器圆角
app:tab_mode | setTabMode | Tab高度模式
app:tab_height | setTabHeight | Tab高度
app:tab_margin | setTabMargin | Tab间距

###4. 创建Tab的方式
- 普通方式创建
 
	``tablayout.addTab(new QTabView(context))``

		tablayout.setOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
- Adapter方式创建
	
		tablayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getBadge(int position) {
                return 0;
            }

            @Override
            public QTabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public QTabView.TabTitle getTitle(int position) {
                return null;
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
	     });
	     
  按照自己的需要进行返回相应的值即可，不需要的返回0或者null，TabIcon和TabTitle使用build模式创建。
  
- 结合ViewPager使用

    `tablayout.setupWithViewPager(viewpager);`
    
      ViewPager的PagerAdapter可选择实现TabAdapter接口

###5. 设置badge
	int tabPosition = 3;
	int badgeNum=55;
	tablayout.setTabBadge(tabPosition,badgeNum);

#LICENSE
```
Copyright 2016, RorbinQiu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

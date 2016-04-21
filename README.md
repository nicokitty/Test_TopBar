# Test_TopBar
This is my imooc project about Topbar(user-defined view).

# NOTE:

# 自定义View

## 系统是如何实现的

我们参考谷歌是如何实现一个控件的。

* 例如LinearLayout，使用如下：在xml文件中定义一个`LinearLayout`，再为其添加相应的属性，比如`layout_width`,`layout_height`,`paddingBottom`等。

```xml
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	   xmlns:tools="http://schemas.android.com/tools"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:paddingBottom="@dimen/activity_vertical_margin"
         android:paddingLeft="@dimen/activity_horizontal_margin"
         android:paddingRight="@dimen/activity_horizontal_margin"
         android:paddingTop="@dimen/activity_vertical_margin"
         tools:context="nicokitty.test_topbar.MainActivity">
     </LinearLayout>
```

* 源文件的定义，再次打开Android源代码，会发现，

```java
public class LinearLayout extends ViewGroup
```
`LinearLayout`继承自`ViewGroup`，并重写了其中的方法。
这些属性是定义在Android源码路径为 **frameworks/base/core/res/values/attrs.xml** 中，内容如下：
	
```
<declare-styleable name="LinearLayout_layout">
	<attr name=layout_width" />
	<attr name=layout_height" />
	<attr name=layout_weight" format="float" />
	<attr name=layout_gravity" />
<declare-styleable>
```


## 开始编写
### 步骤1：自定义需要的属性

> 在values文件中定义一个 **attrs.xml** 这个xml文件

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="Topbar">
        <attr name="titleText" format="string"/>
        <attr name="titleTextSize" format="dimension"/>
        <attr name="titleTextColor" format="color"/>
        <attr name="leftText" format="string"/>
        <attr name="leftBackground" format="reference|color"/>
        <attr name="leftTextColor" format="color"/>
        <attr name="rightText" format="string"/>
        <attr name="rightBackground" format="reference|color"/>
        <attr name="rightTextColor" format="color"/>
    </declare-styleable>
</resources>
```

_其中name中的名字随便定，format是属性 根据情况定。_

### 步骤2：实现我们的一个“View”

#### a.新建"Topbar.java"，实现构造方法。
> 建一个 **Topbar.java** ，extends RelativeLayout，同时实现构造方法。

* 如果是使用一般的自定义控件，不需要自定义属性。那么实现下面这个构造方法:`public Topbar(Context context)`

* 因为我们需要自定义属性，所以实现构造函数:`public Topbar(Context context, AttributeSet attrs)`

#### b.定义Topbar的控件和attrs.xml的属性
> 在Topbar.java里先定义一些Topbar的控件，比如 *button* *textview* 等，还要定义一些attrs.xml里设置的属性。 

```java
//定义Topbar里的控件
private Button leftButton,rightButton;
private TextView title;

//定义attrs.xml里的属性
private int leftTextColor;
private Drawable leftBackground;
private String leftText;

private int rightTextColor;
private Drawable rightBackground;
private String rightText;

private float titleTextSize;
private String title;
private int titleTextColor;
```
#### c.使用TypedArray
> 下面给我们自定义的控件和属性赋值，将控件和属性进行关联。 

(1).首先在构造方法中获得我们在attrs.xml文件中自定义的属性，并把这些属性值赋给控件。 

```java
//TypedArray 来存储在XML中的自定义属性的值
//ta中包含了所有属性和值的映射
TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.Topbar);

//接下来就可以从ta中获取到自定义属性的值了
leftText = ta.getString(R.styleable.TopBar_leftText);
/*
TypedArray的对象是通过键值对来存取的，所以需要key，
android中key是需要参考attrs.xml中我们定义的name，比如Topbar_leftTextColor这样的形式，
有的后面的0是定义的默认值
*/
leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, 0);
leftTextBackground = ta.getDrawable(R.styleable.TopBar_leftTextBackground);
rightText = ta.getString(R.styleable.TopBar_rightText);
rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, 0);
rightTextBackground= ta.getDrawable(R.styleable.TopBar_rightTextBackground);
titleText = ta.getString(R.styleable.TopBar_titleText); 
titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, 0);
titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize,0);

ta.recycle()//避免浪费资源、避免一些由于缓存引起的错误
```

(2).把Topbar里的控件Button、TextView与上面这些属性建立联系。

```java
//控件leftButton、rightButton、tvTitle 与 上面的属性 建立联系
leftButton = new Button(context);
rightButton = new Button(context);
tvTitle = new TextView(context);

leftButton.setText(leftText);
leftButton.setTextColor(leftTextColor);
leftButton.setBackground(leftTextBackground);

rightButton.setText(rightText);
rightButton.setTextColor(rightTextColor);
rightButton.setBackground(rightTextBackground);

tvTitle.setText(titleText);
tvTitle.setTextSize(titleTextSize);
tvTitle.setTextColor(titleTextColor);
tvTitle.setGravity(Gravity.CENTER);//文字居中

setBackgroundColor(0xFF59563);//给整个ViewGroup设置一个背景颜色
```

**小结：** _此时，我们已经定义好TopBar的button和title，也设置好了button和title的文字大小颜色之类的属性，接下来就是把button、title放入布局layout。_

#### d.使用LayoutParams
> 把控件放入布局layout，需要用到LayoutParams，在LayoutParams里设置控件以何种形式放入layout中。

```java
leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
ViewGroup.LayoutParams.WRAP_CONTENT);
leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);//TRUE不是指真假，而是RelativeLayout里定义的常量
//此时就设置好了leftButton的 width:WRAP_CONTENT 和height:WRAP_CONTENT，以及 左对齐。 
addView(leftButton,leftParams);//实现了左边Button的添加
```

**小结：**到此为止，TopBar的静态部分已经全部设置完成。

### 步骤3：引用我们的“View”
> 首先参考系统是如何引用一个View的：

```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="nicokitty.test_topbar.MainActivity">
    
    <TextView
	android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Hello World!" />
    
</RelativeLayout>
```

* 系统先声明一个控件的名字`TextView`
* 然后通过`android:`来引用一个属性，并给这个属性赋一个值，比如`"wrap_content"`。
* 点击`control`+`android:`会发现光标跳转到`xmlns:android="http://schemas.android.com/apk/res/android"`，其实这个`xmlns:`相当于java文件中的`import`，（xmlns:XML name space），`android="..."`是指引用系统的。

> 仿照系统的例子，引用Topbar

* 声明控件：`<nicokitty.test_topbar.Topbar>`，这是Topbar的完整包名。
* 然后“import”，引用第三方的name space，`xmlns:custom="http://schemas.android.com/apk/res-auto"`，其中`custom`是我们自己命名的，**需要注意的是：** _在Android Studio中我们使用`.../res-auto`引用第三方的name space._  

### 实现动态事件
以上几个步骤把静态的部分完全实现，接下来，我们设置左右按钮的动态事件。

#### 方法一：
直接在`Topbar.java`中设置leftButton和rightButton的点击事件。

```
//leftButton的点击事件
leftButton.setOnClickListener(new OnClickListener() {
@Override
public void onClick(View v) {
Toast.makeText(context,"LaLaLa",Toast.LENGTH_SHORT).show();
		}
	});
//rightButton的点击事件
rightButton.setOnClickListener(new OnClickListener() {
@Override
public void onClick(View v) {
Toast.makeText(context,"RaRaRa",Toast.LENGTH_SHORT).show();
		}
	});
```

#### 方法二：
利用==**接口回调机制**==。

1. 在`Topbar.java`中定义接口`topbarOnClickListener{}`:

	```java
	public interface topbarClickListener{
	  	 public void leftClick();
	  	 public void rightClick();
	}
	```
2. 接着定义`setOnTopbarClickListener()`，并将接口`topBarClickListener`作为参数传递进去。

	```java
	private topbarClickListener listener;
	
	publicvoid setOnTopbarClickListener(topbarClickListener listener){
			this.listener = listener;
	}
	```
	
3. 然后设置左右按钮的点击事件，调用setOnClickListener()方法。

	```java
	leftButton.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {
			listener.leftClick();
		}
	});
	
	rightButton.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {
			listener.rightClick();
		}
	});
	```

4. 在MainActivity.java中实例化Topbar，然后调用我们定义的`setOnTopbarClickListener()`，因为之前把接口`topBarClickListener`作为参数传递了进去，所以我们直接new topbarClickListener()：

	```java
	Topbar topbar = (Topbar) findViewById(R.id.topbar);
	topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
		@Override
		public void leftClick() {
			Toast.makeText(MainActivity.this, "LaLaLa", Toast.LENGTH_SHORT).show(oast.makeText(MainActivity.this, "LaLaLa", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void rightClick() {
			Toast.makeText(MainActivity.this, "LaLaLa", Toast.LENGTH_SHORT).show();
		}
	});
	```
	
## 总结

自定义View需要注意以下几点：

1. 查看系统源代码是如何定义的View。
2. 自定义属性时的`format`。
3. 获取XML文件里的属性时要用到`TypedArray`，同时，使用完TypedArray之后要用`ta.recycle()`进行回收。
4. 别忘记把Topbar里的控件Button、TextView与属性建立联系。
5. 使用LayoutParams把控件放入布局layout。
6. 引用View时，使用`xmlns:随便起的名字="..."`引用第三方name space。
7. 在设置点击事件时，学会接口回调机制。


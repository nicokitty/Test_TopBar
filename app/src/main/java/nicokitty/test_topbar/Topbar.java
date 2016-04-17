package nicokitty.test_topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by niuxiangqian on 16/4/12.
 */
public class Topbar extends RelativeLayout {
    //定义Topbar里的控件
    private Button leftButton;
    private Button rightButton;
    private TextView tvTitle;

    //定义attrs.xml里的属性
    private String titleText;
    private int titleColor;
    private float titleTextSize;

    private String leftText;
    private int leftTextColor;
    private Drawable leftTextBackground;

    private String rightText;
    private int rightTextColor;
    private Drawable rightTextBackground;

    //把设置好的button、tvTitle放入layout布局中，需要用到LayoutParams类
    private LayoutParams leftParams,rightParams, titleParams;

    /*
    用接口回调
     */
    private topbarOnClickListener listener;
    //定义接口
    public interface topbarOnClickListener{
        public void leftClick();
        public void rightClick();
    }
    public void setOnTopbarClickListener(topbarOnClickListener listener){
        this.listener = listener;
    }

    public Topbar(final Context context, AttributeSet attrs) {
        super(context, attrs);

        //TypedArray 来存储在XML中的自定义属性的值
        //ta中包含了所有属性和值的映射
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

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
        titleColor = ta.getColor(R.styleable.TopBar_titleColor, 0);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize,0);

        ta.recycle();//避免浪费资源、避免一些由于缓存引起的错误

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
        tvTitle.setTextColor(titleColor);
        tvTitle.setGravity(Gravity.CENTER);//文字居中

        setBackgroundColor(0xFFF59563);//给整个ViewGroup设置背景颜色

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);//TRUE不是指真假，而是RelativeLayout里定义的常量
        //此时就设置好了leftButton的 width:WRAP_CONTENT 和 height:WRAP_CONTENT，以及 左对齐。

        addView(leftButton,leftParams);//实现了左边Button的添加

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);//TRUE不是指真假，而是RelativeLayout里定义的常量
        //此时就设置好了rightButton的 width:WRAP_CONTENT 和 height:WRAP_CONTENT，以及 右对齐。

        addView(rightButton,rightParams);//实现了右边Button的添加

        titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);//TRUE不是指真假，而是RelativeLayout里定义的常量
        //此时就设置好了titleButton的 width:MATCH_PARENT 和 height:MATCH_PARENT，以及 居中对齐。

        addView(tvTitle,titleParams);

        /////////////////////////////////////////////////////////////////////////////////

        /**
         * 左右按钮的点击事件
         * 方法一：
         */
        /*
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"LaLaLa",Toast.LENGTH_SHORT).show();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"RaRaRa",Toast.LENGTH_SHORT).show();
            }
        });
        */

        /**
         * 方法二：
         * 利用接口回调机制
         */
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });

    }


}

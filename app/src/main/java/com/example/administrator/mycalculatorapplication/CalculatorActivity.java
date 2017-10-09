package com.example.administrator.mycalculatorapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.administrator.mycalculatorapplication.InputItem.InputType;

import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends Activity implements OnClickListener{

    private TextView mShowResultTv;  //显示结果
    private TextView mShowInputTv;   //显示输入的字符
    //数字按钮
    private Button mZeroButton,mOnebtn,mTwoBtn,mThreeBtn,mFourBtn,mFiveBtn,mSixBtn,mSevenBtn,mEightBtn,mNineBtn;
    //加减乘除按钮
    private Button mAddBtn,mSubBtn,mMultiplyBtn,mDividebtn;
    //其他按钮
    private Button mPointtn,mEqualBtn,mCBtn,mDelBtn;

    private HashMap<View,String> map; //将View和String映射起来
    private List<InputItem> mInputList; //定义记录每次输入的数
    private int mLastInputstatus = INPUT_NUMBER; //记录上一次输入状态
    public static final int INPUT_NUMBER = 1;
    public static final int INPUT_POINT = 0;
    public static final int INPUT_OPERATOR = -1;
    public static final int END = -2;
    public static final int ERROR= -3;
    public static final int SHOW_RESULT_DATA = 1;
    public static final String nan = "NaN";
    public static final String infinite = "∞";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        //更新UI
        public void handleMessage(Message msg) {

            if(msg.what == SHOW_RESULT_DATA){
                mShowResultTv.setText(mShowInputTv.getText());
                mShowInputTv.setText(mInputList.get(0).getInput());
                clearScreen(mInputList.get(0));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        //初始化视图
        initView();
        //初始化数据
        initData();
    }
    /**
     * 初始化view
     */
    private void initView() {
        mShowResultTv = (TextView) this.findViewById(R.id.show_result_tv);
        mShowInputTv = (TextView)this.findViewById(R.id.show_input_tv);
        mCBtn = (Button)this.findViewById(R.id.c_btn);
        mDelBtn= (Button)this.findViewById(R.id.del_btn);
        mAddBtn= (Button)this.findViewById(R.id.add_btn);
        mMultiplyBtn= (Button)this.findViewById(R.id.multiply_btn);
        mDividebtn= (Button)this.findViewById(R.id.divide_btn);
        mZeroButton = (Button)this.findViewById(R.id.zero_btn);
        mOnebtn= (Button)this.findViewById(R.id.one_btn);
        mTwoBtn= (Button)this.findViewById(R.id.two_btn);
        mThreeBtn= (Button)this.findViewById(R.id.three_btn);
        mFourBtn= (Button)this.findViewById(R.id.four_btn);
        mFiveBtn= (Button)this.findViewById(R.id.five_btn);
        mSixBtn= (Button)this.findViewById(R.id.six_btn);
        mSevenBtn= (Button)this.findViewById(R.id.seven_btn);
        mEightBtn= (Button)this.findViewById(R.id.eight_btn);
        mNineBtn= (Button)this.findViewById(R.id.nine_btn);
        mPointtn= (Button)this.findViewById(R.id.point_btn);
        mEqualBtn= (Button)this.findViewById(R.id.equal_btn);
        mSubBtn = (Button)this.findViewById(R.id.sub_btn);
        setOnClickListener();//调用监听事件

    }
    /**
     * 初始化数据
     */
    private void initData() {
        if(map == null)
            map = new HashMap<View, String>();
        map.put(mAddBtn,getResources().getString(R.string.add));
        map.put(mMultiplyBtn,getResources().getString(R.string.multply));
        map.put(mDividebtn,getResources().getString(R.string.divide));
        map.put(mSubBtn, getResources().getString(R.string.sub));
        map.put(mZeroButton ,getResources().getString(R.string.zero));
        map.put(mOnebtn,getResources().getString(R.string.one));
        map.put(mTwoBtn,getResources().getString(R.string.two));
        map.put(mThreeBtn,getResources().getString(R.string.three));
        map.put(mFourBtn,getResources().getString(R.string.four));
        map.put(mFiveBtn,getResources().getString(R.string.five));
        map.put(mSixBtn,getResources().getString(R.string.six));
        map.put(mSevenBtn,getResources().getString(R.string.seven));
        map.put(mEightBtn,getResources().getString(R.string.eight));
        map.put(mNineBtn,getResources().getString(R.string.nine));
        map.put(mPointtn,getResources().getString(R.string.point));
        map.put(mEqualBtn,getResources().getString(R.string.equal));
        mInputList = new ArrayList<InputItem>();
        mShowResultTv.setText("");
        //清屏
        clearAllScreen();
    }

    /**
     * 设置监听事件
     */
    private void setOnClickListener() {
        mCBtn.setOnClickListener(this);
        mDelBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mMultiplyBtn.setOnClickListener(this);
        mDividebtn.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
        mZeroButton.setOnClickListener(this);
        mOnebtn.setOnClickListener(this);
        mTwoBtn.setOnClickListener(this);
        mThreeBtn.setOnClickListener(this);
        mFourBtn.setOnClickListener(this);
        mFiveBtn.setOnClickListener(this);
        mSixBtn.setOnClickListener(this);
        mSevenBtn.setOnClickListener(this);
        mEightBtn.setOnClickListener(this);
        mNineBtn.setOnClickListener(this);
        mPointtn.setOnClickListener(this);
        mEqualBtn.setOnClickListener(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.c_btn:
                //清除
                clearAllScreen();
                break;
            case R.id.del_btn:
                //删除
                back();
                break;
            case R.id.point_btn:
                //输入点
                inputPoint(arg0);
                break;
            case R.id.equal_btn:
                //运算符
                operator();
                break;
            //加减乘除
            case R.id.add_btn:
            case R.id.sub_btn:
            case R.id.multiply_btn:
            case R.id.divide_btn:
                inputOperator(arg0);
                break;
            default:
                inputNumber(arg0);
                break;
        }
    }
    /**
     * 点击=之后开始运算
     */
    private void operator() {
        if(mLastInputstatus == END ||mLastInputstatus == ERROR || mLastInputstatus == INPUT_OPERATOR|| mInputList.size()==1){
            return;
        }
        mShowResultTv.setText("");
        //实现动画
        startAnim();
        //实现高级运算
        findHighOperator(0);
        if(mLastInputstatus != ERROR){
            findLowOperator(0);
        }
        //延时显示数据
        mHandler.sendMessageDelayed(mHandler.obtainMessage(SHOW_RESULT_DATA), 200);
    }
    //开始动画
    private void startAnim(){
        mShowInputTv.setText(mShowInputTv.getText()+getResources().getString(R.string.equal));
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.screen_anim);
        mShowInputTv.startAnimation(anim);
    }
    /**
     * 输入点
     * @param view
     */
    private void inputPoint(View view) {
        if(mLastInputstatus == INPUT_POINT){
            return;
        }
        if(mLastInputstatus == END || mLastInputstatus == ERROR){
            clearInputScreen();
        }
        String key = map.get(view);
        String input = mShowInputTv.getText().toString();
        //如果点的后面是运算符，则在运算结果添加0
        if(mLastInputstatus == INPUT_OPERATOR){
            input = input+"0";
        }
        mShowInputTv.setText(input+key);
        addInputList(INPUT_POINT, key);
    }
    /**
     * 输入数字
     * @param view
     */
    private void inputNumber(View view){
        if(mLastInputstatus == END || mLastInputstatus == ERROR){
            clearInputScreen();
        }
        String key = map.get(view);
        if("0".equals(mShowInputTv.getText().toString())){
            mShowInputTv.setText(key);
        }else{
            mShowInputTv.setText(mShowInputTv.getText() + key);
        }
        addInputList(INPUT_NUMBER, key);
    }
    /**
     * 输入运算符
     * @param view
     */
    private void inputOperator(View view) {
        if(mLastInputstatus == INPUT_OPERATOR || mLastInputstatus == ERROR){
            return;
        }
        if(mLastInputstatus == END){
            mLastInputstatus = INPUT_NUMBER;
        }

        String key = map.get(view);
        if("0".equals(mShowInputTv.getText().toString())){
            mShowInputTv.setText("0"+key);
            mInputList.set(0,new InputItem("0",InputItem.InputType.INT_TYPE));
        }else{
            mShowInputTv.setText(mShowInputTv.getText() + key);
        }
        addInputList(INPUT_OPERATOR, key);
    }
    /**
     * 回退操作
     */
    private void back() {
        if(mLastInputstatus == ERROR){
            clearInputScreen();
        }
        String str = mShowInputTv.getText().toString();
        if(str.length() != 1){
            mShowInputTv.setText(str.substring(0, str.length()-1));
            backList();
        }else{
            mShowInputTv.setText(getResources().getString(R.string.zero));
            clearScreen(new InputItem("",InputItem.InputType.INT_TYPE));
        }
    }
    /**
     * 回退InputList操作
     */
    private void backList() {
        InputItem item = mInputList.get(mInputList.size() - 1);
        if (item.getType() == InputItem.InputType.INT_TYPE) {
            //获取到最后一个item,并去掉最后一个字符
            String input = item.getInput().substring(0,
                    item.getInput().length() - 1);
            //如果截完了，则移除这个item，并将当前状态改为运算操作符
            if ("".equals(input)) {
                mInputList.remove(item);
                mLastInputstatus = INPUT_OPERATOR;
            } else {
                //否则设置item为截取完的字符串，并将当前状态改为number
                item.setInput(input);
                mLastInputstatus = INPUT_NUMBER;
            }
            //如果item是运算操作符 则移除。
        } else if (item.getType() == InputItem.InputType.OPERATOR_TYPE) {
            mInputList.remove(item);
            if (mInputList.get(mInputList.size() - 1).getType() == InputItem.InputType.INT_TYPE) {
                mLastInputstatus = INPUT_NUMBER;
            } else {
                mLastInputstatus = INPUT_POINT;
            }
            //如果当前item是小数
        } else {
            String input = item.getInput().substring(0,
                    item.getInput().length() - 1);
            if ("".equals(input)) {
                mInputList.remove(item);
                mLastInputstatus = INPUT_OPERATOR;
            } else {
                if (input.contains(".")) {
                    item.setInput(input);
                    mLastInputstatus = INPUT_POINT;
                } else {
                    item.setInput(input);
                    mLastInputstatus = INPUT_NUMBER;
                }
            }
        }
    }
    //清理屏
    private void clearAllScreen() {

        clearResultScreen();
        clearInputScreen();

    }
    //清除结果
    private void clearResultScreen(){
        mShowResultTv.setText("");
    }
    //清除输入
    private void clearInputScreen() {
        mShowInputTv.setText(getResources().getString(R.string.zero));
        mLastInputstatus = INPUT_NUMBER;
        mInputList.clear();
        mInputList.add(new InputItem("", InputItem.InputType.INT_TYPE));
    }
    //计算完成
    private void clearScreen(InputItem item) {
        if(mLastInputstatus != ERROR){
            mLastInputstatus = END;
        }
        mInputList.clear();
        mInputList.add(item);
    }

    //实现高级运算
    public int findHighOperator(int index) {
        if (mInputList.size() > 1 && index >= 0 && index < mInputList.size())
            for (int i = index; i < mInputList.size(); i++) {
                InputItem item = mInputList.get(i);
                if (getResources().getString(R.string.divide).equals(item.getInput())
                        || getResources().getString(R.string.multply).equals(item.getInput())) {
                    int a,b; double c,d;
                    if(mInputList.get(i - 1).getType() == InputItem.InputType.INT_TYPE){
                        a = Integer.parseInt(mInputList.get(i - 1).getInput());
                        if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
                            b = Integer.parseInt(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.multply).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(a * b),InputItem.InputType.INT_TYPE));
                            }else{
                                //如果除数为0，则设置最后的状态为ERROR
                                if(b == 0){
                                    mLastInputstatus = ERROR;
                                    //判断被除数是否为0
                                    if(a==0){
                                        clearScreen(new InputItem(nan,InputType.ERROR));
                                    }else{
                                        clearScreen(new InputItem(infinite,InputType.ERROR));
                                    }
                                    return -1;
                                }else if(a % b != 0){   //如果不能整除，则将结果强转为浮点型
                                    mInputList.set(i - 1,new InputItem(String.valueOf((double)a / b),InputItem.InputType.DOUBLE_TYPE));
                                }else{
                                    mInputList.set(i - 1,new InputItem(String.valueOf((Integer)a / b),InputItem.InputType.INT_TYPE));
                                }
                            }
                        }else{ //如果输入的是浮点型
                            d = Double.parseDouble(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.multply).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(a * d),InputItem.InputType.DOUBLE_TYPE));
                            }else{
                                if(d == 0){
                                    mLastInputstatus = ERROR;
                                    if(a==0){
                                        clearScreen(new InputItem(nan,InputType.ERROR));
                                    }else{
                                        clearScreen(new InputItem(infinite,InputType.ERROR));
                                    }
                                    return -1;
                                }
                                mInputList.set(i - 1,new InputItem(String.valueOf(a / d),InputItem.InputType.DOUBLE_TYPE));
                            }
                        }
                    }else{
                        c = Double.parseDouble(mInputList.get(i-1).getInput());
                        if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
                            b = Integer.parseInt(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.multply).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(c* b),InputItem.InputType.DOUBLE_TYPE));
                            }else{
                                if(b== 0){
                                    mLastInputstatus = ERROR;
                                    if(c==0){
                                        clearScreen(new InputItem(nan,InputType.ERROR));
                                    }else{
                                        clearScreen(new InputItem(infinite,InputType.ERROR));
                                    }
                                    return -1;
                                }
                                mInputList.set(i - 1,new InputItem(String.valueOf(c / b),InputItem.InputType.DOUBLE_TYPE));
                            }
                        }else{
                            d = Double.parseDouble(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.multply).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(mul(c,d)),InputItem.InputType.DOUBLE_TYPE));
                            }else{
                                if(d == 0){
                                    mLastInputstatus = ERROR;
                                    if(c==0){
                                        clearScreen(new InputItem(nan,InputType.ERROR));
                                    }else{
                                        clearScreen(new InputItem(infinite,InputType.ERROR));
                                    }
                                    return -1;
                                }
                                mInputList.set(i - 1,new InputItem(String.valueOf(div(c, d)),InputItem.InputType.DOUBLE_TYPE));
                            }
                        }
                    }
                    mInputList.remove(i + 1);
                    mInputList.remove(i);
                    return findHighOperator(i);
                }
            }
        return -1;

    }

    public int findLowOperator(int index) {
        if (mInputList.size()>1 && index >= 0 && index < mInputList.size())
            for (int i = index; i < mInputList.size(); i++) {
                InputItem item = mInputList.get(i);
                if (getResources().getString(R.string.sub).equals(item.getInput())
                        || getResources().getString(R.string.add).equals(item.getInput())) {
                    int a,b; double c,d;
                    if(mInputList.get(i - 1).getType() == InputItem.InputType.INT_TYPE){
                        a = Integer.parseInt(mInputList.get(i - 1).getInput());
                        if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
                            b = Integer.parseInt(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.add).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(a + b),InputItem.InputType.INT_TYPE));
                            }else{
                                mInputList.set(i - 1,new InputItem(String.valueOf(a - b),InputItem.InputType.INT_TYPE));
                            }
                        }else{
                            d = Double.parseDouble(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.add).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(a + d),InputItem.InputType.DOUBLE_TYPE));
                            }else{
                                mInputList.set(i - 1,new InputItem(String.valueOf(a - d),InputItem.InputType.DOUBLE_TYPE));
                            }
                        }
                    }else{
                        c = Double.parseDouble(mInputList.get(i-1).getInput());
                        if(mInputList.get(i + 1).getType() == InputItem.InputType.INT_TYPE){
                            b = Integer.parseInt(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.add).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(c + b),InputItem.InputType.DOUBLE_TYPE));
                            }else{
                                mInputList.set(i - 1,new InputItem(String.valueOf(c - b),InputItem.InputType.DOUBLE_TYPE));
                            }
                        }else{
                            d = Double.parseDouble(mInputList.get(i + 1).getInput());
                            if(getResources().getString(R.string.add).equals(item.getInput())){
                                mInputList.set(i - 1,new InputItem( String.valueOf(add(c, d)),InputItem.InputType.DOUBLE_TYPE));
                            }else{
                                mInputList.set(i - 1,new InputItem(String.valueOf(sub(c,d)),InputItem.InputType.DOUBLE_TYPE));
                            }
                        }
                    }
                    mInputList.remove(i + 1);
                    mInputList.remove(i);
                    return findLowOperator(i);
                }
            }
        return -1;

    }
    //设置当前状态  9  "9" "+"
    void addInputList(int currentStatus,String inputChar){
        switch (currentStatus) {
            case INPUT_NUMBER:    //输入数字
                if(mLastInputstatus == INPUT_NUMBER){
                    InputItem item = (InputItem)mInputList.get(mInputList.size()-1);
                    item.setInput(item.getInput()+inputChar);
                    item.setType(InputItem.InputType.INT_TYPE);
                    mLastInputstatus = INPUT_NUMBER;
                }else if(mLastInputstatus == INPUT_OPERATOR){
                    InputItem item = new InputItem(inputChar, InputItem.InputType.INT_TYPE);
                    mInputList.add(item);
                    mLastInputstatus = INPUT_NUMBER;
                }else if(mLastInputstatus == INPUT_POINT){
                    InputItem item = (InputItem)mInputList.get(mInputList.size()-1);
                    item.setInput(item.getInput()+inputChar);
                    item.setType(InputItem.InputType.DOUBLE_TYPE);
                    mLastInputstatus = INPUT_POINT;
                }
                break;
            case INPUT_OPERATOR:    //输入操作符
                InputItem item = new InputItem(inputChar, InputItem.InputType.OPERATOR_TYPE);
                mInputList.add(item);
                mLastInputstatus = INPUT_OPERATOR;
                break;
            case INPUT_POINT://输入点
                if(mLastInputstatus == INPUT_OPERATOR){
                    InputItem item1 =  new InputItem("0"+inputChar,InputItem.InputType.DOUBLE_TYPE);
                    mInputList.add(item1);
                    mLastInputstatus = INPUT_POINT;
                }else{
                    InputItem item1 = (InputItem)mInputList.get(mInputList.size()-1);
                    item1.setInput(item1.getInput()+inputChar);
                    item1.setType(InputItem.InputType.DOUBLE_TYPE);
                    mLastInputstatus = INPUT_POINT;
                }
                break;
        }
    }

    //进行除法运算
    public static Double div(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2,10,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    //进行减法运算
    public static Double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }
     //进行加法运算
    public static Double add(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }
    //进行乘法运算
    public static Double mul(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }
}
package com.example.week2_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView tv;//显示框
    StringBuilder viewStr = new StringBuilder();  //显示框字符串

    int[] numBt; //按键数字
    StringBuilder numStr = new StringBuilder(); //键入的数字

    //运算符 + - * / = .
    Button bt_add;
    Button bt_sub;
    Button bt_mul;
    Button bt_div;
    Button bt_eq;
    Button bt_pt;
    Boolean flag = false;  //当前前一个字符是否为数字

    Button bt_c;//清楚按键
    Button bt_back;//退回按钮

    Button bt_mc;
    Button bt_mr;
    Button bt_madd;
    Button bt_msub;
    String result;//计算结果

    String lastRes;
    List<String> bufferList;//m的缓存器

    //中缀,ArrayList模拟队列
    List<String> arrList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        bufferList = new ArrayList<String>();
        bufferList.add("0");
        //初始化中缀list
        arrList =new ArrayList<String>();
        //arrList.add("0");
        //初始化按钮
        initButton();
        //初始化按钮监听
        buttonListener();
        //
        }

    //初始化按钮资源
    public void initButton(){
        tv = (TextView)findViewById(R.id.tv);
        bt_add = (Button)findViewById(R.id.add);
        bt_sub = (Button)findViewById(R.id.sub);
        bt_mul =(Button)findViewById(R.id.mul);
        bt_div = (Button)findViewById(R.id.div);
        bt_eq = (Button)findViewById(R.id.eq);
        bt_pt = (Button)findViewById(R.id.pt);
        numBt = new int[]{
                R.id.bt0,R.id.bt1,R.id.bt2,R.id.bt3,R.id.bt4,
                R.id.bt5,R.id.bt6,R.id.bt7,R.id.bt8,R.id.bt9
        };
        bt_c = (Button)findViewById(R.id.C);
        bt_back = (Button)findViewById(R.id.back);
        bt_mc = (Button)findViewById(R.id.MC);
        bt_mr = (Button)findViewById(R.id.Mr);
        bt_madd = (Button)findViewById(R.id.Madd);
        bt_msub = (Button)findViewById(R.id.Msub);
    }
    //初始化按钮和监听
    public void buttonListener(){
        //System.out.println("初始化按钮和监听");
        //初始获取监听
        //数字按键
        for (int i = 0; i < numBt.length;i++ ) {
            Button temp = (Button)findViewById(numBt[i]);
            addNumListener(temp,String.valueOf(i));
        }
        addNumListener(bt_pt,".");
        //字符按键
        addOpListener(bt_add,"+");
        addOpListener(bt_sub, "-");
        addOpListener(bt_mul, "*");
        addOpListener(bt_div, "/");
        //清除按键
        bt_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrList.clear();//需计算的队列
                numStr.delete(0,numStr.length());//输入的数据
                viewStr.delete(0,viewStr.length());//删除显示
                tv.setText(viewStr);
            }
        });
        //退后按键
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    arrList.add(numStr.toString());
                    numStr.delete(0, numStr.length());
                    arrList.remove(arrList.size() - 1);
                    viewStr.delete(viewStr.length() - 1, viewStr.length());
                    tv.setText(viewStr);
                }catch (RuntimeException e){
                    System.out.println("没有数据,无法退后");
                }
            }
        });
        //=按键
        bt_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    arrList.add(numStr.toString());
                    numStr.delete(0, numStr.length());
                    tv.setText(getResults(arrList));//计算结果
                    arrList.clear();//清空计算队列
                    viewStr.delete(0, viewStr.length());//清空显示屏
                    flag =false;
                }catch (RuntimeException e){
                    System.out.println("没有输入数据");
                }
            }
        });
        addMopListener(bt_madd,"+");//M+
        addMopListener(bt_msub,"-");//M-
        //Mr,显示缓存队列的结果
        bt_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(bufferList.get(0));
            }
        });
        //MC清除缓存队列
        bt_mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bufferList.clear();
                bufferList.add("0");
            }
        });
    }

    //添加M-和M+的监听
    public void addMopListener(Button bt,final  String op){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastRes = result;
                bufferList.add(op);
                bufferList.add(lastRes); //记忆进入缓存队列
                String temp = getResults(bufferList);
                bufferList.clear();
                bufferList.add(temp);
            }
        });

    }

    //添加数字按键监听
    public void addNumListener(Button bt,final String num ){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(num);
                numStr.append(num);
                viewStr.append(num);
                tv.setText(viewStr);//按下即显示
                flag = true;
            }
        });
    }
    //添加运算符按键监听
    public void addOpListener(Button bt,final String nowChar){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrList.isEmpty()&&!flag) {//运算符为首位是添加0
                    numStr.append(0);
                    flag = true;
                }
                //前面是运算符
                if(!flag ){
                    System.out.println("前方运算符");
                    //替换队尾运算符
                    arrList.remove(arrList.size() - 1);
                    arrList.add(nowChar);
                    //替换显示框
                    //System.out.println("替换");
                    viewStr.delete(viewStr.length() - 1, viewStr.length());
                    viewStr.append(nowChar);
                    tv.setText(viewStr);
                    flag = false;
                }//前面是数字或.
                else {
                    //前方输入数据入队
                    arrList.add(numStr.toString());
                    System.out.println("数字入队"+numStr);
                    numStr.delete(0,numStr.length());
                    System.out.println(nowChar+"(前方数字)");
                    arrList.add(nowChar);//入队
                    viewStr.append(nowChar);//入显示框
                    tv.setText(viewStr); //显示
                    flag = false;
                }
            }
        });
    }

    //计算结果，中缀转后缀，后缀输出结果
    public String getResults(List<String> infixList){
        System.out.println("InfixList"+arrList.toString());
        CalculatorResults cr = new CalculatorResults(infixList);
        //中缀转后缀
        cr.infixParseToSuffix();
        //计算结果
        cr.calculateSuffix();
        result = String.valueOf(cr.getResult());
        //去掉多于的0
        if(result.indexOf(".") > 0){
            result = result.replaceAll("0+?$", "");//去掉多余的0
            result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return result;
    }


}

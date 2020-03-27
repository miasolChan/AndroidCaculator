package com.example.week2_calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorResults {

    private List<String> infixList;//中缀
    private List<String> suffixList = new ArrayList<>();//后缀
    private float result;

    public CalculatorResults(List firstList) {
        this.infixList = firstList;
    }

    public float getResult() {
        return result;
    }

    //中缀转后缀，无括号
    public List<String> infixParseToSuffix(){
        //栈保存运算符
        Stack<String> opStack = new Stack<>();
        //链表保存后缀
        //
        for(int i = 0; i < infixList.size();i++){
            String item = String.valueOf(infixList.get(i));
            //判断是数还是操作符
            //如果是操作符
            if(isOperator(item)) {
                //栈空 或 当前操作符大于栈顶操作符直接入栈
                if (opStack.isEmpty() || priority(item) > priority(opStack.peek()))
                {
                    System.out.println(item+"入栈");
                    opStack.push(item);
                }
                //否则将栈中元素出栈入list，直到遇到大于当前操作符
                else
                {
                    while (!opStack.isEmpty()) {
                        if (priority(item) <= priority(opStack.peek())) {
                            suffixList.add(opStack.pop());
                        }
                    }
                    opStack.push(item);
                }
            }
            //如果是数字
            else
            {
                //是数字则直接入list
                System.out.println(item+"入队");
                suffixList.add(item);
            }
        }
        //表达式循环完毕，如果操作符栈中元素不为空，将栈中元素出栈入队
        while (!opStack.isEmpty())
        {
            suffixList.add(opStack.pop());
        }
        System.out.println("suffixList"+suffixList.toString());
        return suffixList;
    }
    //是否操作符
    private boolean isOperator(String op){
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
    }
    //操作符优先级
    private static int priority(String op){
        if(op.equals("*") || op.equals("/")){
            return 1;
        }else if(op.equals("+") || op.equals("-")){
            return 0;
        }
        return -1;
    }
    //后缀计算
    public void calculateSuffix(){
        Stack<Float> stack = new Stack<>();
        for(String item: suffixList){
            if(!isOperator(item)){
                stack.push(Float.parseFloat(item));
            }else {
                //取栈顶
                float num2 = stack.pop();
                float num1 = stack.pop();
                float res = 0;
                //计算
                if(item.equals("+")){
                    res = num1 + num2;
                }else if(item.equals("-")){
                    res = num1 - num2;
                }else if(item.equals("*")){
                    res = num1 * num2;
                }else if(item.equals("/")){
                    res = num1 / num2;
                }
                stack.push(res);
            }
        }
        result = stack.pop();
        System.out.println(result);
    }

}

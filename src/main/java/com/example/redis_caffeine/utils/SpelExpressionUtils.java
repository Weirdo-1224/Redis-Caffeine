package com.example.redis_caffeine.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.TreeMap;

/**
 * @author 周同学
 */
public class SpelExpressionUtils {

    /**
     * 解析 SpEL 表达式并替换变量
     * @param elString 表达式（如 "user.name"）
     * @param map 变量键值对
     * @return 解析后的字符串
     */
    public static String parse(String elString, TreeMap<String, Object> map) {
        // 将输入的表达式包装为 SpEL 表达式格式
        elString = String.format("#{%s}", elString);
        // 创建 SpEL 表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 创建标准的评估上下文，用于存储变量
        EvaluationContext context = new StandardEvaluationContext();
        // 将传入的变量键值对设置到评估上下文中
        map.forEach(context::setVariable);
        // 使用解析器解析表达式，使用模板解析上下文
        Expression expression = parser.parseExpression(elString, new TemplateParserContext());
        // 在指定上下文中计算表达式的值，并将结果转换为字符串返回
        return expression.getValue(context, String.class);
    }
}

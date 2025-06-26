package com.idme.util;

import com.alibaba.fastjson.JSONObject;

public class ConstraintUtil {

    /**
     * 根据字段类型生成对应的 constraint 默认值
     * @param type 字段类型，支持 STRING、INTEGER、DECIMAL
     * @return 默认 constraint 的 JSON 字符串
     */
    public static String getDefaultConstraint(String type) {
        JSONObject constraint = new JSONObject();

        // 通用字段
        constraint.put("multiValue", false);
        constraint.put("notnull", false);
        constraint.put("variable", true);
        constraint.put("stockInDB", true);
        constraint.put("graphIndex", false);
        constraint.put("graphLabel", "");
        constraint.put("secretLevel", "internal");
        constraint.put("associationType", "STRONG");
        constraint.put("index", false);
        constraint.put("optionalValue", "LEGAL_VALUE_TYPE");
        constraint.put("legalValueType", "");
        constraint.put("encryption", false);

        switch (type.toUpperCase()) {
            case "STRING":
                constraint.put("length", "200");
                constraint.put("caseMode", "DEFAULT");
                break;
            case "INTEGER":
                // 可选范围 constraint.put("range", "min=0;max=100");
                break;
            case "DECIMAL":
                constraint.put("precision", "2");
                // 可选范围 constraint.put("range", "min=0.00;max=1000.00");
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }

        return constraint.toJSONString();
    }
}
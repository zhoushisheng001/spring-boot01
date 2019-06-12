package com.zhuguang.zhou.common.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhuguang.zhou.common.tools.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public  class RuleMapperMethodGenerator {
    private static Logger logger = LoggerFactory.getLogger(RuleMapperMethodGenerator.class);
    private List<DataBaseRule> dataBaseRuleList;

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static  String  RULE_JSON = "mysqlRule.json";
    private static Resource resource = null;
    private static  Map<String,String> map = new HashMap<String,String>();
    /**
     * 初始化分库分表的规则
     */
    public static void initRule(String dataBaseName)
    {
        resource = resourceLoader.getResource(RULE_JSON);
        if(resource != null){
            String ruleJson = null;
            try {
                ruleJson = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                        .lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {

                logger.error("initRule  not found "+RULE_JSON);
            }
            logger.info("dataBaseName init =" + dataBaseName);
            logger.info("ruleJson init = " + ruleJson);
            if(StringUtils.isNotEmpty(ruleJson)){
                List<DataBaseRule> dataBaseRules = JsonUtils.serializable(ruleJson,new TypeReference<List<DataBaseRule>>(){});
                dataBaseRules.parallelStream().forEach(dataBaseRule -> {
                    if(dataBaseName.equals(dataBaseRule.getDatabasename())){
                        List<TableRule>  tableRuleList = dataBaseRule.getTables();
                        if(!CollectionUtils.isEmpty(tableRuleList)){
                            tableRuleList.parallelStream().forEach(tableRule -> {
                                map.put(tableRule.getName(),tableRule.getColumn());
                            });
                        }
                    }
                });
            }
        }
    }

    /**
     * 得到表的分片字段
     * @param table
     * @return
     */
    public static String  getRuleByTable(String table){
       return map.get(table);
    }
}

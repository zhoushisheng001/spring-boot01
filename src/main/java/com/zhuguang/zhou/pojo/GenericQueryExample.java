package com.zhuguang.zhou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.kuoke.platform.message.Response;
import net.kuoke.platform.pdgp.ServiceP002;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通用查询
 */
public class GenericQueryExample {
    private static final Logger logger = LoggerFactory.getLogger(GenericQueryExample.class);

    public static final String NULL_VALUE = "null";

    private static final String DEFAULT_PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String IS_EQUAL = "equal";
    public static final String IS_NOT_EQUAL = "not_equal";
    public static final String IS_GREATERTHAN = "greaterthan";
    public static final String IS_GREATERTHANOREQUAL = "greaterthanorequal";
    public static final String IS_LESSTHAN = "lessthan";
    public static final String IS_LESSTHANOREQUAL = "lessthanorequal";
    public static final String IS_CONTAIN = "contain";
    public static final String IS_NOTCONTAIN = "not_contain";
    public static final String IS_BETWEEN = "between";
    public static final String IS_NOT_BETWEEN = "not_between";
    /**
     * 或
     */
    public static final String  CONDITIONOPERATION_OR = "or";
    /**
     * 与
     */
    public static final String  CONDITIONOPERATION_AND = "and";

    public static final String TYPE_DATE = "date";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_ENUM ="enum";
    private  static final  String message = "非法请求!";

    private static  final String  frontBracketReg = "[(|\\s]{0,}$";

    private static final String postBracketsReg =  "[)|\\s]{0,}$";
    public static final String REGEX = "[\\w|-|_｜.]*";
    /**
     * 查询对象
     */
    private List<GenericQuery> vos;

    @JsonIgnore
    protected List<Criteria> genericCriteria;
    /**
     * 快捷查询条件编码
     */
    private String queryConditionCode;

    public GenericQueryExample() {
        genericCriteria = new ArrayList<Criteria>();
    }

    public void initGenericQuery() {
        if (vos != null) {
            Criteria criteria = createCriteria();
            int size = vos.size();
//            String conditionOperation = CONDITIONOPERATION_OR;
//            Criteria superCriteria = null;

            for (int i = 0; i < size; i++) {
                GenericQuery genericQuery = vos.get(i);
                genericCriteria(criteria, genericQuery);
              /*  if (i > 0) {
                    Criteria childCriteria = null;
                    conditionOperation = genericQuery.getConditionOperation();
                    if (StringUtils.equals(conditionOperation,CONDITIONOPERATION_OR)) {
                        childCriteria = createCriteriaInternal();
                        superCriteria = childCriteria;
                        this.or(childCriteria);
                        genericCriteria(childCriteria, genericQuery);;
                    } else {
                        if(superCriteria == null){
                            superCriteria =criteria;
                        }
                        genericCriteria(superCriteria, genericQuery);;
                    }
                } else {
                    genericCriteria(criteria, genericQuery);
                }*/
            }
            logger.info( "genericSql = "+ genericSql());
        }
    }

    public String genericSql(){
        StringBuilder sqlBuilder = new StringBuilder();
        if (vos != null) {
            int size = vos.size();
            for (int i = 0; i < size; i++) {
                GenericQuery genericQuery = vos.get(i);
                String operation = genericQuery.getOperation();
                /**
                 * 前置括号
                 */
                String  frontBrackets = genericQuery.getFrontBrackets();
                /**
                 * 后置括号
                 */
                String postBrackets = genericQuery.getPostBrackets();
                List<String> values = genericQuery.getValues();
                String startValue = null;
                String endValue = null;
                if(StringUtils.equals(TYPE_NUMBER,genericQuery.getType())){
                    if(values.size() == 2){
                        startValue = values.get(0);
                        endValue = values.get(1);
                    }
                    else {
                        startValue = values.get(0);
                    }
                }
                else if(StringUtils.equals(TYPE_DATE,genericQuery.getType())){
                    if(values.size() == 2){
                        startValue = "'"+values.get(0)+"'";
                        endValue = "'"+values.get(1)+"'";
                    }
                    else {
                        startValue = "'"+values.get(0)+"'";
                    }
                }
                else{
                    if(values.size() == 2){
                        startValue = "'"+org.springframework.util.StringUtils.trimWhitespace(values.get(0))+"'";
                        endValue = "'"+values.get(1)+"'";
                    }
                    else {
                        startValue = "'"+org.springframework.util.StringUtils.trimWhitespace(values.get(0))+"'";
                    }
                }
                // 空值处理
                if(values.size() == 1){
                    String queryValue = values.get(0);
                    if(StringUtils.isNotEmpty(queryValue)) {
                        if(NULL_VALUE.equalsIgnoreCase(queryValue.trim())) {
                            startValue = NULL_VALUE;
                        }
                    }
                }

                StringBuilder valuesBuilder = new StringBuilder();
                if(StringUtils.equals(TYPE_ENUM, genericQuery.getType())){
                    List<String>   genericQueryValues =  genericQuery.getValues();
                    if(!CollectionUtils.isEmpty(genericQueryValues)){
                        valuesBuilder.append(" terms(");
                        for(String values1 : genericQueryValues){
                            valuesBuilder.append("'").append(org.springframework.util.StringUtils.trimWhitespace(values1)).append("'").append(",");
                        }
                        valuesBuilder.delete(valuesBuilder.length()-1,valuesBuilder.length());
                        valuesBuilder.append(")");
                    }
                }
                String conditionOperation =  genericQuery.getConditionOperation();
                if(StringUtils.isNotEmpty(frontBrackets)){
                    sqlBuilder.append(frontBrackets).append("  ");
                }
                String propertyName = genericQuery.getPropertyName();
                Boolean isChild = false;
                String entityAlias = null;
                if (propertyName.indexOf(".") > 0){
                    isChild = true;
                    entityAlias =propertyName.substring(0,propertyName.lastIndexOf("."));
                }
                if(isChild) {
                    sqlBuilder.append(" nested('"+entityAlias+"',");
                }
                if (StringUtils.equals(IS_EQUAL, operation)) {
                    if(StringUtils.equals(TYPE_NUMBER,genericQuery.getType())){
                        startValue = "term('" + startValue + "')";
                    }
                    sqlBuilder.append(propertyName).append(" = ").append(startValue).append(" ");
                } else if (StringUtils.equals(IS_NOT_EQUAL, operation)) {
                    sqlBuilder.append(propertyName).append(" <> ").append(startValue).append(" ");
                } else if (StringUtils.equals(IS_GREATERTHAN, operation)) {
                    sqlBuilder.append(propertyName).append(" > ").append(startValue).append(" ");
                } else if (StringUtils.equals(IS_GREATERTHANOREQUAL, operation)) {
                    sqlBuilder.append(propertyName).append(" >=  ").append(startValue).append(" ");
                } else if (StringUtils.equals(IS_LESSTHAN, operation)) {
                    sqlBuilder.append(propertyName).append(" <  ").append(startValue).append(" ");
                } else if (StringUtils.equals(IS_LESSTHANOREQUAL, operation)) {
                    sqlBuilder.append(propertyName).append(" <=  ").append(startValue).append(" ");
                } else if (StringUtils.equals(IS_CONTAIN, operation)) {
                    if (StringUtils.equals(TYPE_ENUM, genericQuery.getType())) {
                        sqlBuilder.append(propertyName).append(" = ").append(valuesBuilder.toString()).append("  ");
                    } else {
                        startValue = startValue.replaceAll("'","");
                        sqlBuilder.append(propertyName).append(" like  '").append(startValue).append("%' ");
                    }
                } else if (StringUtils.equals(IS_NOTCONTAIN, operation)) {
                    if (StringUtils.equals(TYPE_ENUM, genericQuery.getType())) {
                        sqlBuilder.append(" not ").append(propertyName).append(" = ").append(valuesBuilder.toString()).append(" ");
                    } else {
                        startValue = startValue.replaceAll("'","");
                        sqlBuilder.append(propertyName).append(" not like  '").append(startValue).append("%' ");
                    }
                }else  if(StringUtils.equals(IS_BETWEEN, operation)){
                    sqlBuilder.append(propertyName).append(" between  ").append(startValue).append(" and ").append(endValue).append(" ");
                }else if(StringUtils.equals(IS_NOT_BETWEEN, operation)){
                    sqlBuilder.append(propertyName).append(" not between  ").append(startValue).append(" and ").append(endValue).append(" ");
                }
                if(isChild) {
                    sqlBuilder.append(")");
                }
                if(StringUtils.isNotEmpty(postBrackets)){
                    sqlBuilder.append(postBrackets).append(" ");
                }
                if(StringUtils.isNotEmpty(conditionOperation)){
                    sqlBuilder.append(conditionOperation).append(" ");
                }
            }
        }
        return  sqlBuilder.toString();
    }

    private void genericCriteria(Criteria criteria, GenericQuery genericQuery) {
        String operation = genericQuery.getOperation();
        List<String> values = genericQuery.getValues();
        Boolean bool = false;
        List<String> newValus =null;
        if (!CollectionUtils.isEmpty(values)) {
            newValus = new ArrayList<String>();
            for (String str : values) {
                if (org.springframework.util.StringUtils.isEmpty(str) || NULL_VALUE.equalsIgnoreCase(String.valueOf(str))) {
                    bool = true;
                }else if(str.indexOf("'") > 0) {
                    throw new RuntimeException(message);
                }else if(StringUtils.isNotEmpty(genericQuery.getSercurity())){
                    if("standard".equalsIgnoreCase(genericQuery.getSercurity())){
                        newValus.add(standardEncode(str));
                    }else{
                        newValus.add(byteEncode(str));
                    }
                }
            }
            if(!CollectionUtils.isEmpty(newValus)){
                values.clear();
                values.addAll(newValus);
            }
        }

        Object startValue = null;
        Object endValue = null;
        if(StringUtils.equals(TYPE_DATE,genericQuery.getType())){
            if(values.size() == 2){
                startValue = values.get(0);
                endValue = values.get(1);
            }
            else {
                startValue = values.get(0);
            }
        }else{
            if(values.size() == 2){
                startValue = values.get(0);
                endValue = values.get(1);
            }
            else {
                startValue = values.get(0);
            }
        }
        /**
         * 前置括号
         */
        String  frontBrackets = genericQuery.getFrontBrackets();
        /**
         * 后置括号
         */
        String  postBrackets = genericQuery.getPostBrackets();

        if(StringUtils.isNotEmpty(frontBrackets) && !frontBrackets.matches(frontBracketReg)){
            throw new  RuntimeException(message);
        }

        if(StringUtils.isNotEmpty(postBrackets) && !postBrackets.matches(postBracketsReg)){
            throw new  RuntimeException(message);
        }

        if(!genericQuery.getColumnName().matches(REGEX)){
            throw new  RuntimeException(message);
        }

        String conditionOperation =  genericQuery.getConditionOperation();
        if(StringUtils.isNotEmpty(conditionOperation) && !StringUtils.equalsAnyIgnoreCase(conditionOperation,CONDITIONOPERATION_OR,CONDITIONOPERATION_AND)){
            throw new  RuntimeException(message);
        }
        if(startValue != null){
            startValue =  org.springframework.util.StringUtils.trimWhitespace(String.valueOf(startValue));
        }

        if (StringUtils.equals(IS_EQUAL, operation)) {
            if(NULL_VALUE.equalsIgnoreCase(String.valueOf(startValue))){
                criteria.andIsEmpty(genericQuery.getColumnName(),frontBrackets,postBrackets,conditionOperation);
            }else{
                criteria.andEqualTo(genericQuery.getColumnName(),startValue,frontBrackets,postBrackets,conditionOperation);
            }
        } else if (StringUtils.equals(IS_NOT_EQUAL, operation)) {
            if(NULL_VALUE.equalsIgnoreCase(String.valueOf(startValue))){
                criteria.andNotEmpty(genericQuery.getColumnName(),frontBrackets,postBrackets,conditionOperation);
            }else{
                criteria.andNotEqualTo(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
            }
        } else if (StringUtils.equals(IS_GREATERTHAN, operation)) {
            criteria.andGreaterThan(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
        } else if (StringUtils.equals(IS_GREATERTHANOREQUAL, operation)) {
            criteria.andGreaterThanOrEqualTo(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
        } else if (StringUtils.equals(IS_LESSTHAN, operation)) {
            criteria.andLessThan(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
        } else if (StringUtils.equals(IS_LESSTHANOREQUAL, operation)) {
            criteria.andLessThanOrEqualTo(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
        } else if (StringUtils.equals(IS_CONTAIN, operation)) {
            if (StringUtils.equals(TYPE_ENUM, genericQuery.getType())) {
                if(bool){
                    String nullSql = " OR IFNULL("+genericQuery.getColumnName()+",'') ='' ";
                    if(frontBrackets == null){
                        frontBrackets = "(";
                    }else{
                        frontBrackets = frontBrackets+"(";
                    }

                    if(postBrackets == null){
                        postBrackets = nullSql +  ")";
                    }else{
                        postBrackets = nullSql + postBrackets+")";
                    }

                }
                criteria.andIn(genericQuery.getColumnName(), genericQuery.getValues(),frontBrackets,postBrackets,conditionOperation);
            } else {
                criteria.andLike(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
            }
        } else if (StringUtils.equals(IS_NOTCONTAIN, operation)) {
            if (StringUtils.equals(TYPE_ENUM, genericQuery.getType())) {
                criteria.andNotIn(genericQuery.getColumnName(), genericQuery.getValues(),frontBrackets,postBrackets,conditionOperation);
            }else {
                criteria.andNotLike(genericQuery.getColumnName(), startValue,frontBrackets,postBrackets,conditionOperation);
            }
        }else  if(StringUtils.equals(IS_BETWEEN, operation)){
            criteria.andBetween(genericQuery.getColumnName(), startValue, endValue,frontBrackets,postBrackets,conditionOperation);
        }else if(StringUtils.equals(IS_NOT_BETWEEN, operation)){
            criteria.andNotBetween(genericQuery.getColumnName(), startValue, endValue,frontBrackets,postBrackets,conditionOperation);
        }else{
            logger.error(message +"="+operation);
            throw new RuntimeException(message);
        }
    }

    private static Date toDate(String strDate, String pattern){
        if(StringUtils.isEmpty(pattern)){
            pattern = DEFAULT_PATTERN_TIME;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        }catch (Exception e){
            logger.error(String.format("Could not convert '%s' to Timestamp with pattern '%s'", strDate, pattern), e);
        }
        return date;
    }

    public List<Criteria> getGenericCriteria() {
        initGenericQuery();
        return genericCriteria;
    }

    public void or(Criteria criteria) {
        genericCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        genericCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (genericCriteria.size() == 0) {
            genericCriteria.add(criteria);
        }
        return criteria;
    }

    public String getQueryConditionCode() {
        return queryConditionCode;
    }

    public GenericQueryExample setQueryConditionCode(String queryConditionCode) {
        this.queryConditionCode = queryConditionCode;
        return this;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        genericCriteria.clear();
    }

    public List<GenericQuery> getVos() {
        return vos;
    }

    public void setVos(List<GenericQuery> vos) {
        this.vos = vos;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition,String frontBrackets,String  postBrackets,String conditionOperation) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition,frontBrackets,postBrackets,conditionOperation));
        }

        protected void addCriterion(String condition, Object value, String property,String frontBrackets,String  postBrackets,String conditionOperation) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value,frontBrackets,postBrackets,conditionOperation));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property,String frontBrackets,String  postBrackets,String conditionOperation) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2,frontBrackets,postBrackets,conditionOperation));
        }

        public Criteria andIsNull(String property,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property + " is null ",frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andNotNull(String property,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property + " is not null ",frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andIsEmpty(String property,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion("IFNULL("+property+",'') =''",frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andNotEmpty(String property,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion("IFNULL("+property+",'') <>''",frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }


        public Criteria andEqualTo(String property ,Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" =", value,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andNotEqualTo(String property ,Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" <>", value,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andGreaterThan(String property ,Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" > ", value,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andGreaterThanOrEqualTo(String property ,Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" >= ", value,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andLessThan(String property ,Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" < ", value,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andLessThanOrEqualTo(String property ,Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" <= ", value,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andIn(String property ,List<String> values,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" in ", values,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andNotIn(String property ,List<String> values,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+" not in ", values,property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andBetween(String property ,Object value1, Object value2,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+ " between", value1, value2, property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }

        public Criteria andNotBetween(String property ,Object value1, Object value2,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+ " not between", value1, value2, property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }
        public Criteria andLike(String property, Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+ " like","%"+value+"%", property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }
        public Criteria andNotLike(String property, Object value,String frontBrackets,String  postBrackets,String conditionOperation) {
            addCriterion(property+ " not like","%"+value+"%", property,frontBrackets,postBrackets,conditionOperation);
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {

        /**
         * 前置括号
         */
        private String  frontBrackets;
        /**
         * 后置括号
         */
        private String  postBrackets;

        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        private String conditionOperation;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        public String getFrontBrackets() {
            return frontBrackets;
        }

        public Criterion setFrontBrackets(String frontBrackets) {
            this.frontBrackets = frontBrackets;
            return this;
        }

        public String getPostBrackets() {
            return postBrackets;
        }

        public Criterion setPostBrackets(String postBrackets) {
            this.postBrackets = postBrackets;
            return this;
        }

        public String getConditionOperation() {
            return conditionOperation;
        }

        public Criterion setConditionOperation(String conditionOperation) {
            this.conditionOperation = conditionOperation;
            return this;
        }

        protected Criterion(String condition, String frontBrackets, String  postBrackets, String conditionOperation) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
            this.frontBrackets = frontBrackets;
            this.postBrackets = postBrackets;
            this.conditionOperation = conditionOperation;
        }

        protected Criterion(String condition, Object value, String typeHandler,String frontBrackets,String  postBrackets, String conditionOperation) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            this.frontBrackets = frontBrackets;
            this.postBrackets = postBrackets;
            this.conditionOperation = conditionOperation;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value,String frontBrackets,String  postBrackets, String conditionOperation) {
            this(condition, value, null,frontBrackets,postBrackets,conditionOperation);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler,String frontBrackets,String  postBrackets, String conditionOperation) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
            this.frontBrackets = frontBrackets;
            this.postBrackets = postBrackets;
            this.conditionOperation = conditionOperation;
        }

        protected Criterion(String condition, Object value, Object secondValue,String frontBrackets,String  postBrackets, String conditionOperation) {
            this(condition, value, secondValue,null,frontBrackets,postBrackets,conditionOperation);
        }
    }

    /**
     * 标准加密
     * @param obj
     * @return
     */
    public  String standardEncode(Object obj) {
        String result = null;
        if(obj == null || StringUtils.isEmpty(obj.toString())){
            return null;
        }
        Response<ServiceP002.ResponseMsg> response = ServiceP002.request().encrypt_strategy_id("00")
                .data(String.valueOf(obj)).execute();
        ServiceP002.ResponseMsg responseMsg = response.getContent();
        if(responseMsg != null){
            try {
                result = responseMsg.data();
            }catch (Exception e){
                logger.error("standardEncode",e);
            }
        }
        return result;
    }

    /**
     * 字节加密
     * @param obj
     * @return
     */
    public  String byteEncode(Object obj) {
        String str;
        if(obj == null || StringUtils.isEmpty(obj.toString())){
            return null;
        }else {
            try {
                final Response<ServiceP002.ResponseMsg> response = ServiceP002.request()
                        .encrypt_strategy_id("02")
                        .data(String.valueOf(obj))
                        .execute();
                str =  response.getContent().data();
            } catch (Exception e) {
                str = obj.toString();
            }
        }
        return str;
    }
}

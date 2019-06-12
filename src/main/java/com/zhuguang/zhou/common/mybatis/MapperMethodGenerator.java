package com.zhuguang.zhou.common.mybatis;

import com.zhuguang.zhou.common.mapper.AbstractTreeMapper;
import com.zhuguang.zhou.common.rule.RuleMapperMethodGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;

public class MapperMethodGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MapperMethodGenerator.class);

    private static String  TRACE_COLUMN = "trace_id";
    public static void process(Writer writer, CommonTable table, String idProertyClassType, HashSet<String> existMethodSet, KeyGenMode keyGenMode) throws IOException {

        if (table.getBaseColumns() == null) {
            writer.write(addBaseColumnsSQL(table));
        }

        if (!existMethodSet.contains("insert")) {
            writer.write(addInsertAndReturnId(table, idProertyClassType, keyGenMode));
        }

        if (!existMethodSet.contains("insertSelective")) {
            writer.write(addInsertSelectiveAndReturnId(table, idProertyClassType, keyGenMode));
        }

        if (!existMethodSet.contains("insertBatch")) {
            writer.write(addInsertBatch(table, idProertyClassType, keyGenMode));
        }

        if (!existMethodSet.contains("update")) {
            writer.write(addUpdateByPrimaryKey(table, idProertyClassType));
        }

        if (!existMethodSet.contains("updateSelective")) {
            writer.write(addUpdateByPrimaryKeySelective(table, idProertyClassType));
        }

        if (!existMethodSet.contains("delete")) {
            writer.write(addDeleteByPrimaryKey(table, idProertyClassType));
        }

        if (!existMethodSet.contains("disable")) {
            writer.write(addDisableByPrimaryKey(table, idProertyClassType));
        }

        if (!existMethodSet.contains("enable")) {
            writer.write(addEnableByPrimaryKey(table, idProertyClassType));
        }

        if (!existMethodSet.contains("get")) {
            writer.write(addSelectByPrimaryKey(table, idProertyClassType));
        }

        if (!existMethodSet.contains("getByIds")) {
            writer.write(addSelectByPrimaryKeys(table, idProertyClassType));
        }

        if (!existMethodSet.contains("selectAll")) {
            writer.write(addSelectByModel(table, idProertyClassType));
        }

        if (!existMethodSet.contains("search")) {
            writer.write(addSearch(table, idProertyClassType));
        }

        if(!existMethodSet.contains("orderByClause")){
            writer.write(addOrdderByClauseSQL());
        }

        if (isTreeMapperClass(table.getNamespace())) {
            writer.write(addTree(existMethodSet));
        }
    }


    private static String addBaseColumnsSQL(CommonTable table) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<sql id=\"" + table.getBaseColumnsId() + "\" >\n");
        sb.append(getColumns(table));
        sb.append("\n</sql>\n");

        return sb.toString();
    }
    private static String addOrdderByClauseSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("<sql id=\"orderByClause\">                                                     \n");
        sb.append("    <if test=\"criteria.orderByClauses != null\" >                             \n");
        sb.append("        <trim prefix=\" ORDER  BY \" suffix=\"  \" suffixOverrides=\",\">               \n");
        sb.append("          <foreach collection=\"criteria.orderByClauses\" item=\"orderByObj\"> \n");
        sb.append("              <if test=\"orderByObj.field != null and orderByObj.field != ''\">\n");
        sb.append("                  <if test=\"orderByObj.orderByMode == 0\">                    \n");
        sb.append("                      ${orderByObj.field} ASC ,                                \n");
        sb.append("                  </if>                                                        \n");
        sb.append("                  <if test=\"orderByObj.orderByMode != 0\">                    \n");
        sb.append("                      ${orderByObj.field} DESC ,                               \n");
        sb.append("                  </if>                                                        \n");
        sb.append("              </if>                                                            \n");
        sb.append("          </foreach>                                                           \n");
        sb.append("        </trim>                                                                \n");
        sb.append("    </if>                                                                      \n");
        sb.append("</sql>                                                                         \n");
        return sb.toString();
    }

    private static String addInsert(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<insert id=\"insert\"  parameterType=\"" + table.getClassType() + "\" >");
        sb.append("\n insert into " + table.getTable());
        sb.append("(" + getColumns(table) + ")");
        sb.append("\n    values " + getValues(table.getAllColumnList()));
        sb.append("\n</insert>\n");

        return sb.toString();
    }

    private static String addInsertSelective(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n <insert id=\"insertSelective\" parameterType=\"" + table.getClassType() + "\"  >");
        sb.append("\n insert into " + table.getTable());
        sb.append(getIfColumns(table));
        sb.append("\n    values " + getIfValues(table.getAllColumnList()));
        sb.append("\n</insert>\n");
        return sb.toString();
    }

    private static String addInsertAndReturnId(CommonTable table, String idProertyClassType, KeyGenMode keyGenMode) {
        StringBuilder sb = new StringBuilder();
        sb.append(getInsertHeader(table, idProertyClassType, "insert", table.getClassType(), keyGenMode));
        sb.append("\n insert into " + table.getTable());
        sb.append("(" + getColumns(table) + ")");
        sb.append("\n    values " + getValues(table.getAllColumnList()));
        sb.append("\n</insert>\n");
        return sb.toString();
    }

    private static String addInsertSelectiveAndReturnId(CommonTable table, String idProertyClassType, KeyGenMode keyGenMode) {
        StringBuilder sb = new StringBuilder();
        sb.append(getInsertHeader(table, idProertyClassType, "insertSelective", table.getClassType(), keyGenMode));
        sb.append("\n insert into " + table.getTable());
        sb.append(getIfColumns(table));
        sb.append("\n    values " + getIfValues(table.getAllColumnList()));
        sb.append("\n</insert>\n");
        return sb.toString();
    }

    private static String addInsertBatch(CommonTable table, String idProertyClassType, KeyGenMode keyGenMode) {
        StringBuilder sb = new StringBuilder();
        sb.append(getInsertHeader(table, idProertyClassType, "insertBatch", "java.util.List", keyGenMode));
        sb.append("\n insert into " + table.getTable());
        sb.append("(" + getColumns(table) + ")");
        sb.append("\n    values " + getBatchValues(table.getAllColumnList()));
        sb.append("\n</insert>\n");
        return sb.toString();
    }

    private static String addUpdateByPrimaryKey(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n <update id=\"update\" parameterType=\"" + table.getClassType() + "\" >");
        sb.append("\n update " + table.getTable());
        sb.append(getConditions4Update(table.getTable(),table.getColumnList(), false));
        sb.append("\n where " + getKeyCondition(table.getIdColumn()));
        sb.append("\n</update>\n");
        return sb.toString();
    }

    private static String addUpdateByPrimaryKeySelective(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n <update id=\"updateSelective\" parameterType=\"" + table.getClassType() + "\" >");
        sb.append("\n update " + table.getTable());
        sb.append(getConditions4Update(table.getTable(),table.getColumnList(), true));
        sb.append("\n where " + getKeyCondition(table.getIdColumn()));
        sb.append("\n</update>\n");
        return sb.toString();
    }

    private static String addDeleteByPrimaryKey(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<delete id=\"delete\"  parameterType=\"" + idProertyClassType + "\" >");
        sb.append("\n delete  from " + table.getTable());
        sb.append("\n    where " + table.getIdColumn().getColumn() + " in ");
        sb.append("\n    <foreach collection=\"array\" item=\"id\" open=\"(\" separator=\",\" close=\")\">");
        sb.append("\n        #{id}");
        sb.append("\n    </foreach>");
        sb.append("\n</delete>\n");
        return sb.toString();
    }

    private static String addDisableByPrimaryKey(CommonTable table, String idProertyClassType) {
        Boolean bool = containTraceId(table);
        StringBuilder sb = new StringBuilder();
        sb.append("\n<update id=\"disable\"  parameterType=\"" + idProertyClassType + "\" >");
        sb.append("\n update " + table.getTable() + " set enabled_flag = 0");
        // 是否包含traceId
        if(bool){
            sb.append(", ").append(TRACE_COLUMN).append(" = ").append("'${@com.kyexpress.framework.utils.ContextUtils@get().getTraceId()}'");
        }
        sb.append("\n    where " + table.getIdColumn().getColumn() + " in ");
        sb.append("\n    <foreach collection=\"array\" item=\"id\" open=\"(\" separator=\",\" close=\")\">");
        sb.append("\n        #{id}");
        sb.append("\n    </foreach>");
        sb.append("\n</update>\n");
        return sb.toString();
    }

    private static String addEnableByPrimaryKey(CommonTable table, String idProertyClassType) {
        Boolean bool = containTraceId(table);
        StringBuilder sb = new StringBuilder();
        sb.append("\n<update id=\"enable\"  parameterType=\"" + idProertyClassType + "\" >");
        sb.append("\n update " + table.getTable() + " set enabled_flag = 1");
        // 是否包含traceId
        if(bool){
            sb.append(", ").append(TRACE_COLUMN).append(" = ").append("'${@com.kyexpress.framework.utils.ContextUtils@get().getTraceId()}'");
        }
        sb.append("\n    where " + table.getIdColumn().getColumn() + " in ");
        sb.append("\n    <foreach collection=\"array\" item=\"id\" open=\"(\" separator=\",\" close=\")\">");
        sb.append("\n        #{id}");
        sb.append("\n    </foreach>");
        sb.append("\n</update>\n");
        return sb.toString();
    }

    private static String addSelectByPrimaryKey(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<select id=\"get\" resultMap=\"" + table.getBaseResultMap() + "\" parameterType=\"" + idProertyClassType + "\" >");
        sb.append("\n select ");
        sb.append(getColumns(table));
        sb.append("\n    from " + table.getTable());
        sb.append("\n    where " + getKeyCondition(table.getIdColumn()));
        sb.append("\n</select>\n");
        return sb.toString();
    }

    private static String addSelectByPrimaryKeys(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<select id=\"getByIds\" resultMap=\"" + table.getBaseResultMap() + "\" parameterType=\"" + idProertyClassType + "\" >");
        sb.append("\n select ");
        sb.append(getColumns(table));
        sb.append("\n    from " + table.getTable());
        sb.append("\n    where " + table.getIdColumn().getColumn() + " in ");
        sb.append("\n    <foreach collection=\"array\" item=\"id\" open=\"(\" separator=\",\" close=\")\">");
        sb.append("\n        #{id}");
        sb.append("\n    </foreach>");
        sb.append("\n</select>\n");
        return sb.toString();
    }

    private static String addSearch(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<select id=\"search\" resultMap=\"" + table.getBaseResultMap() + "\" >");
        sb.append("\n select ");
        sb.append(getColumns(table));
        sb.append("\n    from " + table.getTable());

        sb.append("\n    <if test=\"criteria != null and criteria.vo != null\" >");
        sb.append(getConditions4Query(table.getAllColumnList(), null, "criteria.vo."));
        sb.append("\n    </if>");
        sb.append("\n    <if test=\"criteria != null and criteria.orderByClauses != null \" >");
        sb.append("\n       <include refid=\"orderByClause\" />");
        sb.append("\n    </if>");
        sb.append("\n</select>\n");

        return sb.toString();
    }

    private static String addSelectByModel(CommonTable table, String idProertyClassType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<select id=\"selectAll\" resultMap=\"" + table.getBaseResultMap() + "\" parameterType=\"" + table.getClassType() + "\" >");
        sb.append("\n select ");
        sb.append(getColumns(table));
        sb.append("\n    from " + table.getTable());
        sb.append(getConditions4Query(table.getAllColumnList(), null, null));
        sb.append("\n</select>\n");
        return sb.toString();
    }


    private static String getInsertHeader(CommonTable table, String idProertyClassType, String method, String parameterType, KeyGenMode globalKeyGenMode) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<insert id=\"" + method + "\" parameterType=\"" + parameterType + "\" ");

        KeyGenMode keyGenMode = table.getKeyGenMode();
        if (keyGenMode == null) {
            keyGenMode = globalKeyGenMode;
        }

        if (keyGenMode != null) {
            if (KeyGenMode.CUSTOM == keyGenMode) {
                sb.append(">");
            } else {
                sb.append("useGeneratedKeys=\"true\" keyProperty=\"" + table.getIdColumn().getProperty() + "\">");
                if (KeyGenMode.DB_UUID == keyGenMode) {
                    sb.append("<selectKey keyProperty=\"" + table.getIdColumn().getProperty() + "\" resultType=\"" + idProertyClassType + "\" order=\"BEFORE\">");
                    sb.append(StringUtils.defaultString(keyGenMode.getValue(), "select replace(uuid(),'-','') from dual"));
                    sb.append("</selectKey>");
                } else if (KeyGenMode.MYCAT == keyGenMode) {
                    sb.append("<selectKey keyProperty=\"" + table.getIdColumn().getProperty() + "\" resultType=\"" + idProertyClassType + "\" order=\"BEFORE\">");
                    sb.append(StringUtils.defaultString(keyGenMode.getValue(), "select next value for MYCATSEQ_" + table.getTable().toUpperCase()));
                    sb.append("</selectKey>");
                } else if (KeyGenMode.IDENTITY == keyGenMode) {
                } else if (KeyGenMode.UUID == keyGenMode) {
                    sb.append("<bind name=\"" + table.getIdColumn().getProperty() + "\" value='" + StringUtils.defaultString(keyGenMode.getValue(), "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")") + "' />");
                } else if (KeyGenMode.CUSTOM == keyGenMode) {
                }
            }
        }

        return sb.toString();
    }

    private static String getColumns(CommonTable table) {
        StringBuilder sb = new StringBuilder();
        for (CommonTableColumn tableColumn : table.getAllColumnList()) {
            sb.append(tableColumn.getColumn());
            sb.append(",");
        }
        if (sb.indexOf(",") != -1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String getIfColumns(CommonTable table) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n   <trim prefix=\"(\" suffix=\")\"   suffixOverrides=\",\" >");

        for (CommonTableColumn tableColumn : table.getAllColumnList()) {
            sb.append("\n    <if test=\"" + tableColumn.getProperty() + " != null\" >");
            sb.append(tableColumn.getColumn());
            sb.append(",");
            sb.append("\n    </if>");
        }
        sb.append("\n   </trim>");
        return sb.toString();
    }

    private static String getKeyCondition(CommonTableColumn idColumn) {
        return idColumn.getColumn() + " = #{" + idColumn.getProperty() + ",jdbcType=" + idColumn.getJdbcType() + "}";
    }

    private static String getConditions4Update(String table,List<CommonTableColumn> columnList, boolean needif) {
        String prefix = "SET";
        String stuffix = ",";

        StringBuilder sb = new StringBuilder();
        sb.append("\n   <trim prefix=\"" + prefix + "\"  suffixOverrides=\"" + stuffix + "\" >");
        for (CommonTableColumn column : columnList) {
            if(isNoUpdateColumn(table,column)){
                continue;
            }
            if (isDefault(column)) {
                sb.append("\n    <if test=\"" + column.getProperty() + " != null\" >");
                sb.append("\n   	  " + column.getColumn() + " = #{" + column.getProperty() + ",jdbcType=" + column.getJdbcType() + "}" + stuffix);
                sb.append("\n    </if>");
            } else {
                if (needif) {
                    sb.append("\n    <if test=\"" + column.getProperty() + " != null\" >");
                }
                sb.append("\n   	  " + column.getColumn() + " = #{" + column.getProperty() + ",jdbcType=" + column.getJdbcType() + "}" + stuffix);
                if (needif) {
                    sb.append("\n    </if>");
                }
            }
        }
        sb.append("\n   </trim>");
        return sb.toString();
    }

    /**
     * 是否允许更新字段
     * @param tableName
     * @param column
     * @return
     */
    private static boolean isNoUpdateColumn(String tableName,CommonTableColumn column){
        if ("creation_date".equals(column.getColumn())) {
            return true;
        }

        if ("created_by".equals(column.getColumn())) {
            return true;
        }
        if(column.getColumn().equalsIgnoreCase(RuleMapperMethodGenerator.getRuleByTable(tableName))){
            return true;
        }
        return false;
    }

    private static boolean isDefault(CommonTableColumn column) {
        if ("creation_date".equals(column.getColumn())) {
            return true;
        }

        if ("created_by".equals(column.getColumn())) {
            return true;
        }

        if ("updation_date".equals(column.getColumn())) {
            return true;
        }

        if ("updated_by".equals(column.getColumn())) {
            return true;
        }

        if ("enabled_flag".equals(column.getColumn())) {
            return true;
        }

        if ("id".equals(column.getColumn())) {
            return true;
        }
        return false;

    }

    private static String getConditions4Query(List<CommonTableColumn> columnList, String prefixSQL, String var) {
        String prefix = "WHERE";
        String prefixOverrides = "AND | OR";

        if (var == null) {
            var = "";
        }
        var = var.trim();

        StringBuilder sb = new StringBuilder();
        sb.append("\n   <trim prefix=\"" + prefix + "\"  prefixOverrides=\"" + prefixOverrides + "\" >");
        if (prefixSQL != null) {
            sb.append("\n   	  " + prefixSQL);
        }
        for (CommonTableColumn column : columnList) {
            if("VARCHAR".equals(column.getJdbcType()) ||  "CHAR".equals(column.getJdbcType())  ||"LONGVARCHAR".equals(column.getJdbcType()) )
            {
                sb.append("\n    <if test=\"" + var + column.getProperty() + " != null and "+ var + column.getProperty() +"!=''\" >");
            }else {
                sb.append("\n    <if test=\"" + var + column.getProperty() + " != null\" >");
            }
            sb.append("\n   	  AND " + column.getColumn() + " = #{" + var + column.getProperty() + ", jdbcType=" + column.getJdbcType() + "}");
            sb.append("\n    </if>");
        }
        if (StringUtils.isNotEmpty(var)) {
            String str = "criteria.generic";
            sb.append("\n    <if test=\"" + str + " != null\">");
            sb.append("\n      <trim prefix=\" and (\" prefixOverrides=\"\" suffix=\")\">");
            sb.append("\n       <foreach collection=\"criteria.generic.genericCriteria\" item=\"criteriachild\" separator=\"or\">\n" +
                    "           <if test=\"criteriachild.valid\">\n" +
                    "          <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
                    "            <foreach collection=\"criteriachild.criteria\" item=\"criterion\">\n" +
                    "               <if test=\"criterion.frontBrackets != null and criterion.frontBrackets != ''\" >\n"+
                    "                    ${criterion.frontBrackets}\n"+
                    "               </if>\n"+
                    "              <choose>\n" +
                    "                <when test=\"criterion.noValue\">\n" +
                    "                   ${criterion.condition}\n" +
                    "                </when>\n" +
                    "                <when test=\"criterion.singleValue\">\n" +
                    "                   ${criterion.condition} #{criterion.value}\n" +
                    "                </when>\n" +
                    "                <when test=\"criterion.betweenValue\">\n" +
                    "                   ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                    "                </when>\n" +
                    "                <when test=\"criterion.listValue\">\n" +
                    "                   ${criterion.condition}\n" +
                    "                  <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                    "                    #{listItem}\n" +
                    "                  </foreach>\n" +
                    "                </when>\n" +
                    "              </choose>\n" +
                    "               <if test=\"criterion.postBrackets != null and criterion.postBrackets != ''\" >\n"+
                    "                    ${criterion.postBrackets}\n"+
                    "               </if>\n"+
                    "               <if test=\"criterion.conditionOperation != null and criterion.conditionOperation != ''\" >\n"+
                    "                    ${criterion.conditionOperation}\n"+
                    "               </if>\n"+
                    "            </foreach>\n" +
                    "          </trim>\n" +
                    "        </if>\n" +
                    "      </foreach>");
            sb.append("\n   </trim>");
            sb.append("\n    </if>");
        }
        sb.append("\n   </trim>");
        return sb.toString();
    }

    private static String getValues(List<CommonTableColumn> columnList) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (CommonTableColumn column : columnList) {
            sb.append("#{" + column.getProperty() + ", jdbcType=" + column.getJdbcType() + "},");
        }
        if (sb.indexOf(",") != -1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    private static String getIfValues(List<CommonTableColumn> columnList) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n   <trim prefix=\"(\" suffix=\")\"  suffixOverrides=\",\" >");
        for (CommonTableColumn column : columnList) {
            sb.append("\n    <if test=\"" + column.getProperty() + " != null\" >");
            sb.append("#{" + column.getProperty() + ", jdbcType=" + column.getJdbcType() + "},");
            sb.append("\n    </if>");
        }
        sb.append("\n   </trim>");
        return sb.toString();
    }


    private static String getBatchValues(List<CommonTableColumn> columnList) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n   <trim prefix=\"\"  suffixOverrides=\",\" >");
        sb.append("\n <foreach item=\"model\" index=\"index\" collection=\"list\"> ");
        sb.append("\n (");
        for (CommonTableColumn column : columnList) {
            sb.append("#{model." + column.getProperty() + ",jdbcType=" + column.getJdbcType() + "},");
        }
        if (sb.indexOf(",") != -1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(" ),");
        sb.append("\n </foreach>");
        sb.append("\n   </trim>");
        return sb.toString();
    }

    private static String addTree(HashSet<String> existMethodSet) {
        StringBuilder sb = new StringBuilder();

        if (!existMethodSet.contains("disableNode")) {
            sb.append("    <update id=\"disableNode\">\n");
            sb.append("        UPDATE <include refid=\"BaseTable\" />\n");
            sb.append("        SET enabled_flag=0\n");
            sb.append("        WHERE code = #{code} AND left_value &gt;= #{left} AND right_value &lt;= #{right}\n");
            sb.append("    </update>\n");
        }
        if (!existMethodSet.contains("deleteNode")) {
            sb.append("    <delete id=\"deleteNode\">\n");
            sb.append("        DELETE FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE code = #{code} AND left_value &gt;= #{left} AND right_value &lt;= #{right}\n");
            sb.append("    </delete>\n");
        }
        if (!existMethodSet.contains("updateDisplayIndex")) {
            sb.append("    <update id=\"updateDisplayIndex\">\n");
            sb.append("        UPDATE <include refid=\"BaseTable\" /> SET\n");
            sb.append("        display_index = #{displayIndex}\n");
            sb.append("        WHERE id = #{id}\n");
            sb.append("    </update>\n");
        }
        if (!existMethodSet.contains("updateRight")) {
            sb.append("    <update id=\"updateRight\">\n");
            sb.append("        UPDATE <include refid=\"BaseTable\" /> SET\n");
            sb.append("        <if test=\"!deleteFlag\">\n");
            sb.append("            right_value = right_value + #{count}\n");
            sb.append("        </if>\n");
            sb.append("        <if test=\"deleteFlag\">\n");
            sb.append("            right_value = right_value - #{count}\n");
            sb.append("        </if>\n");
            sb.append("        WHERE right_value &gt; #{right} AND code = #{code}\n");
            sb.append("    </update>\n");
        }
        if (!existMethodSet.contains("updateLeft")) {
            sb.append("    <update id=\"updateLeft\">\n");
            sb.append("        UPDATE <include refid=\"BaseTable\" /> SET\n");
            sb.append("        <if test=\"!deleteFlag\">\n");
            sb.append("            left_value = left_value + #{count}\n");
            sb.append("        </if>\n");
            sb.append("        <if test=\"deleteFlag\">\n");
            sb.append("            left_value = left_value - #{count}\n");
            sb.append("        </if>\n");
            sb.append("        WHERE left_value &gt; #{right} AND code = #{code}\n");
            sb.append("    </update>\n");
        }
        if (!existMethodSet.contains("getRootByCode")) {
            sb.append("    <select id=\"getRootByCode\" parameterType=\"String\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND parent_id = '0' AND code = #{code} limit 1\n");
            sb.append("    </select>\n");
        }
        if (!existMethodSet.contains("getNodeByCode")) {
            sb.append("    <select id=\"getNodeByCode\" parameterType=\"String\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND parent_id = '0' AND code = #{code} limit 1\n");
            sb.append("    </select>\n");
        }
        if (!existMethodSet.contains("getRoots")) {
            sb.append("    <select id=\"getRoots\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND parent_id = '0'\n");
            sb.append("        ORDER BY display_Index ASC\n");
            sb.append("    </select>\n");
        }
        if (!existMethodSet.contains("getChild")) {
            sb.append("    <select id=\"getChild\" parameterType=\"Long\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND parent_id = #{parentId}\n");
            sb.append("        ORDER BY display_index ASC\n");
            sb.append("    </select>\n");
        }
        if (!existMethodSet.contains("getChildren")) {
            sb.append("    <select id=\"getChildren\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND code = #{code} AND left_value BETWEEN #{left} AND #{right}\n");
            sb.append("        ORDER BY left_value ASC\n");
            sb.append("    </select>\n");
        }
        if (!existMethodSet.contains("getNearestNode")) {
            sb.append("    <select id=\"getNearestNode\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND (parent_id = #{parentId} AND display_index &lt;= #{displayIndex}) OR id = #{parentId}\n");
            sb.append("        ORDER BY left_value DESC LIMIT 0, 1\n");
            sb.append("    </select>\n");
        }
        if (!existMethodSet.contains("getPath")) {
            sb.append("    <select id=\"getPath\" resultMap=\"BaseResultMap\">\n");
            sb.append("        SELECT <include refid=\"BaseColumns\" />\n");
            sb.append("        FROM <include refid=\"BaseTable\" />\n");
            sb.append("        WHERE enabled_flag = 1 AND code = #{code} AND left_value &lt; #{left} AND right_value &gt; #{right}\n");
            sb.append("        ORDER BY left_value ASC\n");
            sb.append("    </select>\n");
        }

        return sb.toString();
    }

    /**
     * 是否包含traceId
     * @param table
     * @return
     */
    private static Boolean containTraceId(CommonTable table) {
        List<CommonTableColumn> commonTableColumns = table.getColumnList();
        if (!CollectionUtils.isEmpty(commonTableColumns)) {
            for (CommonTableColumn commonTableColumn : commonTableColumns) {
                if (commonTableColumn != null && TRACE_COLUMN.equalsIgnoreCase(commonTableColumn.getColumn())) {
                    return true;
                }
            }
        }
        return false;
    }


    private static boolean isTreeMapperClass(String classType) {
        boolean result = false;
        try {
            Class<?> treeMapperClass = Class.forName(classType);
            result = AbstractTreeMapper.class.isAssignableFrom(treeMapperClass);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return result;
    }
}

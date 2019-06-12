package com.zhuguang.zhou.common.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author liheng
 * @since 1.0
 */
public class CommonXMLMapperBuilder {
	private static final Logger logger = LoggerFactory.getLogger(CommonXMLMapperBuilder.class);
	private Resource[] mapperLocations;
	private String configPath;
	private String javaPackage;
	private HashMap<String, String> typeAliasesHm = new HashMap<String, String>();
	private String baseResultMap;
	private String baseTableName;
	private String baseColumns;
	private String generationType;
	/** 数据库Key生成方式 */
	private KeyGenMode keyGenMode = KeyGenMode.IDENTITY;
	
	public CommonXMLMapperBuilder() throws Exception {
	}

	public Resource[] builderCommonMapper(Resource[] mapperLocations) throws Exception {
		paseConfig();
		Resource[] commonMapperResources = new Resource[mapperLocations.length];
		
		for (int i=0; i<mapperLocations.length; i++) {
			commonMapperResources[i] = parse(mapperLocations[i]);
			
		}
		
		return commonMapperResources;
	}
	
	/**
	 * 解决Mybatis配置文件
	 * @throws Exception
	 */
	private void paseConfig() throws Exception {
		if (StringUtils.isEmpty(configPath)) {
			return;
		}
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node rootNode = (Node) xpath.evaluate("/configuration", new InputSource(configPath), XPathConstants.NODE);
		Node packageNode = (Node) xpath.evaluate("package", rootNode, XPathConstants.NODE);
		if (packageNode != null) {
			javaPackage = getAttrbute(packageNode, "name", true);
		}
		
		NodeList typeAliaseNodeList = (NodeList) xpath.evaluate("typeAliases", rootNode, XPathConstants.NODESET) ;
		for (int i = 0; i < typeAliaseNodeList.getLength(); i++) {
			Node typeAliaseNode = typeAliaseNodeList.item(i);			
			typeAliasesHm.put(getAttrbute(typeAliaseNode, "alias", false), getAttrbute(typeAliaseNode, "type", false));
		}
	}

	public static void main(String[] args) throws Exception {
		FileSystemResource resource = new FileSystemResource("/henry/projects/ccpg/framework/framework-web/src/main/resources/com/kyexpress/framework/demo/UserInfoMapper.xml");
		CommonXMLMapperBuilder builder = new CommonXMLMapperBuilder();
		builder.setBaseResultMap("BaseResultMap");
		builder.setBaseTableName("BaseTable");
		builder.paseConfig();
		Resource rewiteResource = builder.parse(resource);
		if (rewiteResource != null) {
			
		}
		
	}
	
	
	private Resource parse(Resource resource) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		builder.setEntityResolver(new XMLMapperEntityResolver());
		Document document = builder.parse(resource.getInputStream());
		Node rootNode = (Node) xpath.evaluate("/mapper", document, XPathConstants.NODE) ;
		if (rootNode == null) {
			logger.info("Invalid XML Map file, ignore xml file {}", resource.getFile().getAbsolutePath());
			return null;			
		}
		
		//查找表名Sql块
		Node tableNameNode = (Node) xpath.evaluate("sql[@id='" + baseTableName + "'][1]", rootNode, XPathConstants.NODE) ;

		if (tableNameNode == null) {
			logger.info("Can't found {} sql segment, ignore map {}", baseTableName, resource.getFile().getAbsolutePath());
			return null;
		}

		String classType = getFullClassPath(tableNameNode.getTextContent().trim());
		CommonTable table = new CommonTable();		
		table.setTable(classType);
        table.setNamespace(getAttrbute(rootNode, "namespace", true));


		//主键生成类型Sql块
		Node generationTypeNode = (Node) xpath.evaluate("sql[@id='" + generationType + "'][1]", rootNode, XPathConstants.NODE) ;
        if (generationTypeNode != null) {
            String generationTypeValue = generationTypeNode.getTextContent().trim();
            if (generationTypeValue.length() > 0) {
                int pos = generationTypeValue.indexOf("(");

                String code = generationTypeValue.substring(0, pos).trim();
                KeyGenMode privateKeyGenMode = KeyGenMode.parse(code);
                if (privateKeyGenMode != null) {
                    String value = generationTypeValue.substring(pos + 1, generationTypeValue.length() -1).trim();
                    privateKeyGenMode.setValue(value);

                    table.setKeyGenMode(privateKeyGenMode);
                }
            }
        }

		//查询字段列表Sql块
        table.setBaseColumnsId(baseColumns);
		Node baseColumnsNode = (Node) xpath.evaluate("sql[@id='" + baseColumns + "'][1]", rootNode, XPathConstants.NODE) ;
        if (baseColumnsNode != null) {
            table.setBaseColumns(baseColumnsNode.getTextContent().trim());
        }

        //查找表字段块
		Node resultMapNode = (Node) xpath.evaluate("resultMap[@id='" + baseResultMap + "'][1]", rootNode, XPathConstants.NODE) ;
		
		if (resultMapNode == null) {
			logger.info("Can't found {} resultMap, ignore map {}", baseTableName, resource.getFile().getAbsolutePath());
			return null;
		}
		//组装表的映射类
		table.setClassType(getAttrbute(resultMapNode, "type", true));
		
		//组装主键
		Node idNode = (Node) xpath.evaluate("id[1]",  resultMapNode, XPathConstants.NODE) ;
		table.setIdColumn(createTableColumn(idNode));
		table.setBaseResultMap(baseResultMap);
		
		//组装非主键字段
		ArrayList<CommonTableColumn> columnList = new ArrayList<CommonTableColumn>();
		NodeList resultNodeList = (NodeList) xpath.evaluate("result", resultMapNode, XPathConstants.NODESET) ;
		for (int i = 0; i < resultNodeList.getLength(); i++) {
			Node resultNode = resultNodeList.item(i);
			columnList.add(createTableColumn(resultNode));
		}
		table.setColumnList(columnList);
		//获取Mapp里所有的select/insert/update/delete方式名
		HashSet<String> existMethodSet = getExistMethod(xpath, rootNode);

		return rewriteXmlMapper(resource, table, existMethodSet);
	}






	/**
	 * 获取Mapp里所有的select/insert/update/delete方式名
	 * 
	 * @param xpath
	 * @param rootNode
	 * @return
	 * @throws XPathExpressionException
	 */
	private HashSet<String> getExistMethod(XPath xpath, Node rootNode) throws XPathExpressionException {
		HashSet<String> existMethodSet = new HashSet<String>();
		NodeList sqlNodeList = (NodeList) xpath.evaluate("select|insert|update|delete", rootNode, XPathConstants.NODESET) ;
		for (int i = 0; i < sqlNodeList.getLength(); i++) {
			Node sqlNode = sqlNodeList.item(i);
			existMethodSet.add(getAttrbute(sqlNode, "id", false));
		}
		return existMethodSet;
	}

	/**
	 * @param resource   原XML Mapper
	 * @param table      Xml Mapper分析出来的表元数据
	 * @param existMethodSet XML Mapper 已经存在的SQL 操作方法 
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private Resource rewriteXmlMapper(Resource resource, CommonTable table, HashSet<String> existMethodSet)
			throws IOException, UnsupportedEncodingException {
		String idProertyClassType = getIdPropertyType(table.getClassType(), table.getIdColumn().getProperty());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(bos);
//		template.process(templateArgs, writer);
		MapperMethodGenerator.process(writer, table, idProertyClassType, existMethodSet, keyGenMode);
		writer.flush();
		String xmlMapperStr = "";
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
		while((line = br.readLine()) != null){
			xmlMapperStr = xmlMapperStr + line + "\n";
		}
		
//		String sqlsegment = new String(bos.toByteArray(), "utf-8");
//		String xmlMapperStr = new String(Files.readAllBytes(resource.getFile().toPath()), "utf-8");
		int index = xmlMapperStr.lastIndexOf("</mapper>");
		
		String xmlContext = xmlMapperStr.substring(0, index) + new String(bos.toByteArray(), "utf-8") + xmlMapperStr.substring(index);
		
		logger.debug("rewite xml mapper content {}", xmlContext);

		return new ByteArrayResource(xmlContext.getBytes("utf-8"), resource.getFilename());
	}
	
	/**
	 * 获取主键的Class类型
	 * @param classType  表的类的类型
	 * @param id         表主键属性
	 * @return
	 */
	private String getIdPropertyType(String classType, String id) {
		try {
			Class<?> idClass = Class.forName(classType);

            Field idField = null;
            for(;idClass != Object.class; idClass= idClass.getSuperclass()){
                try {
                    idField = idClass.getDeclaredField(id);
                    break;
                } catch (Exception e) {}
            }
			return idField.getType().getName();
		} catch (Exception e) {
			throw new InvalidXmlMapperException(String.format("Can't parse id property %s.%s in result map, exception:%s", classType, id, e.getMessage()));
		}
	}
	
	/**
	 * 获取Class全路径
	 * @param name 类名
	 * @return
	 */
	private String getFullClassPath(String name) {
		String type = typeAliasesHm.get(name);
		
		if (StringUtils.isEmpty(type) && !StringUtils.isEmpty(javaPackage)) {
			return javaPackage + "." + name;
		}
		
		return name;
	}

	/**
	 * @param idNode
	 */
	private CommonTableColumn createTableColumn(Node idNode) {
		CommonTableColumn column = new CommonTableColumn();
		column.setColumn(getAttrbute(idNode, "column", true));
		column.setProperty(getAttrbute(idNode, "property", true));
		column.setJdbcType(getAttrbute(idNode, "jdbcType", false));
		
		return column;
	}
	
	/**
	 * 获取节点属性
	 * @param node 节点   
	 * @param name 属性名称
	 * @return
	 */
	private String getAttrbute(Node node, String name, boolean necessary) {
		Node attr = node.getAttributes().getNamedItem(name);
		String value = null;
		
		if (attr != null) {
			value = attr.getNodeValue();
		}
		
		if (necessary && StringUtils.isEmpty(value)) {
			throw new InvalidXmlMapperException("Can't find " + name + " attrbute in result map");
		}
		
		return value;
	}
//
//	private Properties parseAttributes(Node n) {
//		Properties variables = new Properties();
//		Properties attributes = new Properties();
//		NamedNodeMap attributeNodes = n.getAttributes();
//		if (attributeNodes != null) {
//			for (int i = 0; i < attributeNodes.getLength(); i++) {
//				Node attribute = attributeNodes.item(i);
//				String value = PropertyParser.parse(attribute.getNodeValue(), variables);
//				attributes.put(attribute.getNodeName(), value);
//			}
//		}
//		return attributes;
//	}
	

	/**
	 * @return the mapperLocations
	 */
	public Resource[] getMapperLocations() {
		return mapperLocations;
	}

	/**
	 * @param mapperLocations the mapperLocations to set
	 */
	public void setMapperLocations(Resource[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}
	
	
	/**
	 * @return the baseResultMap
	 */
	public String getBaseResultMap() {
		return baseResultMap;
	}

	/**
	 * @param baseResultMap the baseResultMap to set
	 */
	public void setBaseResultMap(String baseResultMap) {
		this.baseResultMap = baseResultMap;
	}

	/**
	 * @return the baseTableName
	 */
	public String getBaseTableName() {
		return baseTableName;
	}

	/**
	 * @param baseTableName the baseTableName to set
	 */
	public void setBaseTableName(String baseTableName) {
		this.baseTableName = baseTableName;
	}

	public String getGenerationType() {
		return generationType;
	}

	public void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

    public String getBaseColumns() {
        return baseColumns;
    }

    public void setBaseColumns(String baseColumns) {
        this.baseColumns = baseColumns;
    }

    /**
	 * @return the configPath
	 */
	public String getConfigPath() {
		return configPath;
	}

	/**
	 * @param configPath the configPath to set
	 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	/**
	 * @return 数据库Key生成方式
	 */
	public KeyGenMode getKeyGenMode() {
		return keyGenMode;
	}

	/**
	 * @param keyGenMode 设置数据库Key生成方式
	 */
	public void setKeyGenMode(KeyGenMode keyGenMode) {
		this.keyGenMode = keyGenMode;
	}
	
}

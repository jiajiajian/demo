package cn.com.tiza.dal.beetlsql.ext;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.sql.core.JavaType;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.ColDesc;
import org.beetl.sql.core.db.MetadataManager;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.kit.StringKit;
import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ServiceGen {
    /**
     * logger
     */
    public static String defaultPkg = "cn.com.tiza.service";
    private MetadataManager mm;
    private SQLManager sm ;
    private String table;
    private String pkg;
    private String srcPath;
    private GenConfig config;
    private static String srcHead ;
    public static final String CR = System.getProperty("line.separator");
    private final static String defaultTemplatePath = "/cn/com/tiza/dal/beetlsql/ext/service.btl";
    /**
     * 代码生成的Beetl的GroupTemplate，与BeetSQL 不同
     */
    private static GroupTemplate gt = null;

    static{
        Configuration conf = null;
        try {
            conf = Configuration.defaultConfiguration();
            conf.setStatementStart("<%");
            conf.setStatementEnd("%>");
        } catch (IOException e) {
            throw new RuntimeException("build defaultConfiguration error",e);
        }

        gt = new GroupTemplate(new StringTemplateResourceLoader(),conf);
        StringBuffer t=new StringBuffer();
        srcHead=t.toString();
    }

    public static String getSrcHead() {
        return srcHead;
    }

    public static GroupTemplate getGt() {
        return gt;
    }


    public ServiceGen(SQLManager sm, String table, String pkg, String srcPath, GenConfig config){
        this.mm = sm.getMetaDataManager();
        this.sm = sm;
        this.table = table;
        this.pkg = pkg;
        this.srcPath = srcPath;
        this.config = config;
    }
    /**
     * 生成代码
     *
     */
    public void gen() throws Exception{
        final TableDesc tableDesc = mm.getTable(table);
        String className = sm.getNc().getClassName(tableDesc.getName());
        if (config.getIgnorePrefix() != null && !config.getIgnorePrefix().trim().equals("")) {
            className = className.replaceFirst(StringKit.toUpperCaseFirstOne(config.getIgnorePrefix()), "");
        }
        String ext = null;

        if(config.getBaseClass()!=null){
            ext = config.getBaseClass();
        }

        Set<String> cols = tableDesc.getCols();
        List<Map> attrs = new ArrayList<Map>();
        for(String col:cols){

            ColDesc desc = tableDesc.getColDesc(col);
            Map attr = new HashMap();
            attr.put("comment", desc.remark);
            String attrName = sm.getNc().getPropertyName(null, desc.colName);
            attr.put("name", attrName);
            attr.put("methodName", getMethodName(attrName));

            boolean isKey = tableDesc.getIdNames().contains(desc.colName);
            attr.put("isKey", isKey);

            String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
            if(config.isPreferBigDecimal()&&type.equals("Double")){
                type = "BigDecimal";
            }
            if(config.isPreferDate()&&type.equals("Timestamp")){
                type ="Date";
            }

            attr.put("type", type);
            attr.put("desc", desc);
            attrs.add(attr);
        }

        if(config.getPropertyOrder()==config.ORDER_BY_TYPE) {
            // 主键总是排在前面，int类型也排在前面，剩下的按照字母顺序排
            Collections.sort(attrs,new Comparator<Map>() {

                @Override
                public int compare(Map o1, Map o2) {
                    ColDesc desc1  = (ColDesc)o1.get("desc");
                    ColDesc desc2  = (ColDesc)o2.get("desc");
                    int score1 = score(desc1);
                    int score2 = score(desc2);
                    if(score1==score2){
                        return desc1.colName.compareTo(desc2.colName);
                    }else{
                        return score2-score1;
                    }


                }

                private int score(ColDesc desc){
                    if(tableDesc.getIdNames().contains(desc.colName)){
                        return 99;
                    }else if(JavaType.isInteger(desc.sqlType)){
                        return 9;
                    }else if(JavaType.isDateType(desc.sqlType)){
                        return  -9;
                    }else{
                        return  0;
                    }
                }


            });
        }



        Template template = gt.getTemplate(config.getTemplate(defaultTemplatePath));
        template.binding("attrs", attrs);
        template.binding("className", className);
        template.binding("lowClassName", getClassName(className));
        template.binding("table",table);
        template.binding("ext", ext);
        template.binding("package", pkg);
        template.binding("imports", srcHead);
        template.binding("comment", tableDesc.getRemark());
        template.binding("catalog", tableDesc.getCatalog());
        template.binding("implSerializable", config.isImplSerializable());

        String code = template.render();
        if(config.isDisplay()){
            System.out.println(code);
        }else{
            saveSourceFile(srcPath,pkg,className,code);
        }


        for(CodeGen codeGen:config.codeGens){
            codeGen.genCode(pkg, className, tableDesc, config,config.isDisplay());
        }




    }

    public static  void saveSourceFile(String srcPath,String pkg,String className,String content) throws IOException{
        String file = srcPath+File.separator+pkg.replace('.',File.separatorChar);
        File f  = new File(file);
        if (!f.exists()) {
            boolean succ=f.mkdirs();
            if(!succ){
                throw  new IOException("创建文件夹失败 "+f);
            }
        }
        File target = new File(file,className+"Service.java");

        FileWriter writer = new FileWriter(target);
        try{
            writer.write(content);
        }finally {
            writer.close();
        }
    }

    private String getMethodName(String name){
        if(name.length()==1){
            return name.toUpperCase();
        }
        char ch1 = name.charAt(0);
        char ch2 = name.charAt(1);
        if(Character.isLowerCase(ch1)&&Character.isUpperCase(ch2)){
            //aUname---> getaUname();
            return name;
        }else if(Character.isUpperCase(ch1)&&Character.isUpperCase(ch2)){
            //ULR --> getURL();
            return name ;
        }else{
            //general  name --> getName()
            char upper = Character.toUpperCase(ch1);
            return upper+name.substring(1);
        }
    }


    private String getClassName(String name){
        if(name.length()==1){
            return name.toLowerCase();
        }
        char ch1 = name.charAt(0);
        if(Character.isLowerCase(ch1)){
            //ULR --> getURL();
            return name ;
        }else{
            //general  name --> getName()
            char upper = Character.toLowerCase(ch1);
            return upper+name.substring(1);
        }
    }
}

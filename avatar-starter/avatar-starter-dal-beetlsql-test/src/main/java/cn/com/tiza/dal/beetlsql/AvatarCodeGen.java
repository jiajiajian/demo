package cn.com.tiza.dal.beetlsql;

import cn.com.tiza.dal.beetlsql.ext.*;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.kit.GenKit;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.MapperCodeGen;

import java.util.Set;

public class AvatarCodeGen {

    public static final void main(String[] args) {
        Config.dbInit("mysql");
        System.setProperty("user.dir", System.getProperty("user.dir") + "/support/generator");
        String template = "/cn/com/tiza/dal/beetlsql/ext/pojo.btl";
        GenConfig config = new GenConfig(template);
        config.setIgnorePrefix("t");
        config.preferBigDecimal(true);
        MapperCodeGen mapper = new MapperCodeGen("cn.com.tiza.dao");
        config.codeGens.add(mapper);
        SQLManager sqlManager = SqlKit.$();
        try {
            sqlManager.genALL("cn.com.tiza.domain", config, null);
            Set<String> tables = sqlManager.getMetaDataManager().allTable();

            for (String table : tables) {
                table = sqlManager.getMetaDataManager().getTable(table).getName();
                try {
                    // 生成代码
                    genAllDtoCode(sqlManager, table, config);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void genAllDtoCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        genDtoCode(sqlManager, table, config);
        genVMCode(sqlManager, table, config);
        genDtoMapperCode(sqlManager, table, config);
        genServiceCode(sqlManager, table, config);
        genQueryCode(sqlManager, table, config);
        genControllerCode(sqlManager, table, config);
    }

//
//    public static void genPOJOCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
//        String srcPath = GenKit.getJavaSRCPath();
//        SourceGen gen = new SourceGen(sqlManager, table, "cn.com.tiza.domain", srcPath, config);
//        gen.gen();
//    }

    public static void genControllerCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        String srcPath = GenKit.getJavaSRCPath();
        ControllerGen gen = new ControllerGen(sqlManager, table, "cn.com.tiza.web.rest", srcPath, config);
        gen.gen();
    }

    public static void genQueryCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        String srcPath = GenKit.getJavaSRCPath();
        QueryGen gen = new QueryGen(sqlManager, table, "cn.com.tiza.service.dto", srcPath, config);
        gen.gen();
    }


    public static void genServiceCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        String srcPath = GenKit.getJavaSRCPath();
        ServiceGen gen = new ServiceGen(sqlManager, table, "cn.com.tiza.service", srcPath, config);
        gen.gen();
    }

    public static void genVMCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        String srcPath = GenKit.getJavaSRCPath();
        VMGen gen = new VMGen(sqlManager, table, "cn.com.tiza.web.rest.vm", srcPath, config);
        gen.gen();
    }

    public static void genDtoCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        String srcPath = GenKit.getJavaSRCPath();
        DtoGen gen = new DtoGen(sqlManager, table, "cn.com.tiza.service.dto", srcPath, config);
        gen.gen();
    }

    public static void genDtoMapperCode(SQLManager sqlManager, String table, GenConfig config) throws Exception {
        String srcPath = GenKit.getJavaSRCPath();
        DtoMapperGen gen = new DtoMapperGen(sqlManager, table, "cn.com.tiza.service.mapper", srcPath, config);
        gen.gen();
    }
}

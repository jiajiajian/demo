package cn.com.tiza.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.engine.PageQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tiza
 */
public abstract class Query {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页条数
     */
    private Integer limit = 10;

    /**
     * 查询默认有组织id
     */
    private Long organizationId;

    /**
     * 查询默认有组织id
     */
    private Long financeId;

    private Map<String, Object> params = new HashMap<>();

    private boolean init = false;

    public void initParams() {
        if (init) {
            return;
        }
        this.init = true;
        if (this.page == null) {
            this.page = 1;
        }
        if (this.limit == null) {
            this.limit = 10;
        }
        convertParams();
    }

    /**
     * <pre>
     *     通过调用@see add(key, value, boolean) 方法，将参数添加到params中
     * </pre>
     */
    protected abstract void convertParams();

    public PageQuery toPageQuery() {
        addCommonParas();
        initParams();
        PageQuery pageQuery = new PageQuery<>();
        pageQuery.setPageSize(this.limit);
        pageQuery.setPageNumber(this.page);
        pageQuery.setParas(params);
        return pageQuery;
    }

    private void addCommonParas() {
        add("organizationId", organizationId);
        add("financeId", financeId);
    }

    /**
     * 获取参数 适用于非分页查询中包含like条件
     */
    public Map<String, Object> params() {
        addCommonParas();
        convertParams();
        return this.params;
    }

    /**
     * 添加一个参数，不做like处理
     *
     * @param key
     * @param value
     */
    protected void add(String key, Object value) {
        add(key, value, false);
    }

    /**
     * 添加一个查询参数
     *
     * @param key
     * @param value
     * @param like  是否是like条件参数
     */
    protected void add(String key, Object value, boolean like) {
        if (value != null) {
            if (like && value instanceof String) {
                value = fuzzy((String) value);
            } else if (value instanceof BaseEnum) {
                value = ((BaseEnum) value).getValue();
            }
            this.params.put(key, value);
        }
    }

    protected String fuzzy(String str) {
        if (str.trim().length() == 0) {
            return null;
        }
        //将特殊字符进行转义处理
        str = str.replaceAll("%", "\\\\%");
        str = str.replaceAll("_", "\\\\_");
        str = str.replaceAll("\\\\", "\\\\\\\\");
        return "%" + str + "%";
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getFinanceId() {
        return financeId;
    }

    public void setFinanceId(Long financeId) {
        this.financeId = financeId;
    }
}

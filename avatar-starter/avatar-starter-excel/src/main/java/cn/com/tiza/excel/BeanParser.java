package cn.com.tiza.excel;

/**
 * 把bean序列化为字符串数组
 *
 * @param <T> 要序列化的bean类型
 */
public interface BeanParser<T> {
    /**
     * 把并按预先约定顺序列化为字符串数组
     *
     * @param bean 要序列化的bean
     * @return
     */
    String[] parser(T bean);
}

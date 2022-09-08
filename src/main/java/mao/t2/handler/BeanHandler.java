package mao.t2.handler;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2.handler
 * Class(类名): BeanHandler
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 13:59
 * Version(版本): 1.0
 * Description(描述)： 无
 */

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/*
接口：
public interface ResultSetHandler<T>
{
    <T> T handler(ResultSet resultSet);
}
 */

@SuppressWarnings("all")
public class BeanHandler<T> implements ResultSetHandler<T>
{

    //class对象类型的变量
    private final Class<T> beanClass;

    //构造方法赋值
    public BeanHandler(Class<T> beanClass)
    {
        this.beanClass = beanClass;
    }

    /**
     * 根据结果集对象返回对应的对象
     *
     * @param resultSet 结果集对象
     * @return 对象
     */
    @Override
    public T handler(ResultSet resultSet)
    {
        //声明自定义类型
        T bean = null;
        try
        {
            //为自定义对象进行赋值
            bean = beanClass.getDeclaredConstructor().newInstance();
            //获取结果集的数据
            if (resultSet.next())
            {
                //通过结果集的对象获取结果源信息对象
                ResultSetMetaData metaData = resultSet.getMetaData();
                //通过源信息对象获取列数
                int count = metaData.getColumnCount();
                //循环遍历列数
                for (int i = 1; i <= count; i++)
                {
                    //获取列名
                    String columnName = metaData.getColumnName(i);
                    //通过列名获取数据
                    Object object = resultSet.getObject(columnName);
                    //创建属性描述器对象,对对象的set方法进行赋值
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName.toLowerCase(), beanClass);
                    //获取set方法
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    //执行set方法,赋值
                    writeMethod.invoke(bean, object);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //返回对象
        return bean;
    }
}


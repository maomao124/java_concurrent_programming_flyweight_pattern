package mao.t2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2
 * Class(类名): Connection
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 13:37
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Connection extends ConnectionAdapter
{
    //连接对象
    private final java.sql.Connection connection;
    //连接池
    private final List<java.sql.Connection> pool;

    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    /**
     * 构造方法对成员变量赋值
     *
     * @param connection 连接对象
     * @param pool       连接池
     */
    public Connection(java.sql.Connection connection, List<java.sql.Connection> pool)
    {
        super(connection);
        this.connection = connection;
        this.pool = pool;
    }

    //重写close方法

    /**
     * 重写close方法 完成归还连接操作
     *
     * @throws SQLException 抛出异常
     */
    @Override
    public void close() throws SQLException
    {
        connection.setAutoCommit(true);     //重新将连接设置为自动提交
        pool.add(connection);               //归还连接
        log.debug("归还连接：" + connection);
    }
}

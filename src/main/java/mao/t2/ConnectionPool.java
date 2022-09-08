package mao.t2;

import javax.sql.DataSource;
import java.awt.*;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2
 * Class(类名): ConnectionPool
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 13:39
 * Version(版本): 1.0
 * Description(描述)： 无
 */


public class ConnectionPool implements DataSource
{
    //连接池对象的数量
    private static final int ConnectionPoolSize = 10;

    //连接池，线程安全
    private static final List<Connection> pool = Collections.synchronizedList(new ArrayList<>());

    static
    {
        try
        {
            for (int i = 0; i < ConnectionPoolSize; i++)
            {
                Connection connection = JDBC.getConnection();
                pool.add(connection);
            }
        }
        catch (Exception e)
        {
            System.err.println("连接池初始化失败！");
            Toolkit.getDefaultToolkit().beep();
            e.printStackTrace();
        }
    }

    /**
     * 返回连接池剩余的连接数量
     *
     * @return int型 连接池剩余的连接数量
     */
    public int getPoolSize()
    {
        return pool.size();
    }

    /**
     * 获取连接池最大连接的数量，此数量固定
     *
     * @return int型 返回最大连接的数量
     */
    public static int getConnectionPoolSize()
    {
        return ConnectionPoolSize;
    }


    @SuppressWarnings("all")
    /**
     * 获得连接 动态代理方式
     *
     * @return 加强的连接对象，此对象调用close方法归还连接，而不是关闭连接
     * @throws SQLException 数据库异常
     */
    /*
    @Override
    public Connection getConnection() throws SQLException
    {
        if (pool.size() > 0)
        {
            Connection connection = pool.remove(0);
            @SuppressWarnings("all")
            Connection proxy = (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler()
            {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                {
                    if (method.getName().equals("close"))
                    {
                        //归还连接
                        connection.setAutoCommit(true);     //重新将连接设置为自动提交
                        pool.add(connection);               //归还连接
                        return null;
                    }
                    else
                    {
                        return method.invoke(connection, args);
                    }
                }
            });
            return proxy;
        }
        else
        {
            throw new RuntimeException("连接池连接数量已经用尽！");
        }
    }
     */

    /**
     * 获得连接 装饰设计模式或者适配器设计模式
     *
     * @return 加强的连接对象，此对象调用close方法归还连接，而不是关闭连接
     * @throws SQLException 数据库异常
     */
    @Override
    public mao.t2.Connection getConnection() throws SQLException
    {
        if (pool.size() > 0)
        {
            Connection connection = pool.remove(0);
            @SuppressWarnings("all")
            mao.t2.Connection con = new mao.t2.Connection(connection, pool);
            return con;
        }
        else
        {
            throw new RuntimeException("连接池连接数量已经用尽！");
        }
    }


    @Override
    public Connection getConnection(String username, String password) throws SQLException
    {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException
    {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException
    {

    }

    @Override
    public int getLoginTimeout() throws SQLException
    {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return false;
    }
}
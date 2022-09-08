package mao.t2;

import java.util.HashMap;
import java.util.List;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2
 * Class(类名): Test2
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 14:30
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Test2
{
    public static void main(String[] args)
    {
        List<HashMap<String, Object>> list = JDBCTemplate.queryForListMap("select * from class");
        for (HashMap<String, Object> map : list)
        {
            System.out.println(map);
        }
    }
}

package springboot.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import springboot.modal.vo.UserVo;
import springboot.modal.vo.UserVoExample;

import java.util.List;

/**
 * @author tangj
 * @date 2018/1/21 14:59
 */
@Component
public interface UserVoMapper {
    long countByExample(UserVoExample example);

    int deleteByExample(UserVoExample example);

    int deleteByPrimaryKey(Integer uid);

    int insert(UserVo record);

    int insertSelective(UserVo record);

    List<UserVo> selectByExample(UserVoExample example);

    UserVo selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByExample(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);

    // List<UserVo> userLogin(UserVo userVo);
    // UserVo userLogin(@Param("userVo") userVo);
    UserVo userLogin(UserVo userVo);

    /*-----------------传多个参数----------------------------*/
    /**
     * MyBatis提供了一个使用注解来参入多个参数的方式。这种方式需要在接口的参数上添加@Param注解
     * 此处要注意的是，由于该方法需要传入多个参数，在进行Mybatis配置时，
     * 没有办法同时配置多个参数，另外MyBatis还提供了一个使用注解来参入
     * 多个参数的方式。这种方式需要在接口的参数上添加@Param注解。。
     * 注意，以下两种写法是完全相同的。但使用的时候要使用第一种类型
     */
    // User login(@Param(value = "name") String name, @Param(value = "password") String password);

//    mapper.xml 配置如下：
// <select id="login"resultType="com.pojo.User">
//    select *
//    from us
//    where name =#{name}
//    and password =#{password}
//   </select>

//括号中为User实体的属性名称

    /*----------------pojo的对象传入-----------------------------*/
//    pojo的对象传入
//    public interface UserMappe{
//        public User login(User user);
//    }

//<select id="login" resultType="user">
//    select* from user where username=#{username} and password= #{password};
//</select>　　

//    注意:占位符中当参数传递的是pojo的时候，括号中的内容是pojo的属性

    /*------------------map传入方式---------------------------*/
//    map传入方式
//    public interface UserMappe{
//        public User login(Map<String ,Object> map);
//    }
//
//<select id="login" resultType="user">
//    select* from user where username=#{username} and password= #{password};
//</select>
}

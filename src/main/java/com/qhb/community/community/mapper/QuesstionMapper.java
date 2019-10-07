package com.qhb.community.community.mapper;

import com.qhb.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuesstionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag)" +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
    @Select("select * from question limit #{offset},#{size}") //传非数据库得对象需要自己做映射
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
    @Select("select count(1) from question")
    Integer count();
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUser(@Param("userId") Integer userId, @Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
    @Select("select count(1) from question where creator =#{userId}")
    Integer countByUserId(@Param("userId")Integer userId);
    @Select("select * from question where id = #{id }")
    Question getId(@Param("id")Integer id);
@Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
}

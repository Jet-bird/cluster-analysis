package dao;

import model.TmProductDataModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TmProductDataDao {

    @Select("SELECT * FROM tm_product_data WHERE id = #{id}")
    TmProductDataModel getById(@Param("id") Integer id);

    @Select("select IFNULL(MIN(id),0)  FROM tm_product_data")
    Integer selectMinId();

    @Select("select IFNULL( MAX(id) ,0) FROM tm_product_data")
    Integer selectMaxId();
}

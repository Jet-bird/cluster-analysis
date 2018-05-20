package vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TmProductDataVO {
    private Integer id;
    private String productDescription;
    private String productName;
    private String productId;
    private String shopId;
    private String shopName;
    private String brandId;
    private String brandName;
    private String productPlace;
    private String productArgs;
    private Integer goodCommentNum;
    private Integer midCommentNum;
    private Integer badCommentNum;
    private Integer goodCommentRate;
    private Integer commentNum;
    private BigDecimal productPrice;
    private String updateTime;
    private String updateUser;
    private String createUser;
    private String createTime;

}



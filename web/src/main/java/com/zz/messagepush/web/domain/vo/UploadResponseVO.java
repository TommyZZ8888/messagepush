package com.zz.messagepush.web.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 上传成功后返回素材的id
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseVO {

    private String id;
}

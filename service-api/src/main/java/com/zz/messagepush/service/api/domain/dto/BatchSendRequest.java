package com.zz.messagepush.service.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchSendRequest {

private String code;

private Long messageTemplateId;

private List<MessageParam> messageParamList;

}

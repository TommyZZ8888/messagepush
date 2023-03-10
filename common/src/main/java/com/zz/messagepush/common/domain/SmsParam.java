package com.zz.messagepush.common.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;


@Data
@Builder
public class SmsParam {

    private Set<String> phones;

    private String content;
}

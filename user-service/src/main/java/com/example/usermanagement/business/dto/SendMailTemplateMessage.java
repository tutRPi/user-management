package com.example.usermanagement.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
@Getter
@Setter
public class SendMailTemplateMessage implements Serializable {
    @JsonProperty("version")
    final int version = 1;

    @JsonProperty("receiver")
    String receiver;

    @JsonProperty("subject")
    String subject;

    @JsonProperty("templateName")
    String templateName;

    @JsonProperty("templateModel")
    transient Map<String, Object> templateModel;
}

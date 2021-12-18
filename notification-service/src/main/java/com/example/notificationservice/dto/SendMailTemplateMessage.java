package com.example.notificationservice.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SendMailTemplateMessage implements Serializable {
    @JsonProperty("version")
    int version;

    @JsonProperty("receiver")
    String receiver;

    @JsonProperty("subject")
    String subject;

    @JsonProperty("templateName")
    String templateName;

    @JsonProperty("templateModel")
    transient Map<String, Object> templateModel;
}

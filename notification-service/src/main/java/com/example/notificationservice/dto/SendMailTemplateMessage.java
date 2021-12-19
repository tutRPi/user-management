package com.example.notificationservice.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@JsonInclude
public class SendMailTemplateMessage implements Serializable {

    String receiver;

    String language;

    String subjectKey;

    String templateName;

    transient Map<String, Object> templateModel;
}

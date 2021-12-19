package com.example.usermanagement.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class SendMailTemplateMessage implements Serializable {

    String receiver;

    String language;

    String subjectKey;

    String templateName;

    transient Map<String, Object> templateModel;
}

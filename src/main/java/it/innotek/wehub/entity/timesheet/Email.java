/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    private String from;
    private String to;
    private String subject;
    private String template;
    private Map<String, Object> properties;

}

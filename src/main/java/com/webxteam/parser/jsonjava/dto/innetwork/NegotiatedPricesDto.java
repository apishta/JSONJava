package com.webxteam.parser.jsonjava.dto.innetwork;

import java.util.List;

public class NegotiatedPricesDto {
    public String negotiated_type;
    public String negotiated_rate;
    public String expiration_date;
    public List<String> service_code;
    public String billing_class;
    public String additional_information;
    public List<String> billing_code_modifier;
}

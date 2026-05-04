package com.smartfacility.support.dto;

import lombok.Data;

@Data
public class TicketStatusRequest {

    private String status;
    private String remarks;
}
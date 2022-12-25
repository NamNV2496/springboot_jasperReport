package com.example.jasper.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    String fullName;
    String telephone;
}

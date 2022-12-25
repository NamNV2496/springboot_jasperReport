package com.example.jasper.controller;

import com.example.jasper.domain.Customer;
import com.example.jasper.service.JasperReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {
    private final JasperReportService jasperReportService;

    @GetMapping("/test")
    public void test() {
        Customer customer = Customer.builder().fullName("Nguyễn Văn Nam").telephone("09342834433").build();
        jasperReportService.exportReport(customer);
    }
}

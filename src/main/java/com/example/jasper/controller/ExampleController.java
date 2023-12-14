package com.example.jasper.controller;

import com.example.jasper.domain.Customer;
import com.example.jasper.service.JasperReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExampleController {
    private final JasperReportService jasperReportService;

    @GetMapping("/testPDF")
    public void testPDF() {
        Customer customer = Customer.builder().fullName("Nguyễn Văn Nam").telephone("09342834433").build();
        List<Object> dataSource = new ArrayList<>();
        dataSource.add(Customer.builder().fullName("datasource tesst").build());
        jasperReportService.exportReport(customer, dataSource);
    }

    @GetMapping("/testHtml")
    public byte[] testHtml() {
        Customer customer = Customer.builder().fullName("Nguyễn Văn Nam").telephone("09342834433").build();
        List<Object> dataSource = new ArrayList<>();
        dataSource.add(Customer.builder().fullName("datasource tesst").build());
        return jasperReportService.exportReportHTML(customer, dataSource);
    }
}

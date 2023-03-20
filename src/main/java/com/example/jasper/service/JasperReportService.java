package com.example.jasper.service;

import com.example.jasper.domain.Customer;
import net.sf.jasperreports.engine.JasperReport;

import java.util.List;

public interface JasperReportService {
    JasperReport generateReport(Customer customer);
    void exportReport(Customer customer, List<Object> dataSource);
}

package com.example.jasper.service;

import com.example.jasper.domain.Customer;
import net.sf.jasperreports.engine.JasperReport;

public interface JasperReportService {
    JasperReport generateReport(Customer customer);
    void exportReport(Customer customer);
}

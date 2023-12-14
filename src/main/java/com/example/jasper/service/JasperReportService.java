package com.example.jasper.service;

import com.example.jasper.domain.Customer;
import net.sf.jasperreports.engine.JasperReport;

import java.util.List;

public interface JasperReportService {

    void exportReport(Customer customer, List<Object> dataSource);

    byte[] exportReportHTML(Customer customer, List<Object> dataSource);
}

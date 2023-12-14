package com.example.jasper.service.Impl;

import com.example.jasper.domain.Customer;
import com.example.jasper.service.JasperReportService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JasperReportImpl implements JasperReportService {
    @Value("classpath:image/logo.png")
    Resource resourceFile;


    private JasperReport generateReport() {
        try {
            InputStream exampleReportStream
                    = getClass().getResourceAsStream("/jrxml/template.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(exampleReportStream);
            // To avoid compiling it every time, we can save it to a file:
//            JRSaver.saveObject(jasperReport, "exampleReport.jasper");
            return jasperReport;
        } catch (JRException ex) {
            log.error(JasperReportImpl.class.getName());
        }
        return null;
    }

    @SneakyThrows
    @Override
    public void exportReport(Customer customer, List<Object> dataSource) {
        JasperPrint jasperPrint = new JasperPrint();
        try {

            InputStream logoInputStream = resourceFile.getInputStream();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fullName", customer.getFullName());
            parameters.put("telephone", customer.getTelephone());
            // inset image
            parameters.put("logo", logoInputStream);

            JRDataSource dataSource1 = new JRBeanCollectionDataSource(dataSource);
//            Way 1: empty dataSource
//            jasperPrint = JasperFillManager.fillReport(generateReport(customer), parameters, new JREmptyDataSource());
//            WAY 2: dataSource in memory: data will be saved in database and jrxml file will load it from memory
            jasperPrint = JasperFillManager.fillReport(this.generateReport(), parameters, dataSource1);
//            way 3: load from database => modify jrxml with <queryString>
//            // Create a connection to the database
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "password");
//
//            // Create a statement to execute the SQL query
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
//
//            // Create a JRResultSetDataSource using the ResultSet
//            JRResultSetDataSource dataSource1 = new JRResultSetDataSource(rs);
//            jasperPrint = JasperFillManager.fillReport(generateReport(customer), parameters, dataSource1);

        } catch (JRException ex) {
            log.error(JasperReportImpl.class.getName());
        }


        // print report to file
////        ============================= WAY 1 =============================
        try {
            OutputStream output = new FileOutputStream(new File("out-put/test.pdf"));
            JasperExportManager.exportReportToPdfStream(jasperPrint, output);
            output.close();
        } catch (Exception ex) {

        }

////        ============================= WAY 2 =============================
//        JRPdfExporter exporter = new JRPdfExporter();
//
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("out-put/exampleReport.pdf"));
//
//        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
//        reportConfig.setSizePageToContent(true);
//        reportConfig.setForceLineBreakPolicy(false);
//
//        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
//        exportConfig.setMetadataAuthor("namnv");
////        exportConfig.setEncrypted(false);
////        exportConfig.setAllowedPermissionsHint("PRINTING");
//
//        exporter.setConfiguration(reportConfig);
//        exporter.setConfiguration(exportConfig);
//        try {
//            exporter.exportReport();
//        } catch (JRException ex) {
//            log.error(JasperReportImpl.class.getName());
//        }
    }

    @SneakyThrows
    @Override
    public byte[] exportReportHTML(Customer customer, List<Object> dataSource) {

        InputStream logoInputStream = resourceFile.getInputStream();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fullName", customer.getFullName());
        parameters.put("telephone", customer.getTelephone());
        // inset image
        parameters.put("logo", logoInputStream);

        JRDataSource dataSource1 = new JRBeanCollectionDataSource(dataSource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(this.generateReport(), parameters, dataSource1);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Exporter exporter = new HtmlExporter();
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.exportReport();
        return out.toByteArray();
    }
}

package com.example.jasper.service.Impl;

import com.example.jasper.domain.Customer;
import com.example.jasper.service.JasperReportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JasperReportImpl implements JasperReportService {
    @Value("classpath:image/logo.png")
    Resource resourceFile;

    @Override
    public JasperReport generateReport(Customer customer) {
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
    public void exportReport(Customer customer) {
        JasperPrint jasperPrint = new JasperPrint();
        try {

            InputStream logoInputStream = resourceFile.getInputStream();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fullName", customer.getFullName());
            parameters.put("telephone", customer.getTelephone());
            // inset image
            parameters.put("logo", logoInputStream);

            jasperPrint = JasperFillManager.fillReport(generateReport(customer), parameters, new JREmptyDataSource());
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
}

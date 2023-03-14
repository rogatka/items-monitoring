package com.items.monitoring.service.reports;

import com.items.monitoring.model.Report;

public interface ReportExporter {
    Report export();

    ReportCase getReportCase();
}

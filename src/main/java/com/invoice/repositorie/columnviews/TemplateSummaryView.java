package com.invoice.repositorie.columnviews;

import java.time.Instant;

public interface TemplateSummaryView {
    String getTemplateName();
    Long getTemplateId();
    Instant getUpdatedAt();
}

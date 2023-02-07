package com.items.monitoring.service.reports;

import com.items.monitoring.model.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@ToString
@EqualsAndHashCode
public class ReportCase {
    private ItemCategory itemCategory;
    private String fileExtension;
}

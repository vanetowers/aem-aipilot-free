/*
 *  Copyright 2025 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.merkle.aipilot.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LayoutContainerModel {

    public static final String RESOURCE_TYPE = "aem-aipilot/components/layout-container";

    @ValueMapValue
    @Default(values = "12")
    private String columnLayout;

    @SlingObject
    private Resource resource;

    private List<LayoutColumn> columns;

    @PostConstruct
    protected void init() {
        String layout = columnLayout != null ? columnLayout : "12";
        List<LayoutColumn> result = new ArrayList<>();

        String prefix = "cmp-layout-container__column ";
        switch (layout) {
            case "8-4":
                result.add(new LayoutColumn(prefix + "col-8", "col1"));
                result.add(new LayoutColumn(prefix + "col-4", "col2"));
                break;
            case "4-8":
                result.add(new LayoutColumn(prefix + "col-4", "col1"));
                result.add(new LayoutColumn(prefix + "col-8", "col2"));
                break;
            case "6-6":
                result.add(new LayoutColumn(prefix + "col-6", "col1"));
                result.add(new LayoutColumn(prefix + "col-6", "col2"));
                break;
            case "4-4-4":
                result.add(new LayoutColumn(prefix + "col-4", "col1"));
                result.add(new LayoutColumn(prefix + "col-4", "col2"));
                result.add(new LayoutColumn(prefix + "col-4", "col3"));
                break;
            default:
                result.add(new LayoutColumn(prefix + "col-12", "col1"));
                break;
        }

        columns = Collections.unmodifiableList(result);
    }

    public List<LayoutColumn> getColumns() {
        return columns;
    }

    public boolean hasContent() {
        return columns != null && !columns.isEmpty();
    }

    public static class LayoutColumn {
        private final String cssClass;
        private final String childName;

        LayoutColumn(String cssClass, String childName) {
            this.cssClass = cssClass;
            this.childName = childName;
        }

        public String getCssClass() {
            return cssClass;
        }

        public String getChildName() {
            return childName;
        }
    }
}

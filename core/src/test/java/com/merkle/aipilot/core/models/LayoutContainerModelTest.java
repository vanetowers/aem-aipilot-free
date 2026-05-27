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

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;
import com.merkle.aipilot.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class LayoutContainerModelTest {

    private final AemContext context = AppAemContext.newAemContext();

    private Page page;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(LayoutContainerModel.class);
        page = context.create().page("/content/mypage");
    }

    @Test
    void testDefaultLayoutFullWidth() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE);

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertNotNull(model);
        assertEquals(1, model.getColumns().size());
        assertEquals("cmp-layout-container__column col-12", model.getColumns().get(0).getCssClass());
        assertEquals("col1", model.getColumns().get(0).getChildName());
        assertTrue(model.hasContent());
    }

    @Test
    void testLayout8_4() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE,
            "columnLayout", "8-4");

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertEquals(2, model.getColumns().size());
        assertEquals("cmp-layout-container__column col-8", model.getColumns().get(0).getCssClass());
        assertEquals("col1", model.getColumns().get(0).getChildName());
        assertEquals("cmp-layout-container__column col-4", model.getColumns().get(1).getCssClass());
        assertEquals("col2", model.getColumns().get(1).getChildName());
    }

    @Test
    void testLayout4_8() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE,
            "columnLayout", "4-8");

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertEquals(2, model.getColumns().size());
        assertEquals("cmp-layout-container__column col-4", model.getColumns().get(0).getCssClass());
        assertEquals("col1", model.getColumns().get(0).getChildName());
        assertEquals("cmp-layout-container__column col-8", model.getColumns().get(1).getCssClass());
        assertEquals("col2", model.getColumns().get(1).getChildName());
    }

    @Test
    void testLayout6_6() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE,
            "columnLayout", "6-6");

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertEquals(2, model.getColumns().size());
        assertEquals("cmp-layout-container__column col-6", model.getColumns().get(0).getCssClass());
        assertEquals("col1", model.getColumns().get(0).getChildName());
        assertEquals("cmp-layout-container__column col-6", model.getColumns().get(1).getCssClass());
        assertEquals("col2", model.getColumns().get(1).getChildName());
    }

    @Test
    void testLayout4_4_4() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE,
            "columnLayout", "4-4-4");

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertEquals(3, model.getColumns().size());
        for (LayoutContainerModel.LayoutColumn col : model.getColumns()) {
            assertEquals("cmp-layout-container__column col-4", col.getCssClass());
        }
        assertEquals("col1", model.getColumns().get(0).getChildName());
        assertEquals("col2", model.getColumns().get(1).getChildName());
        assertEquals("col3", model.getColumns().get(2).getChildName());
    }

    @Test
    void testLayoutFullWidth() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE,
            "columnLayout", "12");

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertEquals(1, model.getColumns().size());
        assertEquals("cmp-layout-container__column col-12", model.getColumns().get(0).getCssClass());
        assertEquals("col1", model.getColumns().get(0).getChildName());
    }

    @Test
    void testInvalidLayoutFallsBackToFullWidth() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE,
            "columnLayout", "invalid");

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertEquals(1, model.getColumns().size());
        assertEquals("cmp-layout-container__column col-12", model.getColumns().get(0).getCssClass());
    }

    @Test
    void testHasContentWithColumns() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE);

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertTrue(model.hasContent());
    }

    @Test
    void testGetColumnsReturnsNonNull() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE);

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertNotNull(model.getColumns());
    }

    @Test
    void testLayoutColumnConstructor() {
        LayoutContainerModel.LayoutColumn column =
            new LayoutContainerModel.LayoutColumn("col-4", "col2");

        assertEquals("col-4", column.getCssClass());
        assertEquals("col2", column.getChildName());
    }

    @Test
    void testColumnsListIsUnmodifiable() {
        Resource resource = context.create().resource(page, "layout",
            "sling:resourceType", LayoutContainerModel.RESOURCE_TYPE);

        LayoutContainerModel model = resource.adaptTo(LayoutContainerModel.class);

        assertThrows(UnsupportedOperationException.class, () -> model.getColumns().clear());
    }
}

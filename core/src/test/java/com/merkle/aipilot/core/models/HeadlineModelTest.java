/*
 *  Copyright 2015 Adobe Systems Incorporated
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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import com.merkle.aipilot.core.testcontext.AppAemContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HeadlineModelTest {

    private final AemContext context = AppAemContext.newAemContext();

    private HeadlineModel model;
    private Resource resource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeadlineModel.class);

        context.create().resource("/apps/aem-aipilot/components/headline",
            "sling:resourceSuperType", "aem-aipilot/components/title");
        context.create().resource("/apps/aem-aipilot/components/title",
            "sling:resourceSuperType", "core/wcm/components/title/v3/title");

        Page page = context.create().page("/content/mypage");
        resource = context.create().resource(page, "headline",
            "sling:resourceType", "aem-aipilot/components/headline",
            "jcr:title", "Main Title",
            "subtitle", "Subtitle text");

        context.currentResource(resource);
        model = context.request().adaptTo(HeadlineModel.class);
    }

    @Test
    void testWithCompleteData() {
        assertNotNull(model);
        assertEquals("Main Title", model.getText());
        assertEquals("Subtitle text", model.getSubtitle());
        assertTrue(model.hasContent());
    }

    @Test
    void testWhenEmpty() {
        Resource emptyResource = context.create().resource(resource, "empty",
            "sling:resourceType", "aem-aipilot/components/headline");
        context.currentResource(emptyResource);
        HeadlineModel emptyModel = context.request().adaptTo(HeadlineModel.class);

        assertNotNull(emptyModel);
        assertNotNull(emptyModel.getText());
        assertNull(emptyModel.getSubtitle());
    }

    @Test
    void testWithSubtitleOnly() {
        Resource subtitleOnly = context.create().resource(resource, "subonly",
            "sling:resourceType", "aem-aipilot/components/headline",
            "subtitle", "Just subtitle");
        context.currentResource(subtitleOnly);
        HeadlineModel subtitleModel = context.request().adaptTo(HeadlineModel.class);

        assertNotNull(subtitleModel);
        assertNotNull(subtitleModel.getText());
        assertEquals("Just subtitle", subtitleModel.getSubtitle());
        assertTrue(subtitleModel.hasContent());
    }

    @Test
    void testGetLinkURL() {
        assertNotNull(model);
        assertNull(model.getLinkURL());
    }
}

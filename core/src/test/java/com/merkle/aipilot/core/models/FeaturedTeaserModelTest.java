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

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class FeaturedTeaserModelTest {

    private final AemContext context = new AemContext();

    private Page page;
    private Resource componentResource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(FeaturedTeaserModel.class);
        page = context.create().page("/content/aem-aipilot/test-page");
    }

    @Test
    void testWithCompleteData() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "preTitle", "Featured",
            "headline", "Welcome to Merkle",
            "description", "A short description for the teaser",
            "imageRef", "/content/dam/aem-aipilot/asset.jpg",
            "ctaLink", "/content/aem-aipilot/us/en",
            "ctaText", "Learn More",
            "ctaTarget", "_blank",
            "variant", "fullscreen",
            "imagePosition", "left");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("Featured", model.getPreTitle());
        assertEquals("Welcome to Merkle", model.getHeadline());
        assertEquals("A short description for the teaser", model.getDescription());
        assertEquals("/content/dam/aem-aipilot/asset.jpg", model.getImageRef());
        assertTrue(model.getCtaLink().contains("/content/aem-aipilot/us/en"));
        assertEquals("Learn More", model.getCtaText());
        assertEquals("_blank", model.getCtaTarget());
        assertEquals("fullscreen", model.getVariant());
        assertEquals("left", model.getImagePosition());
        assertTrue(model.hasCta());
        assertTrue(model.hasContent());
    }

    @Test
    void testWhenEmpty() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertNull(model.getHeadline());
        assertNull(model.getPreTitle());
        assertNull(model.getDescription());
        assertNull(model.getImageRef());
        assertNull(model.getCtaLink());
        assertNull(model.getCtaText());
        assertFalse(model.hasContent());
        assertFalse(model.hasCta());
    }

    @Test
    void testWithOnlyHeadline() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Just a Headline");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("Just a Headline", model.getHeadline());
        assertNull(model.getPreTitle());
        assertNull(model.getDescription());
        assertNull(model.getImageRef());
        assertNull(model.getCtaLink());
        assertFalse(model.hasCta());
        assertTrue(model.hasContent());
    }

    @Test
    void testWithHeadlineAndDescription() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Headline with Description",
            "description", "This is a description text");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("Headline with Description", model.getHeadline());
        assertEquals("This is a description text", model.getDescription());
        assertNull(model.getPreTitle());
        assertNull(model.getCtaLink());
        assertFalse(model.hasCta());
        assertTrue(model.hasContent());
    }

    @Test
    void testWithPreTitleAboveHeadline() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "preTitle", "Pre Title Text",
            "headline", "Main Headline");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("Pre Title Text", model.getPreTitle());
        assertEquals("Main Headline", model.getHeadline());
        assertTrue(model.hasContent());
    }

    @Test
    void testSideBySideVariant() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Side by Side",
            "imageRef", "/content/dam/aem-aipilot/asset.jpg",
            "variant", "sidebyside",
            "imagePosition", "right");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("sidebyside", model.getVariant());
        assertEquals("right", model.getImagePosition());
        assertEquals("/content/dam/aem-aipilot/asset.jpg", model.getImageRef());
        assertTrue(model.hasContent());
    }

    @Test
    void testDefaultValues() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Default Values");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("fullscreen", model.getVariant());
        assertEquals("left", model.getImagePosition());
        assertEquals("_self", model.getCtaTarget());
    }

    @Test
    void testCtaWithIcon() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Test",
            "ctaLink", "/content/aem-aipilot/us/en",
            "ctaText", "Learn More",
            "ctaIcon", "/content/dam/aem-aipilot/icon.svg");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("/content/dam/aem-aipilot/icon.svg", model.getCtaIcon());
        assertTrue(model.hasCta());
    }

    @Test
    void testCtaWithIconOnly() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Test",
            "ctaLink", "/content/aem-aipilot/us/en",
            "ctaIcon", "/content/dam/aem-aipilot/icon.svg");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("/content/dam/aem-aipilot/icon.svg", model.getCtaIcon());
        assertNull(model.getCtaText());
        assertTrue(model.hasCta());
    }

    @Test
    void testStyleSystemColorBlack() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Test");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("cmp-featured-teaser--color-black", model.getColorStyleClass());
    }

    @Test
    void testStyleSystemColorWhite() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Test",
            "cq:styleIds", new String[]{"featured-teaser-color-white"});

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("cmp-featured-teaser--color-white", model.getColorStyleClass());
    }

    @Test
    void testCtaLinkExternal() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Test",
            "ctaLink", "https://example.com",
            "ctaText", "Visit");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertEquals("https://example.com", model.getCtaLink());
        assertTrue(model.hasCta());
    }

    @Test
    void testCtaWithoutText() {
        componentResource = context.create().resource(page, "featured-teaser",
            "sling:resourceType", "aem-aipilot/components/featured-teaser",
            "headline", "Test",
            "ctaLink", "/content/aem-aipilot/us/en");

        FeaturedTeaserModel model = componentResource.adaptTo(FeaturedTeaserModel.class);

        assertNotNull(model);
        assertNull(model.getCtaText());
        assertFalse(model.hasCta());
    }
}

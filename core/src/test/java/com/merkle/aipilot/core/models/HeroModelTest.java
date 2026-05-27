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
import com.merkle.aipilot.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class HeroModelTest {

    private final AemContext context = AppAemContext.newAemContext();

    private Resource resource;
    private Page page;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroModel.class);
        page = context.create().page("/content/mypage");
    }

    @Test
    void testWithCompleteData() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "image",
            "imageRef", "/content/dam/test-image.jpg",
            "headline", "Welcome to Our Site",
            "description", "This is a description of the hero section.",
            "ctaLink", "/content/mypage",
            "ctaText", "Learn More",
            "ctaIcon", "icon-arrow",
            "ctaTarget", "_blank",
            "horizontalAlignment", "center",
            "verticalAlignment", "top",
            "opacity", "50");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model);
        assertEquals("image", model.getBackgroundType());
        assertEquals("/content/dam/test-image.jpg", model.getImageRef());
        assertEquals("/content/dam/test-image.jpg", model.getBackgroundRef());
        assertEquals("Welcome to Our Site", model.getHeadline());
        assertEquals("This is a description of the hero section.", model.getDescription());
        assertNotNull(model.getCtaLink());
        assertEquals("Learn More", model.getCtaText());
        assertEquals("icon-arrow", model.getCtaIcon());
        assertEquals("_blank", model.getCtaTarget());
        assertEquals("center", model.getHorizontalAlignment());
        assertEquals("top", model.getVerticalAlignment());
        assertEquals("50", model.getOpacity());
        assertTrue(model.hasCta());
        assertTrue(model.hasContent());
    }

    @Test
    void testWhenEmpty() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE);

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model);
        assertNull(model.getImageRef());
        assertNull(model.getVideoRef());
        assertNull(model.getBackgroundRef());
        assertNull(model.getHeadline());
        assertNull(model.getDescription());
        assertNull(model.getCtaLink());
        assertFalse(model.hasCta());
        assertFalse(model.hasContent());
    }

    @Test
    void testWithPartialData() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "headline", "Minimal Hero");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model);
        assertEquals("Minimal Hero", model.getHeadline());
        assertNull(model.getDescription());
        assertNull(model.getCtaLink());
        assertTrue(model.hasContent());
    }

    @Test
    void testDefaultValues() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE);

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model);
        assertEquals("image", model.getBackgroundType());
        assertEquals("0", model.getOpacity());
        assertEquals("left", model.getHorizontalAlignment());
        assertEquals("middle", model.getVerticalAlignment());
        assertEquals("_self", model.getCtaTarget());
    }

    @Test
    void testHasCtaWithLinkAndText() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaLink", "/content/page1",
            "ctaText", "Click Here");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertTrue(model.hasCta());
    }

    @Test
    void testHasCtaWithoutTextAndIcon() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaLink", "/content/page1");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertFalse(model.hasCta());
    }

    @Test
    void testHasCtaWithLinkAndIcon() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaLink", "/content/page1",
            "ctaIcon", "/content/dam/icon.svg");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertTrue(model.hasCta());
    }

    @Test
    void testHasCtaWithoutLink() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaText", "Click Here");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertFalse(model.hasCta());
    }

    @Test
    void testExternalLinkDoesNotGetExternalized() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaLink", "https://example.com/page");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("https://example.com/page", model.getCtaLink());
    }

    @Test
    void testAnchorLinkDoesNotGetExternalized() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaLink", "#section");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("#section", model.getCtaLink());
    }

    @Test
    void testInternalLinkGetsMapped() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "ctaLink", "/content/mypage");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model.getCtaLink());
        assertTrue(model.getCtaLink().startsWith("http"));
        assertTrue(model.getCtaLink().contains("/content/mypage.html"));
    }

    @Test
    void testImageBackgroundType() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "image",
            "imageRef", "/content/dam/image.jpg");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("image", model.getBackgroundType());
        assertEquals("/content/dam/image.jpg", model.getImageRef());
        assertEquals("/content/dam/image.jpg", model.getBackgroundRef());
        assertNull(model.getVideoRef());
    }

    @Test
    void testVideoBackgroundType() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video",
            "videoRef", "/content/dam/video.mp4");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("video", model.getBackgroundType());
        assertEquals("/content/dam/video.mp4", model.getVideoRef());
        assertEquals("/content/dam/video.mp4", model.getBackgroundRef());
        assertNull(model.getImageRef());
    }

    @Test
    void testVideoUrlReturnsNullForImageType() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "image",
            "imageRef", "/content/dam/image.jpg");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNull(model.getVideoUrl());
    }

    @Test
    void testVideoUrlReturnsNullWhenNoRef() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNull(model.getVideoUrl());
    }

    @Test
    void testVideoUrlForVideoType() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video",
            "videoRef", "/content/dam/video.mp4");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model.getVideoUrl());
        assertTrue(model.getVideoUrl().contains("/content/dam/video.mp4"));
    }

    @Test
    void testVideoMimeTypeForMp4() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video",
            "videoRef", "/content/dam/video.mp4");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("video/mp4", model.getVideoMimeType());
    }

    @Test
    void testVideoMimeTypeForWebM() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video",
            "videoRef", "/content/dam/video.webm");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("video/webm", model.getVideoMimeType());
    }

    @Test
    void testVideoMimeTypeForOgg() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video",
            "videoRef", "/content/dam/video.ogv");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("video/ogg", model.getVideoMimeType());
    }

    @Test
    void testVideoMimeTypeReturnsNullForImage() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "image",
            "imageRef", "/content/dam/image.jpg");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNull(model.getVideoMimeType());
    }

    @Test
    void testColorStyleClassDefaultsToBlack() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE);

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("cmp-hero--color-black", model.getColorStyleClass());
    }

    @Test
    void testColorStyleClassWithWhiteStyle() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "cq:styleIds", new String[]{"hero-color-white"});

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("cmp-hero--color-white", model.getColorStyleClass());
    }

    @Test
    void testColorStyleClassWithMultipleStyles() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "cq:styleIds", new String[]{"some-other-style", "hero-color-white"});

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertEquals("cmp-hero--color-white", model.getColorStyleClass());
    }

    @Test
    void testVideoUrlWithResourceResolverMap() {
        resource = context.create().resource(page, "hero",
            "sling:resourceType", HeroModel.RESOURCE_TYPE,
            "backgroundType", "video",
            "videoRef", "/content/dam/video.mp4");

        HeroModel model = resource.adaptTo(HeroModel.class);

        assertNotNull(model.getVideoUrl());
        assertNotNull(model.getVideoMimeType());
    }
}

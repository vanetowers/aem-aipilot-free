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
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.commons.Externalizer;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroModel {

    public static final String RESOURCE_TYPE = "aem-aipilot/components/hero";

    @ValueMapValue
    @Default(values = "image")
    private String backgroundType;

    @ValueMapValue
    private String imageRef;

    @ValueMapValue
    private String videoRef;

    @ValueMapValue
    @Default(values = "0")
    private String opacity;

    @ValueMapValue
    private String headline;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String ctaLink;

    @ValueMapValue
    private String ctaText;

    @ValueMapValue
    private String ctaIcon;

    @ValueMapValue
    @Default(values = "_self")
    private String ctaTarget;

    @ValueMapValue
    @Default(values = "left")
    private String horizontalAlignment;

    @ValueMapValue
    @Default(values = "middle")
    private String verticalAlignment;

    @ValueMapValue(name = "cq:styleIds")
    private String[] styleIds;

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private Externalizer externalizer;

    public String getBackgroundType() {
        return backgroundType;
    }

    public String getBackgroundRef() {
        if ("video".equals(backgroundType)) {
            return videoRef;
        }
        return imageRef;
    }

    public String getOpacity() {
        return opacity;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public String getImageRef() {
        return imageRef;
    }

    public String getVideoRef() {
        return videoRef;
    }

    public String getCtaLink() {
        if (ctaLink == null || ctaLink.isEmpty()) {
            return null;
        }
        if (ctaLink.startsWith("http") || ctaLink.startsWith("#")) {
            return ctaLink;
        }
        String link = ctaLink;
        if (!link.endsWith(".html")) {
            link = link + ".html";
        }
        if (externalizer != null) {
            return externalizer.externalLink(resourceResolver, Externalizer.PUBLISH, link);
        }
        return link;
    }

    public String getVideoUrl() {
        if (videoRef == null || videoRef.isEmpty()) {
            return null;
        }
        if (!"video".equals(backgroundType)) {
            return null;
        }
        if (resourceResolver != null) {
            return resourceResolver.map(videoRef);
        }
        return videoRef;
    }

    public String getVideoMimeType() {
        if (videoRef == null || !"video".equals(backgroundType)) {
            return null;
        }
        String lower = videoRef.toLowerCase();
        if (lower.endsWith(".mp4")) return "video/mp4";
        if (lower.endsWith(".webm")) return "video/webm";
        if (lower.endsWith(".ogg") || lower.endsWith(".ogv")) return "video/ogg";
        return null;
    }

    public String getCtaText() {
        return ctaText;
    }

    public String getCtaIcon() {
        return ctaIcon;
    }

    public String getCtaTarget() {
        return ctaTarget;
    }

    public String getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    public String getColorStyleClass() {
        if (styleIds != null) {
            for (String id : styleIds) {
                if ("hero-color-white".equals(id)) {
                    return "cmp-hero--color-white";
                }
            }
        }
        return "cmp-hero--color-black";
    }

    public boolean hasCta() {
        return ctaLink != null && !ctaLink.isEmpty()
            && (ctaText != null && !ctaText.isEmpty() || ctaIcon != null && !ctaIcon.isEmpty());
    }

    public boolean hasContent() {
        return getBackgroundRef() != null || headline != null;
    }
}

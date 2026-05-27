package com.merkle.aipilot.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Title;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {HeadlineModel.class, Title.class},
    resourceType = HeadlineModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeadlineModel implements Title {

    public static final String RESOURCE_TYPE = "aem-aipilot/components/headline";

    @Self
    @Via(type = ResourceSuperType.class)
    private Title coreTitle;

    @ValueMapValue
    private String subtitle;

    public String getSubtitle() {
        return subtitle;
    }

    public boolean hasContent() {
        return coreTitle != null || subtitle != null;
    }

    @Override
    public String getText() {
        return coreTitle != null ? coreTitle.getText() : null;
    }

    @Override
    public String getType() {
        return coreTitle != null ? coreTitle.getType() : null;
    }

    @Override
    public Link getLink() {
        return coreTitle != null ? coreTitle.getLink() : null;
    }

    @Override
    public String getLinkURL() {
        return coreTitle != null ? coreTitle.getLinkURL() : null;
    }

    @Override
    public boolean isLinkDisabled() {
        return coreTitle != null && coreTitle.isLinkDisabled();
    }

    @Override
    public String getId() {
        return coreTitle != null ? coreTitle.getId() : null;
    }

    @Override
    public ComponentData getData() {
        return coreTitle != null ? coreTitle.getData() : null;
    }
}

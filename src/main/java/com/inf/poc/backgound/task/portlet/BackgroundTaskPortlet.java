package com.inf.poc.backgound.task.portlet;

import com.inf.poc.backgound.task.SampleBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.requires-namespaced-parameters=false",
                "com.liferay.portlet.display-category=category.background",
                "com.liferay.portlet.instanceable=false",
                "javax.portlet.name=BackgroundTaskPortlet",
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.display-name=BackgroundTaskPortlet",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp"
        },
        service = Portlet.class
)
public class BackgroundTaskPortlet extends MVCPortlet {

    static final private Log LOGGER = LogFactoryUtil.getLog(BackgroundTaskPortlet.class);

    @Reference
    protected BackgroundTaskManager backgroundTaskManager;

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {
        Random random = new Random(12);
        HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
        ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
        ServiceContext serviceContext = null;
        try {
            serviceContext = ServiceContextFactory.getInstance(renderRequest);
        } catch (PortalException pe) {
            LOGGER.error("Error in gettting service context", pe.getCause());
        }

        Map<String, Serializable> taskContextMap = new HashMap<>();
        taskContextMap.put("processName", "testing"+random.nextInt());
        taskContextMap.put("totalNodes", String.valueOf(random.nextInt()));

        try {
            BackgroundTask backgroundTask = backgroundTaskManager.addBackgroundTask(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(),
                    SampleBackgroundTaskExecutor.class.getName(), SampleBackgroundTaskExecutor.class.getName(),
                    taskContextMap, serviceContext);

            renderRequest.setAttribute("backgroundTaskId", String.valueOf(backgroundTask.getBackgroundTaskId()));
        } catch (PortalException e) {
            e.printStackTrace();
        }

        super.doView(renderRequest, renderResponse);
    }

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws IOException, PortletException {
        Long backgroundTaskId = ParamUtil.getLong(resourceRequest, "backgroundTaskId");
        PrintWriter out = resourceResponse.getWriter();
        out.println(backgroundTaskManager.getBackgroundTaskStatusJSON(backgroundTaskId));
        super.serveResource(resourceRequest, resourceResponse);
    }
}

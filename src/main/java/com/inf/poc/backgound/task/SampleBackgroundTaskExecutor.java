package com.inf.poc.backgound.task;

import com.inf.poc.backgound.task.model.SampleBackgroundTask;
import com.liferay.portal.kernel.backgroundtask.*;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplayFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.osgi.service.component.annotations.Component;

import java.io.Serializable;
import java.util.Map;

@Component(
        immediate = true,
        property = {"background.task.executor.class.name=com.inf.poc.backgound.task.SampleBackgroundTaskExecutor"},
        service = BackgroundTaskExecutor.class
)
public class SampleBackgroundTaskExecutor extends BaseBackgroundTaskExecutor {

    public static final Log LOGGER = LogFactoryUtil.getLog(SampleBackgroundTaskExecutor.class);

    public SampleBackgroundTaskExecutor() {
        setBackgroundTaskStatusMessageTranslator(
                new SampleBackgroundTaskStatusMessageTranslator());
    }

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {
        Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();
        String taskName = (String)taskContextMap.get("processName") ;
        String totalNodes = (String)taskContextMap.get("totalNodes");

        SampleBackgroundTask sampleBackgroundTask = new SampleBackgroundTask();
        sampleBackgroundTask.setTotalNodes(totalNodes);

        SampleDataHandlerStatusMessageSenderUtil.sendStatusMessage(sampleBackgroundTask);

        BackgroundTaskResult backgroundTaskResult = new BackgroundTaskResult(BackgroundTaskConstants.STATUS_SUCCESSFUL);
        backgroundTaskResult.setStatusMessage("Wonderful Job Done");

        return backgroundTaskResult;
    }

    @Override
    public boolean isSerial() {
        return Boolean.TRUE;
    }

    @Override
    public BackgroundTaskDisplay getBackgroundTaskDisplay(BackgroundTask backgroundTask) {
        return BackgroundTaskDisplayFactoryUtil.getBackgroundTaskDisplay(backgroundTask);
    }

    @Override
    public BackgroundTaskExecutor clone() {
        return this;
    }
}

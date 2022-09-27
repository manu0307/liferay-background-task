package com.inf.poc.backgound.task;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatus;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatusMessageTranslator;
import com.liferay.portal.kernel.messaging.Message;

public class SampleBackgroundTaskStatusMessageTranslator implements BackgroundTaskStatusMessageTranslator {

    @Override
    public void translate(BackgroundTaskStatus backgroundTaskStatus, Message message) {
        backgroundTaskStatus.setAttribute("totalNodes", message.getLong("totalNodes"));
    }
}

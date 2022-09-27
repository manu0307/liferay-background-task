package com.inf.poc.backgound.task;

import com.inf.poc.backgound.task.model.SampleBackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskThreadLocal;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;

public class SampleDataHandlerStatusMessageSenderUtil {

    public static void sendStatusMessage(SampleBackgroundTask messageContent) {
        if(!BackgroundTaskThreadLocal.hasBackgroundTask()) {
            return;
        }

        Message message = createMessage(messageContent);
        MessageBusUtil.sendMessage(DestinationNames.BACKGROUND_TASK_STATUS, message);
    }

    protected static Message createMessage(SampleBackgroundTask messageContent) {
        Message message = new Message();
        message.put("backgroundTaskId", BackgroundTaskThreadLocal.getBackgroundTaskId());
        message.put("totalNodes", messageContent.getTotalNodes());
        return message;
    }
}

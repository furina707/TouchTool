package top.bogey.touch_tool.bean.action.normal;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.bogey.touch_tool.bean.action.Action;
import top.bogey.touch_tool.bean.action.ActionType;
import top.bogey.touch_tool.bean.action.parent.DynamicPinsAction;
import top.bogey.touch_tool.bean.action.parent.ExecuteAction;
import top.bogey.touch_tool.bean.pin.Pin;
import top.bogey.touch_tool.bean.pin.pin_objects.PinBase;
import top.bogey.touch_tool.bean.pin.pin_objects.PinObject;
import top.bogey.touch_tool.bean.save.task.TaskSaver;
import top.bogey.touch_tool.bean.task.Task;
import top.bogey.touch_tool.service.TaskRunnable;
import top.bogey.touch_tool.ui.blueprint.card.FloatInputConfigActionCard;

public class InputConfigAction extends ExecuteAction implements DynamicPinsAction {

    public InputConfigAction() {
        super(ActionType.INPUT_CONFIG);
    }

    public InputConfigAction(JsonObject jsonObject) {
        super(jsonObject);
        tmpPins.forEach(this::addPin);
        tmpPins.clear();
    }

    @Override
    public void execute(TaskRunnable runnable, Pin pin) {
        Map<String, Pin> inPins = new HashMap<>();
        Map<String, Pin> outPins = new HashMap<>();
        for (Pin dynamicPin : getDynamicPins()) {
            if (dynamicPin.isOut()) outPins.put(dynamicPin.getUid(), dynamicPin);
            else inPins.put(dynamicPin.getUid(), dynamicPin);
        }
        inPins.forEach((uid, inPin) -> {
            PinObject pinValue = getPinValue(runnable, inPin);
            inPin.getValue().sync(pinValue);
        });

        FloatInputConfigActionCard.showInputConfig(runnable.getTask(), this, result -> runnable.resume());
        runnable.await();

        outPins.forEach((uid, outPin) -> {
            Pin inPin = inPins.get(uid);
            if (inPin != null) outPin.getValue().sync(inPin.getValue());
        });

        // 将当前值保存一下
        Task task = runnable.getTask();
        Task topTask = task.getTopParent();
        Task saveTask = TaskSaver.getInstance().getTask(topTask.getId());
        Task currentTask = saveTask.downFindTask(task.getId());
        Action action = currentTask.getAction(getId());
        if (action instanceof InputConfigAction inputConfigAction) {
            for (Pin dynamicPin : inputConfigAction.getDynamicPins()) {
                Pin pinById = getPinById(dynamicPin.getId());
                dynamicPin.getValue().sync(pinById.getValue());
            }
            saveTask.save();
        }

        executeNext(runnable, outPin);
    }

    @Override
    public List<Pin> getDynamicPins() {
        List<Pin> pins = new ArrayList<>();
        boolean start = false;
        for (Pin pin : getPins()) {
            if (start) pins.add(pin);
            if (pin == outPin) start = true;
        }
        return pins;
    }

    @Override
    public void removePin(Task context, Pin pin) {
        super.removePin(context, pin);
        Pin pinByUid = getPinByUid(pin.getUid());
        if (pinByUid != null) super.removePin(context, pinByUid);
    }

    @Override
    public void onValueReplaced(Task task, Pin origin, PinBase value) {
        super.onValueReplaced(task, origin, value);
        for (Pin dynamicPin : getDynamicPins()) {
            if (dynamicPin == origin) continue;
            if (dynamicPin.getUid().equals(origin.getUid())) {
                if (dynamicPin.getValue().equals(value)) continue;
                dynamicPin.setValue(task, value.copy());
            }
        }
    }

    @Override
    public void onTitleChanged(Pin origin, String title) {
        super.onTitleChanged(origin, title);
        for (Pin dynamicPin : getDynamicPins()) {
            if (dynamicPin == origin) continue;
            if (dynamicPin.getUid().equals(origin.getUid())) {
                if (dynamicPin.getTitle().equals(title)) continue;
                dynamicPin.setTitle(title);
            }
        }
    }
}

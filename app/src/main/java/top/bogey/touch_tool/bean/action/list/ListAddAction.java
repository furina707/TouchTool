package top.bogey.touch_tool.bean.action.list;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.bean.action.ActionType;
import top.bogey.touch_tool.bean.pin.Pin;
import top.bogey.touch_tool.bean.pin.pin_objects.PinBoolean;
import top.bogey.touch_tool.bean.pin.pin_objects.PinObject;
import top.bogey.touch_tool.bean.pin.pin_objects.PinSubType;
import top.bogey.touch_tool.bean.pin.pin_objects.pin_list.PinList;
import top.bogey.touch_tool.bean.pin.pin_objects.pin_number.PinInteger;
import top.bogey.touch_tool.bean.pin.pin_objects.pin_number.PinNumber;
import top.bogey.touch_tool.service.TaskRunnable;

public class ListAddAction extends ListExecuteAction {
    private final transient Pin listPin = new Pin(new PinList());
    private final transient Pin elementPin = new Pin(new PinObject(PinSubType.DYNAMIC), R.string.pin_object);
    private final transient Pin resultPin = new Pin(new PinBoolean(), R.string.pin_boolean_result, true);
    private final transient Pin indexPin = new Pin(new PinInteger(-1), R.string.list_add_action_index, false, false, true);

    public ListAddAction() {
        super(ActionType.LIST_ADD);
        addPins(listPin, elementPin, resultPin, indexPin);
    }

    public ListAddAction(JsonObject jsonObject) {
        super(jsonObject);
        reAddPin(listPin);
        reAddPin(elementPin, true);
        reAddPins(resultPin, indexPin);
    }

    @Override
    public void execute(TaskRunnable runnable, Pin pin) {
        PinList list = getPinValue(runnable, listPin);
        PinObject element = getPinValue(runnable, elementPin);
        PinNumber<?> index = getPinValue(runnable, indexPin);
        int i = index.intValue();
        if (i == 0) i = 1; // 0和1一样，代表添加到第一个位置
        if (i < 0) i = list.size() + i + 2; // 这里+2，因为索引从1开始，之后会再-1
        i--;

        if (i < 0 || i > list.size()) {
            resultPin.getValue(PinBoolean.class).setValue(false);
        } else {
            list.add(i, element);
            resultPin.getValue(PinBoolean.class).setValue(true);
        }
        executeNext(runnable, outPin);
    }

    @NonNull
    @Override
    public List<Pin> getDynamicTypePins() {
        return Arrays.asList(listPin, elementPin);
    }

}

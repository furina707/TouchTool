package top.bogey.touch_tool.ui.blueprint.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;

import top.bogey.touch_tool.MainApplication;
import top.bogey.touch_tool.bean.action.Action;
import top.bogey.touch_tool.bean.pin.Pin;
import top.bogey.touch_tool.bean.save.SettingSaver;
import top.bogey.touch_tool.bean.task.Task;
import top.bogey.touch_tool.databinding.FloatInputConfigCardBinding;
import top.bogey.touch_tool.ui.blueprint.picker.FloatBaseCallback;
import top.bogey.touch_tool.ui.blueprint.pin.PinInputConfigView;
import top.bogey.touch_tool.ui.blueprint.pin.PinView;
import top.bogey.touch_tool.ui.custom.ActionFloatViewCallback;
import top.bogey.touch_tool.ui.custom.KeepAliveFloatView;
import top.bogey.touch_tool.utils.EAnchor;
import top.bogey.touch_tool.utils.callback.BooleanResultCallback;
import top.bogey.touch_tool.utils.float_window_manager.FloatInterface;
import top.bogey.touch_tool.utils.float_window_manager.FloatWindow;

@SuppressLint("ViewConstructor")
public class FloatInputConfigActionCard extends ActionCard implements FloatInterface {

    public static void showInputConfig(Task task, Action action, BooleanResultCallback callback) {
        KeepAliveFloatView keepView = (KeepAliveFloatView) FloatWindow.getView(KeepAliveFloatView.class.getName());
        if (keepView == null) return;
        new Handler(Looper.getMainLooper()).post(() -> {
            FloatInputConfigActionCard card = new FloatInputConfigActionCard(keepView.getThemeContext(), task, action, callback);
            card.show();
        });
    }

    private final BooleanResultCallback callback;
    private FloatInputConfigCardBinding binding;

    public FloatInputConfigActionCard(Context context, Task task, Action action, BooleanResultCallback callback) {
        super(context, task, action);
        this.callback = callback;
    }

    @Override
    public void init() {
        binding = FloatInputConfigCardBinding.inflate(LayoutInflater.from(getContext()), this, true);
        binding.enterButton.setOnClickListener(v -> {
            callback.onResult(true);
            dismiss();
        });
    }

    @Override
    public void refreshCardInfo() {

    }

    @Override
    public void refreshCardLockState() {

    }

    @Override
    public boolean check() {
        return true;
    }

    @Override
    public void addPinView(Pin pin, int offset) {
        if (pin.isDynamic()) {
            PinView pinView = new PinInputConfigView(getContext(), this, pin);
            binding.inBox.addView(pinView);
            pinView.expand(Action.ExpandType.FULL);
            pinViews.put(pin.getId(), pinView);
        }
    }

    @Override
    public void show() {
        Point point = SettingSaver.getInstance().getManualChoiceViewPos();
        FloatWindow.with(MainApplication.getInstance().getService())
                .setLayout(this)
                .setTag(FloatInputConfigActionCard.class.getName())
                .setLocation(EAnchor.CENTER, point.x, point.y)
                .setSpecial(true)
                .setExistEditText(true)
                .setCallback(new ActionFloatViewCallback(FloatInputConfigActionCard.class.getName()))
                .show();
        FloatBaseCallback.Block = true;
    }

    @Override
    public void dismiss() {
        FloatWindow.dismiss(FloatInputConfigActionCard.class.getName());
        FloatBaseCallback.Block = false;
    }
}

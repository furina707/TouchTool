package top.bogey.touch_tool.ui.tool;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import top.bogey.touch_tool.MainApplication;
import top.bogey.touch_tool.databinding.ViewToolBinding;
import top.bogey.touch_tool.service.MainAccessibilityService;
import top.bogey.touch_tool.ui.blueprint.picker.NodePickerPreview;
import top.bogey.touch_tool.ui.tool.app_info.AppInfoFloatView;
import top.bogey.touch_tool.ui.tool.log.LogFloatView;

public class ToolView extends Fragment {
    public static final String TOOL_CAPTURE_SERVICE = "capture_service";
    public static final String TOOL_PACKAGE_ACTIVITY = "package_activity";
    public static final String TOOL_NODE_PICKER = "node_picker";
    public static final String TOOL_RUNNING_LOG = "running_log";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewToolBinding binding = ViewToolBinding.inflate(inflater, container, false);
        binding.captureServiceButton.setOnClickListener(v -> openTool(getContext(), TOOL_CAPTURE_SERVICE));
        binding.packageActivityButton.setOnClickListener(v -> openTool(getContext(), TOOL_PACKAGE_ACTIVITY));
        binding.nodePickerButton.setOnClickListener(v -> openTool(getContext(), TOOL_NODE_PICKER));
        binding.runningLogButton.setOnClickListener(v -> openTool(getContext(), TOOL_RUNNING_LOG));

        return binding.getRoot();
    }

    public static void openTool(Context context, String toolName) {
        if (toolName == null || toolName.isEmpty()) return;
        switch (toolName) {
            case TOOL_CAPTURE_SERVICE -> {
                MainAccessibilityService service = MainApplication.getInstance().getService();
                if (service != null && service.isEnabled()) {
                    if (service.isCaptureEnabled()) {
                        service.stopCapture();
                    } else {
                        service.startCapture(null);
                    }
                }
            }
            case TOOL_PACKAGE_ACTIVITY -> new AppInfoFloatView(context).show();
            case TOOL_NODE_PICKER -> new NodePickerPreview(context, null, null).show();
            case TOOL_RUNNING_LOG -> new LogFloatView(context).show();
        }
    }
}
